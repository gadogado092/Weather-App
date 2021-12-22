package com.example.jdsweather.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jdsweather.BuildConfig
import com.example.jdsweather.data.entity.WeatherEntity
import com.example.jdsweather.databinding.ItemWeatherBinding
import com.example.jdsweather.utils.parseTimeToDisplay

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private var listData = ArrayList<WeatherEntity>()

    fun setData(listData: List<WeatherEntity>?) {
        if (listData == null) return
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: WeatherEntity) {
            with(binding) {
                textViewTime.text = entity.time?.let { parseTimeToDisplay(it) }
                textViewDescription.text = entity.description
                textViewTemp.text = entity.temp

                Glide.with(itemView.context)
                    .load(
                        BuildConfig.BASE_URL_ICON + entity.icon + "@2x.png"
                    )
                    .centerCrop()
                    .into(imageViewIconWeather)
            }
        }
    }

}