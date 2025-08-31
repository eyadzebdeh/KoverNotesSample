package com.example.kovernotes.di

import com.example.kovernotes.data.repository.InMemoryNoteRepository
import com.example.kovernotes.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteRepository(): NoteRepository = InMemoryNoteRepository()
}
