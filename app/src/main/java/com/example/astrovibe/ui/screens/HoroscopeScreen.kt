package com.example.astrovibe.ui.screens

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.astrovibe.data.models.ZodiacInfo
import com.example.astrovibe.data.models.ZodiacItem
import com.example.astrovibe.ui.HoroscopeViewModel
import com.example.astrovibe.ui.components.DOBPicker
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HoroscopeScreen(viewModel: HoroscopeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.selectedZodiac) {
        val index = uiState.zodiacList.indexOfFirst { it.name == uiState.selectedZodiac }
        if (index >= 0) {
            scope.launch {
                listState.animateScrollToItem(index)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ZodiacSelector(
            zodiacList = uiState.zodiacList,
            selectedZodiac = uiState.selectedZodiac,
            onZodiacSelected = viewModel::onZodiacSelected,
            listState = listState
        )

        Spacer(modifier = Modifier.height(16.dp))

        DOBPicker(
            selectedDate = uiState.selectedDOB,
            onDateSelected = viewModel::onDOBSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        uiState.zodiacDetails?.let {
            ZodiacDetails(info = it)
        }
    }
}

@Composable
fun ZodiacSelector(
    zodiacList: List<ZodiacItem>,
    selectedZodiac: String?,
    onZodiacSelected: (String) -> Unit,
    listState: LazyListState
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        itemsIndexed(zodiacList) { index, zodiac ->
            val isSelected = zodiac.name == selectedZodiac
            val bgColor by animateColorAsState(
                if (isSelected) Color(0xFFBB86FC) else Color(0xFFF2F2F2),
                label = "bgColor"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .clickable { onZodiacSelected(zodiac.name) }
                    .padding(12.dp)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = zodiac.imageRes),
                    contentDescription = zodiac.name,
                    modifier = Modifier
                        .size(48.dp)
                )
                Text(
                    text = zodiac.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}


@Composable
fun ZodiacDetails(info: ZodiacInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFDAE2F8), Color(0xFFD6A4A4))
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = info.imageRes),
                    contentDescription = info.name,
                    modifier = Modifier
                        .size(96.dp)
                        .padding(bottom = 12.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = info.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A148C)
                )
                Text(
                    text = info.description,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.DarkGray
                )
            }
        }
    }
}
