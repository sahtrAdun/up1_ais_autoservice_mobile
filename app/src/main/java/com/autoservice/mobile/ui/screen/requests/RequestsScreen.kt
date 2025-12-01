package com.autoservice.mobile.ui.screen.requests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.autoservice.mobile.domain.model.ServiceRequest
import com.autoservice.mobile.ui.component.BaseColumn
import com.autoservice.mobile.ui.viewmodel.RequestsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
    onBackClick: () -> Unit,
    viewModel: RequestsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои заявки") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        BaseColumn(
            onRefresh = viewModel::loadRequests,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.requests.isEmpty() -> {
                    Text(text = "У вас пока нет заявок")
                }
                else -> {
                    val sorted = remember(state.requests) {
                        state.requests.sortedByDescending { it.id }
                    }

                    sorted.forEach { request ->
                        RequestItem(request = request)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RequestItem(request: ServiceRequest) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Заявка #${request.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = getStatusText(request.status),
                    color = getStatusColor(request.status)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = request.description,
                style = MaterialTheme.typography.bodyMedium
            )

            if (request.estimatedCost > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Стоимость: ${request.estimatedCost} руб.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun getStatusText(status: String): String {
    return when (status) {
        "PENDING" -> "Ожидание"
        "IN_PROGRESS" -> "В работе"
        "COMPLETED" -> "Завершено"
        "CANCELLED" -> "Отменено"
        else -> status
    }
}

@Composable
private fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status) {
        "PENDING" -> MaterialTheme.colorScheme.onSurfaceVariant
        "IN_PROGRESS" -> MaterialTheme.colorScheme.primary
        "COMPLETED" -> MaterialTheme.colorScheme.tertiary
        "CANCELLED" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
}