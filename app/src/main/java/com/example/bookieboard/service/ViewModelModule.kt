package com.example.bookieboard.service

import com.example.bookieboard.data.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.ktor.client.HttpClient

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideApiRepository(httpClient: HttpClient): ApiRepository {
        return ApiRepository(httpClient)
    }
}