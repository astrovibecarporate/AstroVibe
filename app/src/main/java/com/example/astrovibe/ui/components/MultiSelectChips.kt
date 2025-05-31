package com.example.astrovibe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiSelectChips(
    options: List<String>,
    selected: Set<String>,
    onSelectionChange: (Set<String>) -> Unit
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = selected.contains(option)
            FilterChip(
                selected = isSelected,
                onClick = {
                    val newSet = selected.toMutableSet()
                    if (isSelected) newSet.remove(option) else newSet.add(option)
                    onSelectionChange(newSet)
                },
                label = { Text(option) }
            )
        }
    }
}
