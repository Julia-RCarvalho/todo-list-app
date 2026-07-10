package com.example.todolist.ui.feature.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.database.ToDoDataBaseProvider
import com.example.todolist.data.repository.ToDoRepositoryImpl
import com.example.todolist.domain.ToDo
import com.example.todolist.domain.toDo1
import com.example.todolist.domain.toDo2
import com.example.todolist.domain.toDo3
import com.example.todolist.domain.usecase.DeleteToDoUseCase
import com.example.todolist.domain.usecase.GetAllToDosUseCase
import com.example.todolist.domain.usecase.GetToDoUseCase
import com.example.todolist.domain.usecase.SaveToDoUseCase
import com.example.todolist.domain.usecase.SetToDoCompletedUseCase
import com.example.todolist.domain.usecase.ToDoUseCases
import com.example.todolist.navigation.AddEditRoute
import com.example.todolist.ui.UiEvent
import com.example.todolist.ui.components.ToDoItem
import com.example.todolist.ui.components.ToDoTopAppBar
import com.example.todolist.ui.components.TunicoDialog
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
fun ListScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    navigateToAddEditScreen: (id: Long?) -> Unit,
) {
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
    val viewModel = viewModel<ListViewModel> {
        ListViewModel(useCases = useCases)
    }

    val todos by viewModel.todos.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = uiEvent.message)
                }
                is UiEvent.Navigate<*> -> {
                    when (uiEvent.route){
                        is AddEditRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                    }
                }

                UiEvent.NavigateBack -> {

                }
            }
        }
    }

    ListContent(
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange,
        todos = todos,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    todos: List<ToDo>,
    onEvent: (ListEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    var showPhoto by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ToDoTopAppBar(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onShowPhoto = { showPhoto = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ListEvent.AddEdit(null))
                },
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(todos) { index, todo ->
                ToDoItem(
                    todo = todo,
                    isDarkTheme = isDarkTheme,
                    onCompletedChange = { isCompleted ->
                        onEvent(
                            ListEvent.CompleteChanged(
                                todo.id,
                                isCompleted
                            )
                        )
                    },
                    onItemClick = {
                        onEvent(ListEvent.AddEdit(todo.id))
                    },
                    onDeleteClick = {
                        onEvent(ListEvent.Delete(todo.id))
                    }
                )

                if (index < todos.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showPhoto) {
        TunicoDialog(onDismissRequest = { showPhoto = false })
    }
}


@Preview
@Composable
private fun ListContentPreview() {
    ToDoListTheme {
        ListContent(
            isDarkTheme = false,
            onThemeChange = {},
            todos = listOf(
                toDo1,
                toDo2,
                toDo3,
            ),
            onEvent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
