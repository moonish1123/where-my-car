package com.brice.wheremycar.di

import android.content.Context
import androidx.room.Room
import com.brice.wheremycar.data.parkplace.dao.ParkDatabase
import com.brice.wheremycar.data.parkplace.dao.ParkInfoDao
import com.brice.wheremycar.data.parkplace.repository.ParkInfoFirebaseStoreRepositoryImpl
import com.brice.wheremycar.data.parkplace.repository.ParkInfoRoomRepositoryImpl
import com.brice.wheremycar.data.parkplace.repository.UserRepositoryImpl
import com.brice.wheremycar.domain.repository.ParkInfoRepository
import com.brice.wheremycar.domain.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RoomRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideParkDatabase(@ApplicationContext context: Context): ParkDatabase {
        return Room.databaseBuilder(
            context,
            ParkDatabase::class.java,
            "park_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideParkInfoDao(database: ParkDatabase): ParkInfoDao {
        return database.parkInfoDao()
    }

    @Provides
    @Singleton
    @FirebaseRepository
    fun provideFirebaseParkRepository(
        store: FirebaseFirestore
    ): ParkInfoRepository {
        return ParkInfoFirebaseStoreRepositoryImpl(store)
    }
    @Provides
    @Singleton
    @RoomRepository
    fun provideRoomParkRepository(
        dao: ParkInfoDao
    ): ParkInfoRepository {
        return ParkInfoRoomRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(store: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(store)
    }
}