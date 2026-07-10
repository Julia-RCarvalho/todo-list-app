package com.example.todolist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todolist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoTopAppBar(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onShowPhoto: () -> Unit
) {
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
            IconButton(onClick = onThemeChange) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Alternar tema",
                    tint = Color.White
                )
            }
            IconButton(onClick = onShowPhoto) {
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
}

@Composable
fun TunicoDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
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
