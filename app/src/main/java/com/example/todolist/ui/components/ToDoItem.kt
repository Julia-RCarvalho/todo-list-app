package com.example.todolist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.domain.ToDo
import com.example.todolist.domain.toDo1
import com.example.todolist.domain.toDo2
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
fun ToDoItem(
    todo: ToDo,
    onCompletedChange: (Boolean) -> Unit,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val cardBackground = if (isDarkTheme) Color(0xFF242424) else Color(0xFFF0F0F0)
    val internalColor = if (isDarkTheme) Color.White else Color.Black
    val completedColor = if (isDarkTheme) Color(0xFF2E7D32) else Color(0xFFC8E6C9)
    val uncompletedColor = if (isDarkTheme) Color(0xFF333333) else Color(0xFFE0E0E0)

    Surface(
        onClick = onItemClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 2.dp,
        color = cardBackground
    ) {
        Row (
             modifier = Modifier
                 .fillMaxWidth()
                 .clip(RoundedCornerShape(12.dp))
                 .background(if(todo.isCompleted) completedColor else uncompletedColor)
                 .padding(16.dp),
             verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = onCompletedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF388E3C),
                    checkmarkColor = Color.White,
                    uncheckedColor = if (isDarkTheme) Color.Gray else MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = internalColor
                )
                todo.description?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = todo.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDarkTheme) Color.LightGray else Color.DarkGray
                    )
                 }
            }

            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = internalColor
                )
            }
        }
    }
}


@Preview
@Composable
private fun ToDoItemPreview() {
    ToDoListTheme(darkTheme = false) {
        ToDoItem(
            todo = toDo1,
            onCompletedChange = {},
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview
@Composable
private fun ToDoItemDarkPreview() {
    ToDoListTheme(darkTheme = true) {
        ToDoItem(
            todo = toDo1,
            onCompletedChange = {},
            onItemClick = {},
            onDeleteClick = {},
            isDarkTheme = true
        )
    }
}

@Preview
@Composable
private fun ToDoItemCompletedPreview() {
    ToDoListTheme {
        ToDoItem(
            todo = toDo2,
            onCompletedChange = {},
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}
