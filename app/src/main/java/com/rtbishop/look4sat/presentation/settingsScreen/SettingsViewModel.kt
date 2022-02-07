package com.rtbishop.look4sat.presentation.settingsScreen

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtbishop.look4sat.data.ISettingsHandler
import com.rtbishop.look4sat.domain.IDataRepository
import com.rtbishop.look4sat.domain.ILocationHandler
import com.rtbishop.look4sat.domain.model.DataState
import com.rtbishop.look4sat.domain.predict.GeoPos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val resolver: ContentResolver,
    private val settings: ISettingsHandler,
    private val repository: IDataRepository,
    private val locationHandler: ILocationHandler
) : ViewModel() {

    fun updateDataFromFile(uri: Uri) {
        viewModelScope.launch {
            @Suppress("BlockingMethodInNonBlockingContext")
            resolver.openInputStream(uri)?.use { fileUri ->
                repository.updateDataFromFile(fileUri)
            }
        }
    }

    fun updateDataFromWeb(sources: List<String>) {
        repository.updateDataFromWeb(sources)
    }

    fun getUseUTC(): Boolean = settings.getUseUTC()

    fun setUseUTC(value: Boolean) = settings.setUseUTC(value)

    fun getUseCompass(): Boolean = settings.getUseCompass()

    fun setUseCompass(value: Boolean) = settings.setUseCompass(value)

    fun getShowSweep(): Boolean = settings.getShowSweep()

    fun setShowSweep(value: Boolean) = settings.setShowSweep(value)

    fun getRotatorEnabled(): Boolean = settings.getRotatorEnabled()

    fun setRotatorEnabled(value: Boolean) = settings.setRotatorEnabled(value)

    fun getRotatorServer(): String = settings.getRotatorServer()

    fun setRotatorServer(value: String) = settings.setRotatorServer(value)

    fun getRotatorPort(): String = settings.getRotatorPort()

    fun setRotatorPort(value: String) = settings.setRotatorPort(value)

    fun getUpdateState() = repository.dataUpdateState

    fun setUpdateHandled() = repository.setDataUpdateHandled()

    val stationPosition: SharedFlow<DataState<GeoPos>> = locationHandler.stationPosition

    fun getStationPosition(): GeoPos = locationHandler.getStationPosition()

    fun setPositionFromGps() = locationHandler.setPositionFromGps()

    fun setPositionFromNet() = locationHandler.setPositionFromNet()

    fun setPositionFromQth(qthString: String) = locationHandler.setPositionFromQth(qthString)

    fun setPositionHandled() = locationHandler.setPositionHandled()
}
