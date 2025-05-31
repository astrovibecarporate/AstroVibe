import android.R
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClockTimePickerWithButton(
    initialTime: LocalTime = LocalTime.MIDNIGHT,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedTimeText by remember { mutableStateOf(initialTime.toString()) }

    if (showDialog) {
        TimePickerDialog(
            context,
            android.R.style.Theme_Material_Light_Dialog_Alert,
            { _, hourOfDay, minute ->
                val selectedTime = LocalTime.of(hourOfDay, minute)
                selectedTimeText = selectedTime.toString()
                onTimeSelected(selectedTime)
                showDialog = false
            },
            initialTime.hour,
            initialTime.minute,
            true
        ).apply {
            setOnCancelListener { showDialog = false }
            show()
        }
    }

    Button(onClick = { showDialog = true }) {
        Text(selectedTimeText)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClockTimePickerWithButton2(
    initialTime: LocalTime = LocalTime.MIDNIGHT,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(initialTime) }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val timeText = selectedTime.format(timeFormatter)

    if (showDialog) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedTime = LocalTime.of(hour, minute)
                onTimeSelected(selectedTime)
                showDialog = false
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        ).apply {
            setOnCancelListener { showDialog = false }
            show()
        }
    }

    OutlinedButton(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Select Time",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = timeText, style = MaterialTheme.typography.bodyLarge)
    }
}

