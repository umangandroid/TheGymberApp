package com.umang.thegymberapp.data

import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.data.models.GymsResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: APIService) :RemoteDataSource() {
    override suspend fun getGyms(): DataResult<GymsResponse> {
        return getResult {  apiService.getGyms()}
    }

    /**
     * To parse api result in DataResult for further processing
     *
     * @param T : return type
     * @param call : api call
     * @return : DataResult (Success or Error)
     */
    private suspend fun <T> getResult(call: suspend () -> Response<T>): DataResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    DataResult.Success(body)
                } else {
                    DataResult.Error(Exception("Error Occurred"))
                }
            } else {
                val errorMsg = JSONObject(response.errorBody()?.string())
                DataResult.Error(Exception(errorMsg.getString("message")))
            }

        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}
