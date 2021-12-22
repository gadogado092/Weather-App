package com.example.jdsweather.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jdsweather.BuildConfig
import com.example.jdsweather.R
import com.example.jdsweather.data.entity.UserEntity
import com.example.jdsweather.data.entity.WeatherEntity
import com.example.jdsweather.databinding.ActivityDetailBinding
import com.example.jdsweather.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.lang.Exception
import java.util.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val TAG = "detail"
    }

    private var _binding: ActivityDetailBinding? = null
    private val _activityBinding get() = _binding
    private var _userEntity = UserEntity()
    private lateinit var _dateTimeNow: String
    private lateinit var _detailViewModel: DetailViewModel
    private lateinit var _forecastWeatherViewModel: ForecastWeatherViewModel
    private var _listWeatherDay1Entity: MutableList<WeatherEntity> = mutableListOf()
    private lateinit var _weatherDay1Adapter: WeatherAdapter
    private var _listWeatherDay2Entity: MutableList<WeatherEntity> = mutableListOf()
    private lateinit var _weatherDay2Adapter: WeatherAdapter
    private var _listWeatherDay3Entity: MutableList<WeatherEntity> = mutableListOf()
    private lateinit var _weatherDay3Adapter: WeatherAdapter
    private var _listWeatherDay4Entity: MutableList<WeatherEntity> = mutableListOf()
    private lateinit var _weatherDay4Adapter: WeatherAdapter
    private var _listWeatherDay5Entity: MutableList<WeatherEntity> = mutableListOf()
    private lateinit var _weatherDay5Adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(_activityBinding?.root)

        val factory = ViewModelFactory.getInstance()
        _detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        _forecastWeatherViewModel =
            ViewModelProvider(this, factory)[ForecastWeatherViewModel::class.java]

        val bundle = intent.getBundleExtra(EXTRA_DETAIL)
        if (bundle != null) {
            _userEntity = bundle.getParcelable(EXTRA_DETAIL)!!
        }

        _binding?.swipeRefreshLayout?.setOnRefreshListener {
            _detailViewModel.getData()
        }

        initWeatherAdapter()
        initData()

    }

    override fun onResume() {
        super.onResume()
        _detailViewModel.getData()
    }

    private fun initWeatherAdapter() {
        _weatherDay1Adapter = WeatherAdapter()
        with(_binding?.recyclerViewDay1) {
            this?.layoutManager = LinearLayoutManager(
                this?.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this?.setHasFixedSize(true)
            this?.adapter = _weatherDay1Adapter
        }

        _weatherDay2Adapter = WeatherAdapter()
        with(_binding?.recyclerViewDay2) {
            this?.layoutManager = LinearLayoutManager(
                this?.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this?.setHasFixedSize(true)
            this?.adapter = _weatherDay2Adapter
        }

        _weatherDay3Adapter = WeatherAdapter()
        with(_binding?.recyclerViewDay3) {
            this?.layoutManager = LinearLayoutManager(
                this?.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this?.setHasFixedSize(true)
            this?.adapter = _weatherDay3Adapter
        }

        _weatherDay4Adapter = WeatherAdapter()
        with(_binding?.recyclerViewDay4) {
            this?.layoutManager = LinearLayoutManager(
                this?.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this?.setHasFixedSize(true)
            this?.adapter = _weatherDay4Adapter
        }

        _weatherDay5Adapter = WeatherAdapter()
        with(_binding?.recyclerViewDay5) {
            this?.layoutManager = LinearLayoutManager(
                this?.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this?.setHasFixedSize(true)
            this?.adapter = _weatherDay5Adapter
        }
    }

    private fun initData() {

        _dateTimeNow = generateDateTimeNow()
        _binding?.textViewName?.text = _userEntity.fullName
        _binding?.textViewCity?.text = _userEntity.cityName
        _binding?.textViewDate?.text = dateToDisplay(_dateTimeNow)
        _binding?.textViewDay1?.text = dateToDisplayShort(generateDateTimeNow(1))
        _binding?.textViewDay2?.text = dateToDisplayShort(generateDateTimeNow(2))
        _binding?.textViewDay3?.text = dateToDisplayShort(generateDateTimeNow(3))
        _binding?.textViewDay4?.text = dateToDisplayShort(generateDateTimeNow(4))
        _binding?.textViewDay5?.text = dateToDisplayShort(generateDateTimeNow(5))
        _binding?.textViewGreeting?.text = "${dateToGreeting(_dateTimeNow)}, "

        _detailViewModel.setCity(_userEntity.cityName)
        _forecastWeatherViewModel.setCity(_userEntity.cityName)

        _detailViewModel.responseModel.observe(this, { response ->
            if (response.status) {
                try {
                    _binding?.textViewCurrentTemp?.text = response.main?.temp.toString()
                    _binding?.textViewDescriptionCurrentWeather?.text =
                        response.weather[0].description

                    _binding?.imageViewIconWeather?.let {
                        Glide.with(applicationContext)
                            .load(
                                BuildConfig.BASE_URL_ICON + response.weather[0].icon + "@2x.png"
                            )
                            .centerCrop()
                            .into(it)
                    }
                    _forecastWeatherViewModel.getData()
                } catch (e: Exception) {
                    _binding?.layoutContent?.visibility = View.GONE
                    showSnackBar(getString(R.string.err_msg_failed_load_data))
                }
            } else {
                _binding?.layoutContent?.visibility = View.GONE
                showSnackBar(response.message)
            }
        })

        _detailViewModel.isLoading.observe(this, {
            if (it) {
                _binding?.layoutContent?.visibility = View.GONE
            }
        })

        _forecastWeatherViewModel.isLoading.observe(this, {
            _binding?.swipeRefreshLayout?.isRefreshing = it
            _binding?.layoutContent?.visibility = if (it) View.GONE else View.VISIBLE
        })

        //Forecast Weather
        _forecastWeatherViewModel.responseModel.observe(this, { response ->
            if (response.status) {
                try {
                    _listWeatherDay1Entity.clear()
                    _listWeatherDay2Entity.clear()
                    _listWeatherDay3Entity.clear()
                    _listWeatherDay4Entity.clear()
                    _listWeatherDay5Entity.clear()
                    response.list.forEach {
                        val date = it.dtTxt?.split(" ")?.get(0) ?: ""
                        val time = it.dtTxt?.split(" ")?.get(1) ?: ""
                        if (date == generateDate(1)) {
                            _listWeatherDay1Entity.add(
                                WeatherEntity(
                                    time = time,
                                    description = it.weather[0].description,
                                    icon = it.weather[0].icon,
                                    temp = it.main?.temp.toString()
                                )
                            )
                        }
                        _weatherDay1Adapter.setData(_listWeatherDay1Entity)

                        if (date == generateDate(2)) {
                            _listWeatherDay2Entity.add(
                                WeatherEntity(
                                    time = time,
                                    description = it.weather[0].description,
                                    icon = it.weather[0].icon,
                                    temp = it.main?.temp.toString()
                                )
                            )
                        }
                        _weatherDay2Adapter.setData(_listWeatherDay2Entity)

                        if (date == generateDate(3)) {
                            _listWeatherDay3Entity.add(
                                WeatherEntity(
                                    time = time,
                                    description = it.weather[0].description,
                                    icon = it.weather[0].icon,
                                    temp = it.main?.temp.toString()
                                )
                            )
                        }
                        _weatherDay3Adapter.setData(_listWeatherDay3Entity)

                        if (date == generateDate(4)) {
                            _listWeatherDay4Entity.add(
                                WeatherEntity(
                                    time = time,
                                    description = it.weather[0].description,
                                    icon = it.weather[0].icon,
                                    temp = it.main?.temp.toString()
                                )
                            )
                        }
                        _weatherDay4Adapter.setData(_listWeatherDay4Entity)

                        if (date == generateDate(5)) {
                            _listWeatherDay5Entity.add(
                                WeatherEntity(
                                    time = time,
                                    description = it.weather[0].description,
                                    icon = it.weather[0].icon,
                                    temp = it.main?.temp.toString()
                                )
                            )
                        }
                        _weatherDay5Adapter.setData(_listWeatherDay5Entity)
                    }
                } catch (e: Exception) {
                    _binding?.layoutContent?.visibility = View.GONE
                    showSnackBar(getString(R.string.err_msg_failed_load_data))
                }
            } else {
                _binding?.layoutContent?.visibility = View.GONE
                showSnackBar(response.message)
            }
        })
    }

    private fun showSnackBar(message: String) {
        _binding?.let {
            Snackbar.make(
                it.root,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}