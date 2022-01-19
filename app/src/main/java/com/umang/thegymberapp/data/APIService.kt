package com.umang.thegymberapp.data

import com.umang.thegymberapp.data.models.GymsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers


/**
 * Contains all api calls
 *
 */
interface APIService {

    @Headers("User-Agent: OneFit/1.19.0")
    @GET("partners/city/UTR")
    suspend fun getGyms(): Response<GymsResponse>



}