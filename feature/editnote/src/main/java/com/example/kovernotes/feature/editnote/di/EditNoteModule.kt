package com.example.kovernotes.feature.editnote.di

import com.example.kovernotes.domain.repository.NoteRepository
import com.example.kovernotes.domain.usecase.EditNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EditNoteModule {
    @Provides
    fun provideEditNoteUseCase(repo: NoteRepository): EditNoteUseCase = EditNoteUseCase(repo)
}
