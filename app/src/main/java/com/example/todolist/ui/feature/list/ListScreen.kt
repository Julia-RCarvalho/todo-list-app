package com.example.todolist.ui.feature.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import com.example.todolist.R
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.example.todolist.ui.theme.ToDoListTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ListScreen(
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
        todos = todos,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    todos: List<ToDo>,
    onEvent: (ListEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    var showPhoto by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(65.dp),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.tunico_branco2),
                        contentDescription = "Logo do App",
                        modifier = Modifier
                            .height(44.dp)
                            .padding(start = 8.dp)
                    )
                },
                title = {
                    Text(
                        text = "List",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    IconButton(onClick = { showPhoto = true }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ListEvent.AddEdit(null))
                },
                containerColor = Color.Black,
                contentColor = Color.White
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
        Dialog(onDismissRequest = { showPhoto = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.conheca_tunico),
                    contentDescription = "Foto do Tunico",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview
@Composable
private fun ListContentPreview() {
    ToDoListTheme {
        ListContent(
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
