package com.example.kovernotes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kovernotes.feature.addnote.AddNoteScreen
import com.example.kovernotes.feature.addnote.AddNoteViewModel
import com.example.kovernotes.feature.noteslist.NotesListScreen
import com.example.kovernotes.feature.noteslist.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppNav() }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(navController = navController, startDestination = "list") {
                composable("list") {
                    val vm: NotesListViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                    NotesListScreen(
                        viewModel = vm,
                        onNavigateToAdd = { navController.navigate("add") })
                }
                composable("add") {
                    val vm: AddNoteViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                    AddNoteScreen(viewModel = vm, onDone = { navController.popBackStack() })
                }
            }
        }
    }
}