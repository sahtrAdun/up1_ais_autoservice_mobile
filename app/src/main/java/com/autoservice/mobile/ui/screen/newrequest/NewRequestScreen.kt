package com.autoservice.mobile.ui.screen.newrequest

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.autoservice.mobile.domain.model.Car
import com.autoservice.mobile.ui.component.BaseColumn
import com.autoservice.mobile.ui.viewmodel.NewRequestsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRequestScreen(
    onBackClick: () -> Unit,
    onRequestCreated: () -> Unit,
    viewModel: NewRequestsViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var description by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var carError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRequestCreated()
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(uiState.error!!)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Новое заявление") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        BaseColumn(
            onRefresh = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.cars.isEmpty() -> {
                    Text("У вас нет автомобилей")
                }
                else -> {
                    CarSelector(
                        cars = uiState.cars,
                        selectedCarId = uiState.selectedCarId,
                        onCarSelected = { viewModel.selectCar(it) },
                        error = carError
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it; descriptionError = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Опишите проблему") },
                        isError = descriptionError != null,
                        supportingText = { if (descriptionError != null) Text(descriptionError!!) },
                        minLines = 4
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            var hasError = false
                            if (uiState.selectedCarId == null) {
                                carError = "Выберите автомобиль"
                                hasError = true
                            }
                            if (description.isBlank()) {
                                descriptionError = "Описание не может быть пустым"
                                hasError = true
                            }
                            if (!hasError) {
                                viewModel.createApplication(description)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Создать заявление")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarSelector(
    cars: List<Car>,
    selectedCarId: Int?,
    onCarSelected: (Car) -> Unit,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = remember(selectedCarId, cars) {
        cars.find { it.id == selectedCarId }
    }

    val arrowAnimate by animateFloatAsState(
        targetValue = if (!expanded) 0f else 180f
    )

    Box {
        Row(
            modifier = Modifier
                .clickable(null, null) { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selected?.let { "${it.brand} ${it.model} (${it.licensePlate})" } ?: "",
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Выберите автомобиль") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Выпадающий список",
                        modifier = Modifier.rotate(arrowAnimate)
                    )
                },
                isError = error != null,
                supportingText = { if (error != null) Text(error) },
                colors = rememberFieldColors(),
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            cars.forEach { car ->
                DropdownMenuItem(
                    text = { Text("${car.brand} ${car.model} (${car.licensePlate})") },
                    onClick = {
                        onCarSelected(car)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberFieldColors(): TextFieldColors {
    val defaultFieldColors = OutlinedTextFieldDefaults.colors()
    val fieldColors = OutlinedTextFieldDefaults.colors()
        .copy(
            disabledContainerColor = defaultFieldColors.unfocusedContainerColor,
            disabledLabelColor = defaultFieldColors.unfocusedLabelColor,
            disabledLeadingIconColor = defaultFieldColors.unfocusedLeadingIconColor,
            disabledTrailingIconColor = defaultFieldColors.unfocusedTrailingIconColor,
            disabledPlaceholderColor = defaultFieldColors.unfocusedPlaceholderColor,
            disabledTextColor = defaultFieldColors.unfocusedTextColor,
        )

    return remember { fieldColors }
}
