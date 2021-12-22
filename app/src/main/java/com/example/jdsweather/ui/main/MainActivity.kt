package com.example.jdsweather.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jdsweather.R
import com.example.jdsweather.data.entity.UserEntity
import com.example.jdsweather.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import androidx.lifecycle.ViewModelProvider
import com.example.jdsweather.data.entity.LocationEntity
import com.example.jdsweather.data.response.ListLocationResponse
import com.example.jdsweather.ui.detail.DetailActivity
import com.example.jdsweather.utils.ViewModelFactory
import com.example.jdsweather.utils.capitalizeSentence
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var _binding: ActivityMainBinding? = null
    private val _activityBinding get() = _binding
    private lateinit var _userEntity: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_activityBinding?.root)

        _userEntity = UserEntity(
            provinceKode = "32",
            provinceName = "Jawa Barat",
            cityKode = "32.73",
            cityName = "Bandung"
        )

        _activityBinding?.textViewLocation?.text =
            "${capitalizeSentence(_userEntity.cityName)}, ${capitalizeSentence(_userEntity.provinceName)}"

        _activityBinding?.layoutSelectLocation?.setOnClickListener {
            showBottomSheetLocation(_userEntity)
        }
        _activityBinding?.buttonProses?.setOnClickListener {
            proses()
        }

    }

    private fun proses() {
        hideKeyboard()
        val name = _binding?.editTextTextName
        if (name?.text.toString().trim().isEmpty()) {
            name?.error =
                getString(R.string.err_msg_name_empty)
            showSnackBar(getString(R.string.err_msg_name_empty))
            return
        }

        _userEntity.fullName = name?.text.toString().trim()
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(DetailActivity.EXTRA_DETAIL, _userEntity)
        intent.putExtra(DetailActivity.EXTRA_DETAIL, bundle)
        startActivity(intent)
    }

    private fun showBottomSheetLocation(userEntity: UserEntity) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_location_layout)
        val recyclerViewLocation =
            bottomSheetDialog.findViewById<RecyclerView>(R.id.recyclerViewLocation)
        val progressBar = bottomSheetDialog.findViewById<ProgressBar>(R.id.progressBar)
        val textViewProvince = bottomSheetDialog.findViewById<TextView>(R.id.textViewProvince)
        val textViewCity = bottomSheetDialog.findViewById<TextView>(R.id.textViewCity)
        val textViewTitleLocation =
            bottomSheetDialog.findViewById<TextView>(R.id.textViewTitleWilayah)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[LocationViewModel::class.java]

        var typeSelected = "province"
        val locationAdapter = LocationAdapter {
            if (typeSelected == "province") {
                typeSelected = "city"
                viewModel.setId(it.kode)
                viewModel.getData()
                userEntity.provinceKode = it.kode
                userEntity.provinceName = it.name
                textViewTitleLocation?.text = getString(R.string.city)

                with(textViewProvince) {
                    this?.text = userEntity.provinceName
                    this?.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.black
                        )
                    )
                }

                with(textViewCity) {
                    this?.text = getString(R.string.select_city)
                    this?.visibility = View.VISIBLE
                    this?.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.teal_200
                        )
                    )
                }
            } else if (typeSelected == "city") {
                //DISMISS DIALOG AFTER SELECT CITY
                userEntity.cityKode = it.kode
                userEntity.cityName = it.name
                textViewCity?.text = userEntity.cityName

                _activityBinding?.textViewLocation?.text =
                    "${capitalizeSentence(userEntity.cityName)}, ${capitalizeSentence(userEntity.provinceName)}"

                _userEntity = userEntity
                bottomSheetDialog.dismiss()
            }
        }
        with(recyclerViewLocation) {
            this?.layoutManager = LinearLayoutManager(this@MainActivity)
            this?.setHasFixedSize(true)
            this?.adapter = locationAdapter
        }

        val listWilayahEntity: MutableList<LocationEntity> = mutableListOf()

        viewModel.setId("")
        viewModel.getData()
        textViewTitleLocation?.text = getString(R.string.province)

        with(textViewProvince) {
            this?.text = userEntity.provinceName
            this?.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.teal_200
                )
            )
            this?.setOnClickListener {
                typeSelected = "province"
                viewModel.setId("")
                viewModel.getData()
                textViewTitleLocation?.text = getString(R.string.province)
                with(textViewProvince) {
                    this?.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.teal_200
                        )
                    )
                }
                with(textViewCity) {
                    this?.visibility = View.GONE
                }
            }
        }
        with(textViewCity) {
            this?.text = userEntity.cityName
            this?.visibility = View.VISIBLE
            this?.setOnClickListener {
                typeSelected = "city"
                textViewTitleLocation?.text = getString(R.string.city)
                viewModel.setId(userEntity.provinceKode)
                viewModel.getData()
                with(textViewCity) {
                    this?.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.teal_200
                        )
                    )
                }
                with(textViewProvince) {
                    this?.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.black
                        )
                    )
                }
            }
        }

        viewModel.responseModel.observe(this, { response ->
            val gson = Gson()
            if (response.status) {
                Log.d(TAG, "respon" + response.data.toString())
                if (recyclerViewLocation != null) {
                    recyclerViewLocation.visibility = View.VISIBLE
                }

                val listProvinceResponse =
                    gson.fromJson(response.data, ListLocationResponse::class.java)
                listWilayahEntity.clear()
                listProvinceResponse.data.forEach {
                    listWilayahEntity.add(
                        LocationEntity(
                            it.kode,
                            it.name.replace("KAB. ", "").replace("KOTA ", "")
                        )
                    )
                }
                locationAdapter.setData(listWilayahEntity, userEntity, typeSelected)
                locationAdapter.notifyDataSetChanged()

            } else {
                if (recyclerViewLocation != null) {
                    recyclerViewLocation.visibility = View.GONE
                }
                Toast.makeText(applicationContext, response.message, Toast.LENGTH_LONG)
                    .show()
            }
        })

        viewModel.isLoading.observe(this, {
            if (progressBar != null) {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
            if (recyclerViewLocation != null) {
                recyclerViewLocation.visibility = if (it) View.GONE else View.VISIBLE
            }
        })

        bottomSheetDialog.show()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
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