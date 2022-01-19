package com.umang.thegymberapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umang.thegymberapp.R
import com.umang.thegymberapp.data.DataResourceProvider
import com.umang.thegymberapp.data.GymsRepository
import com.umang.thegymberapp.data.db.GymDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * To get matched gyms from [GymsRepository]
 *
 * @property gymsRepository : repository
 * @property dataResourceProvider : to access string resources from strings.xml
 */
@HiltViewModel
class GymMatchesListViewModel @Inject constructor(
    private val gymsRepository: GymsRepository,
    private val dataResourceProvider: DataResourceProvider
) :
    ViewModel() {

    val gymMatchesList = MutableStateFlow<List<GymDb>>(listOf())
    val errorMessage = MutableStateFlow("")

    /**
     * Fetch matched gyms from db
     *
     */
    fun getGymMatches() {
        viewModelScope.launch(Dispatchers.Default) {
            gymsRepository.getMatchedGyms().catch {
                errorMessage.value = it.localizedMessage
            }.collect {
                gymMatchesList.value = it
                if (it.isNullOrEmpty())
                    errorMessage.value = dataResourceProvider.getString(R.string.no_matches_found)
            }
        }
    }
}