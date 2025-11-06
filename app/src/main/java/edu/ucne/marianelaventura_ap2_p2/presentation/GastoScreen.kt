package edu.ucne.marianelaventura_ap2_p2.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreen(
    viewModel: GastoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Gastos",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(GastoEvent.ClearForm)
                    showBottomSheet = true
                },
                containerColor = Color(0xFF6200EE)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Gasto",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (state.gastos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "No hay gastos registrados",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.gastos) { gasto ->  // ← Ahora funciona correctamente
                        GastoItem(
                            gasto = gasto,
                            onClick = {
                                viewModel.onEvent(GastoEvent.SelectGasto(gasto))
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    viewModel.onEvent(GastoEvent.ClearForm)
                },
                sheetState = sheetState
            ) {
                GastoFormContent(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onDismiss = {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                            viewModel.onEvent(GastoEvent.ClearForm)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gasto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = gasto.suplidor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${gasto.monto}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF6200EE),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "NCF: ${gasto.ncf}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Fecha: ${gasto.fecha.take(10)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "ITBIS: $${gasto.itbis}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun GastoFormContent(
    state: GastoUiState,
    onEvent: (GastoEvent) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (state.gastoId == null) "Nuevo Gasto" else "Editar Gasto",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = state.fecha,
            onValueChange = { onEvent(GastoEvent.FechaChanged(it)) },
            label = { Text("Fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.fechaError != null,
            supportingText = {
                state.fechaError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            leadingIcon = {
                Icon(Icons.Default.CalendarToday, contentDescription = null)
            }
        )

        OutlinedTextField(
            value = state.suplidor,
            onValueChange = { onEvent(GastoEvent.SuplidorChanged(it)) },
            label = { Text("Suplidor") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.suplidorError != null,
            supportingText = {
                state.suplidorError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        )

        OutlinedTextField(
            value = state.ncf,
            onValueChange = { onEvent(GastoEvent.NcfChanged(it)) },
            label = { Text("NCF") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.ncfError != null,
            supportingText = {
                state.ncfError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            leadingIcon = {
                Icon(Icons.Default.Receipt, contentDescription = null)
            }
        )

        OutlinedTextField(
            value = state.itbis,
            onValueChange = { onEvent(GastoEvent.ItbisChanged(it)) },
            label = { Text("ITBIS") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.itbisError != null,
            supportingText = {
                state.itbisError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            leadingIcon = {
                Icon(Icons.Default.AttachMoney, contentDescription = null)
            }
        )

        OutlinedTextField(
            value = state.monto,
            onValueChange = { onEvent(GastoEvent.MontoChanged(it)) },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.montoError != null,
            supportingText = {
                state.montoError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            leadingIcon = {
                Icon(Icons.Default.Money, contentDescription = null)
            }
        )

        state.successMessage?.let {
            Text(
                text = it,
                color = Color(0xFF4CAF50),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = { onEvent(GastoEvent.SaveGasto) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                ),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (state.gastoId == null) "Guardar" else "Actualizar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}