package org.le_grid.dev.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

//class AppDataStore(
//
//) {
    fun createDataStore(context: Context): DataStore<Preferences> {
        return createDataStore { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
    }

//}