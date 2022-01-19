package com.umang.thegymberapp

import com.google.common.truth.Truth
import com.umang.thegymberapp.data.GymRepositoryImpl
import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.fakedata.FakeLocalDataSource
import com.umang.thegymberapp.fakedata.FakeRemoteSource
import com.umang.thegymberapp.fakedata.FakeRepository
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 *  Unit test implementation for GymRepositoryImpl
 */
@ExperimentalCoroutinesApi
class GymsRepositoryImplTest {
    private lateinit var gymRepositoryImpl: GymRepositoryImpl
    private val fakeRepository = FakeRepository()

    @Before
    fun setup() {
        gymRepositoryImpl = GymRepositoryImpl(
            FakeRemoteSource(), FakeLocalDataSource(),
        )
    }

    @Test
    fun getGymsDataFlowTest() = runBlocking {
        val resultFlow = gymRepositoryImpl.getGyms(true)
        val resultValue = resultFlow.first()
        assert(resultValue is DataResult.Success)
        val list = (resultValue as DataResult.Success).data
        Truth.assertThat(list.size).isEqualTo(3)
        Truth.assertThat(list.find { it.name == "A ZUMBODY"} !=null).isEqualTo(true)
        Truth.assertThat(list.find { it.address == "Teun de Jagerdreef Utrecht 3561 JK"} !=null).isEqualTo(true)
    }

    @Test
    fun getMatchedGymTest() = runBlocking{
        gymRepositoryImpl.matchOrRejectGym(fakeRepository.getFormattedResponse()[0],Direction.Right)
        val resultFlow = gymRepositoryImpl.getMatchedGyms()
        val resultValue = resultFlow.first()
        Truth.assertThat(resultValue.size).isEqualTo(1)
        Truth.assertThat(resultValue.find { it.name == "A ZUMBODY"} !=null).isEqualTo(true)
        Truth.assertThat(resultValue.find { it.address == "Teun de Jagerdreef Utrecht 3561 JK"} !=null).isEqualTo(true)

    }



}