package com.example.todolist.ui.feature.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.database.ToDoDataBaseProvider
import com.example.todolist.data.repository.ToDoRepositoryImpl
import com.example.todolist.domain.usecase.DeleteToDoUseCase
import com.example.todolist.domain.usecase.GetAllToDosUseCase
import com.example.todolist.domain.usecase.GetToDoUseCase
import com.example.todolist.domain.usecase.SaveToDoUseCase
import com.example.todolist.domain.usecase.SetToDoCompletedUseCase
import com.example.todolist.domain.usecase.ToDoUseCases
import com.example.todolist.ui.UiEvent
import com.example.todolist.ui.components.ToDoTopAppBar
import com.example.todolist.ui.components.TunicoDialog
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
fun AddEditScreen(
    id: Long?,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    navigateback: () -> Unit,
){
    val context = LocalContext.current.applicationContext
    val database = ToDoDataBaseProvider.provide(context)
    val repository = ToDoRepositoryImpl(
        dao = database.ToDoDao
    )
    val useCases = ToDoUseCases(
        getAllToDos = GetAllToDosUseCase(repository),
        getToDo = GetToDoUseCase(repository),
        saveToDo = SaveToDoUseCase(repository),
        deleteToDo = DeleteToDoUseCase(repository),
        setToDoCompleted = SetToDoCompletedUseCase(repository)
    )
    val viewModel = viewModel<AddEditViewModel> {
        AddEditViewModel(
            id = id,
            useCases = useCases
        )
    }

    val title = viewModel.title
    val description = viewModel.description

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                    )
                }

                UiEvent.NavigateBack -> {
                    navigateback()
                }

                is UiEvent.Navigate<*> -> {

                }
            }
        }
    }

    AddEditContent(
        title = title,
        description = description,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContent(
    title: String,
    description: String?,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddEditEvent) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
){
    var showPhoto by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ToDoTopAppBar(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onShowPhoto = { showPhoto = true }
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        onEvent(AddEditEvent.Close)
                    },
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }

                FloatingActionButton(
                    onClick = {
                        onEvent(AddEditEvent.Save)
                    },
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save")

                }
            }
        },

        floatingActionButtonPosition = FabPosition.Center,

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = title,
                onValueChange = {
                    onEvent(
                        AddEditEvent.TitleChanged(it)
                    )
                },
                placeholder = {
                    Text(text = "Título")
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = description ?: "",
                onValueChange = {
                    onEvent(
                        AddEditEvent.DescriptionChanged(it)
                    )
                },
                placeholder = {
                    Text(text = "Descrição (opcional)")
                }
            )
        }
    }

    if (showPhoto) {
        TunicoDialog(onDismissRequest = { showPhoto = false })
    }
}


@Preview
@Composable
private fun AddEditContentPreview() {
    ToDoListTheme{
        AddEditContent(
            title = "",
            description = null,
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            isDarkTheme = false,
            onThemeChange = {}
        )
    }
}
