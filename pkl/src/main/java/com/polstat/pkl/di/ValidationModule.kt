package com.polstat.pkl.di

import com.polstat.pkl.utils.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ValidationModule {

    @Provides
    @Singleton
    fun provideNimValidator(): ValidateNim {
        return ValidateNim()
    }

    @Provides
    @Singleton
    fun providePasswordValidator(): ValidatePassword {
        return ValidatePassword()
    }

}