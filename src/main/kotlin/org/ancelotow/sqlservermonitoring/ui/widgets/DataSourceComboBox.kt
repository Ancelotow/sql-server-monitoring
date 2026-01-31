package org.ancelotow.sqlservermonitoring.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.database.dataSource.LocalDataSource
import org.jetbrains.jewel.ui.component.CircularProgressIndicator
import org.jetbrains.jewel.ui.component.ListComboBox


@Composable
fun DataSourceComboBox(
    modifier: Modifier = Modifier,
    items: List<LocalDataSource>,
    selected: LocalDataSource?,
    isLoading: Boolean,
    onItemSelected: (LocalDataSource) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.uniqueId == selected?.uniqueId }
    var internalSelectedIndex by remember(items, selected?.uniqueId) {
        mutableStateOf(selectedIndex)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ListComboBox(
            modifier = Modifier.weight(1f),
            enabled = !isLoading && items.isNotEmpty(),
            items = items.map { it.name },
            selectedIndex = internalSelectedIndex,
            onSelectedItemChange = { onItemSelected(items[it]) }
        )

        Spacer(Modifier.width(8.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp)
            )
        }
    }
}