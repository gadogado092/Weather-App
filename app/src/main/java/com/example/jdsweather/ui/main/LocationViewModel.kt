package com.example.jdsweather.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jdsweather.BuildConfig
import com.example.jdsweather.data.response.ResponseLocationModel
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header

class LocationViewModel : ViewModel() {
    companion object {
        const val TAG = "LocationViewModel"
    }

    private lateinit var id: String

    fun setId(id: String) {
        this.id = id
    }

    private val _responseModel = MutableLiveData<ResponseLocationModel>()
    val responseModel: LiveData<ResponseLocationModel> = _responseModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getData() {
        val params = RequestParams()
        params.put("id", id)

        _isLoading.value = true
        val client = AsyncHttpClient()
        val url = BuildConfig.BASE_URL + "wilayah"
        Log.d(TAG, "url $url id $id")
        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                val gson = Gson()
                val response = gson.fromJson(result, ResponseLocationModel::class.java)
                _responseModel.postValue(response)
                _isLoading.value = false
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoading.value = false
                Log.e(TAG, "error load Info")
                if (statusCode == 0) {
                    val response =
                        ResponseLocationModel(false, "Koneksi anda bermasalah\nCoba lagi")
                    _responseModel.postValue(response)
                } else {
                    val response = ResponseLocationModel(false, "Maaf koneksi bermasalah kode 100")
                    _responseModel.postValue(response)
                }
            }
        })
    }
}