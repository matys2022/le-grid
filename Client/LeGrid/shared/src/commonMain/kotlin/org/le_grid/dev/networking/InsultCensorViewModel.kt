package org.le_grid.dev.networking

import androidx.lifecycle.ViewModel
import org.le_grid.dev.util.NetworkError
import org.le_grid.dev.util.Result

class InsultCensorViewModel(
    val censorClient: InsultCensorClient
) : ViewModel() {
    suspend fun censorWords(uncensoredString : String) : Result<String, NetworkError>{
        return censorClient.censorWords(uncensoredString);
    }
}