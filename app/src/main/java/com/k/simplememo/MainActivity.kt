package com.k.simplememo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.k.simplememo.data.local.Note
import com.k.simplememo.ui.NoteViewModel
import com.k.simplememo.ui.NoteViewModel.NoteUiState
import com.k.simplememo.ui.theme.SimpleMemoAppTheme
import java.text.DateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as SimpleMemoApplication
        setContent {
            SimpleMemoAppTheme {
                val viewModel: NoteViewModel = viewModel(
                    factory = NoteViewModel.provideFactory(app.repository)
                )
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SimpleMemoScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun SimpleMemoScreen(viewModel: NoteViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SimpleMemoContent(
        uiState = uiState,
        onContentChange = viewModel::onContentChange,
        onSave = viewModel::saveNote,
        onEdit = viewModel::onEditNote,
        onDelete = viewModel::deleteNote,
        onDismissDialog = viewModel::dismissDialog,
        onDialogContentChange = viewModel::onEditingContentChange,
        onUpdateNote = viewModel::updateNote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMemoContent(
    uiState: NoteUiState,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onEdit: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onDismissDialog: () -> Unit,
    onDialogContentChange: (String) -> Unit,
    onUpdateNote: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.app_title),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.noteContent,
            onValueChange = onContentChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            label = { Text(text = stringResource(id = R.string.note_hint)) },
            singleLine = false
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onSave, enabled = uiState.noteContent.isNotBlank()) {
                Text(text = stringResource(id = R.string.save))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (uiState.notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.empty_state),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(uiState.notes, key = { it.id }) { note ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.StartToEnd ||
                                value == SwipeToDismissBoxValue.EndToStart
                            ) {
                                onDelete(note)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            DismissBackground()
                        }
                    ) {
                        NoteItem(
                            note = note,
                            onClick = { onEdit(note) }
                        )
                    }
                }
            }
        }
    }

    if (uiState.showEditDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            title = { Text(text = stringResource(id = R.string.edit_note)) },
            text = {
                OutlinedTextField(
                    value = uiState.editingContent,
                    onValueChange = onDialogContentChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.edit_hint)) },
                    singleLine = false
                )
            },
            confirmButton = {
                TextButton(onClick = onUpdateNote, enabled = uiState.editingContent.isNotBlank()) {
                    Text(text = stringResource(id = R.string.update))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDialog) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun DismissBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = stringResource(id = R.string.delete),
            modifier = Modifier.padding(end = 24.dp),
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    val formattedDate = remember(note.createdAt) {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            .format(Date(note.createdAt))
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.last_updated, formattedDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleMemoPreview() {
    SimpleMemoAppTheme {
        SimpleMemoContent(
            uiState = NoteUiState(
                noteContent = "",
                notes = listOf(
                    Note(id = 1, content = "買い物リスト: 牛乳、パン、卵", createdAt = System.currentTimeMillis()),
                    Note(id = 2, content = "明日の打ち合わせ 10:00", createdAt = System.currentTimeMillis())
                )
            ),
            onContentChange = {},
            onSave = {},
            onEdit = {},
            onDelete = {},
            onDismissDialog = {},
            onDialogContentChange = {},
            onUpdateNote = {}
        )
    }
}
