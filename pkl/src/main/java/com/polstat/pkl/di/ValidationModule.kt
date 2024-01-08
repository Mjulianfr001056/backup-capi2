package com.polstat.pkl.di

import com.polstat.pkl.utils.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ValidationModule {
    @Provides
    fun provideNimValidator(): ValidateNim {
        return ValidateNim()
    }

    @Provides
    fun providePasswordValidator(): ValidatePassword {
        return ValidatePassword()
    }


}