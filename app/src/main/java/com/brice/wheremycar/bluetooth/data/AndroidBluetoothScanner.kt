package com.brice.wheremycar.bluetooth.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothScanner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AndroidBluetoothScanner @Inject constructor(
    @ApplicationContext private val context: Context
) : BluetoothScanner {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner

    private var scanCallback: ScanCallback? = null

    override fun startScanning(): Flow<BluetoothSignal> = callbackFlow {
        if (bluetoothLeScanner == null) {
            close(IllegalStateException("Bluetooth LE Scanner not available"))
            return@callbackFlow
        }

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                val signal = BluetoothSignal(
                    address = result.device.address,
                    rssi = result.rssi,
                    name = result.device.name
                )
                trySend(signal)
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
                results?.forEach { result ->
                    val signal = BluetoothSignal(
                        address = result.device.address,
                        rssi = result.rssi,
                        name = result.device.name
                    )
                    trySend(signal)
                }
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                close(RuntimeException("Bluetooth scan failed with error code: $errorCode"))
            }
        }

        bluetoothLeScanner.startScan(scanCallback)

        awaitClose {
            stopScanning()
        }
    }

    override fun stopScanning() {
        bluetoothLeScanner?.stopScan(scanCallback)
        scanCallback = null
    }
}