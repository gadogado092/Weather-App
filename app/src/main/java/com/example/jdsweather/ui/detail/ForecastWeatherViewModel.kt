package com.example.jdsweather.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jdsweather.BuildConfig
import com.example.jdsweather.data.response.open_weather.ResponseOpenWeatherModel
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header

class ForecastWeatherViewModel : ViewModel() {
    companion object {
        const val TAG = "ForecastWeather"
    }

    private lateinit var city: String

    fun setCity(city: String) {
        this.city = city
    }

    private val _responseModel = MutableLiveData<ResponseOpenWeatherModel>()
    val responseModel: LiveData<ResponseOpenWeatherModel> = _responseModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getData() {
        val params = RequestParams()
        params.put("q", city)
        params.put("appid", BuildConfig.API_KEY_OPEN_WEATHER)
        params.put("units", "metric")
        params.put("lang", "id")

        _isLoading.value = true
        val client = AsyncHttpClient()
        val url = BuildConfig.BASE_URL_OPEN_WEATHER + "forecast"
        Log.d(TAG, "url $url params $params")
        client.get(url, params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                val gson = Gson()
                val response = gson.fromJson(result, ResponseOpenWeatherModel::class.java)
                response.status = true
                response.message = "success"
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
                when (statusCode) {
                    1 -> {
                        val response =
                            ResponseOpenWeatherModel(false, "Koneksi anda bermasalah\nCoba lagi")
                        _responseModel.postValue(response)
                    }
                    404 -> {
                        val response =
                            ResponseOpenWeatherModel(
                                false,
                                "Kota Tidak Ditemukan\nCoba lagi"
                            )
                        _responseModel.postValue(response)
                    }
                    else -> {
                        val response =
                            ResponseOpenWeatherModel(false, "Maaf koneksi bermasalah kode 100")
                        _responseModel.postValue(response)
                    }
                }
            }
        })
    }
}