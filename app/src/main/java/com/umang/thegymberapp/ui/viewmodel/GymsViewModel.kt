package com.umang.thegymberapp.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umang.thegymberapp.R
import com.umang.thegymberapp.data.DataResourceProvider
import com.umang.thegymberapp.data.GymsRepository
import com.umang.thegymberapp.data.db.GymDb
import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.utils.GRANTED
import com.umang.thegymberapp.utils.getDistanceBetweenTwoCoordinates
import com.yuyakaido.android.cardstackview.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * To manage operations for GymsFragment
 *
 * @property gymsRepository : repository
 * @property dataResourceProvider : to access string resources
 */
@HiltViewModel
class GymsViewModel @Inject constructor(
    private val gymsRepository: GymsRepository,
    private val dataResourceProvider: DataResourceProvider
) : ViewModel() {

    var intermediateResults: List<GymDb>? = null
    var progressStatus = MutableStateFlow(false)

    var gymResults = MutableStateFlow<List<GymDb>>(listOf())

    // to show common error as toast
    private val _commonError = Channel<String>()
    val commonError = _commonError.receiveAsFlow()

    //to initiate location access once data fetched from repository
    private val _requestLocationPermission = Channel<Boolean>()
    val requestLocationPermission = _requestLocationPermission.receiveAsFlow()

    /**
     * Get gyms list from repository class
     *
     * @param isRefresh :true for refresh call
     */
    fun getGyms(isRefresh: Boolean) {
        progressStatus.value = true
        gymResults.value = listOf()
        viewModelScope.launch(Dispatchers.IO) {
            gymsRepository.getGyms(isRefresh).catch {
                progressStatus.value = false
                _commonError.send(it.localizedMessage)

            }.collect {
                if (it is DataResult.Success) {
                    intermediateResults = it.data
                    progressStatus.value = false
                    _requestLocationPermission.send(true)
                } else {
                    progressStatus.value = false
                    val error = it as DataResult.Error
                    if (error.exception is UnknownHostException) {
                        _commonError.send(dataResourceProvider.getString(R.string.unable_to_connect))
                    } else {
                        _commonError.send(error.exception.localizedMessage)
                    }

                }
            }
        }
    }

    /**
     * Once user allows location permission using location of user calculate distance between gym and user
     * If no permission granted then distance wont be calculated
     *
     * @param permissionResult : location permission result
     * @param location : user location
     */
    fun onRequestPermissionResult(permissionResult: Int, location: Location? = null) {
        if (permissionResult == GRANTED) {
            location?.let { location ->
                intermediateResults?.forEach { gym ->
                    gym.distance =
                        "${
                            getDistanceBetweenTwoCoordinates(
                                location,
                                gym.latitude,
                                gym.longitude
                            )
                        } ${dataResourceProvider.getString(R.string.miles)}"
                }
            }

        }
        gymResults.value = intermediateResults ?: listOf()
    }

    /**
     * Based on swipe operation update match status of gym
     *
     * @param position : gym position in list
     * @param direction :swipe direction
     */
    fun matchOrRejectGym(position: Int, direction: Direction?) {
        viewModelScope.launch(Dispatchers.Default) {
            gymsRepository.matchOrRejectGym(intermediateResults?.get(position), direction)
        }
    }
}