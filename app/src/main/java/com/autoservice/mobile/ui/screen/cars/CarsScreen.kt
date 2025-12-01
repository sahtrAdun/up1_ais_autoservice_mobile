// ui/screen/cars/CarsScreen.kt
package com.autoservice.mobile.ui.screen.cars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.ui.component.AddCarDialog
import com.autoservice.mobile.ui.component.BaseColumn
import com.autoservice.mobile.ui.viewmodel.CarsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarsScreen(
    onBackClick: () -> Unit,
    viewModel: CarsViewModel = koinViewModel()
) {
    val carsState by viewModel.state.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCars()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои автомобили") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        BaseColumn(
            onRefresh = viewModel::loadCars,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                carsState.isLoading -> {
                    CircularProgressIndicator()
                }
                carsState.error != null -> {
                    Text(
                        text = carsState.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                carsState.userId == null -> {
                    Text(
                        text = "Пользователь не авторизован",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                carsState.cars.isEmpty() -> {
                    Text(
                        text = "У вас пока нет автомобилей"
                    )
                }
                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Автомобили: ${carsState.cars.size}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Button(onClick = { showAddDialog = true }) {
                            Text("Добавить")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    carsState.cars.forEach { car ->
                        CarItem(car = car)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            if (carsState.userId == null) {
                Text(
                    text = "Пользователь не авторизован",
                    color = MaterialTheme.colorScheme.error
                )

                return@BaseColumn
            }
        }
    }

    if (showAddDialog) {
        AddCarDialog(
            currentUserId = carsState.userId?.toInt(),
            onDismiss = { showAddDialog = false },
            onAddCar = { car ->
                viewModel.addCar(car)
            }
        )
    }
}

@Composable
fun CarItem(car: Car) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${car.brand} ${car.model}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Год: ${car.year}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (!car.licensePlate.isNullOrEmpty()) {
                Text(
                    text = "Номер: ${car.licensePlate}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (!car.vin.isNullOrEmpty()) {
                Text(
                    text = "VIN: ${car.vin}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}