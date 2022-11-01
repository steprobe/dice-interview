package com.steprobe.diceinterview

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textFlow(): Flow<String> = callbackFlow {
    val watcher = doAfterTextChanged { trySend(it.toString()) }
    awaitClose { removeTextChangedListener(watcher) }
}
