package com.umang.thegymberapp

import com.google.common.truth.Truth.assertThat
import com.umang.thegymberapp.data.DataResourceProvider
import com.umang.thegymberapp.fakedata.FakeRepository
import com.umang.thegymberapp.ui.viewmodel.GymsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

/**
 *
 *  Unit test implementation for GymsViewModel
 */

@ExperimentalCoroutinesApi
class GymsViewModelTest {
    private lateinit var viewModel: GymsViewModel

    @Before
    fun setup() {
        val dataResourceProvider =
            mock<DataResourceProvider>()

        viewModel = GymsViewModel(
            FakeRepository(),
            dataResourceProvider
        )
    }

    @Test
    fun getAirQualityDataFlowTest() = runBlocking {
        viewModel.getGyms(true)
        Assert.assertEquals(true, viewModel.progressStatus.value)
        delay(1000)
        val list = viewModel.intermediateResults
        assertThat(list?.size).isEqualTo(3)
        Assert.assertEquals(false, viewModel.progressStatus.value)

        assertThat(list?.get(0)?.name).isEqualTo(
            "A ZUMBODY"
        )
        assertThat(list?.get(0)?.address).isEqualTo(
            "Teun de Jagerdreef Utrecht 3561 JK"
        )
    }



}