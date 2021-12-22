package com.example.jdsweather.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jdsweather.R
import com.example.jdsweather.data.entity.LocationEntity
import com.example.jdsweather.data.entity.UserEntity
import com.example.jdsweather.databinding.ItemLocationBinding

class LocationAdapter(private val listener: (LocationEntity) -> Unit) :
    RecyclerView.Adapter<LocationAdapter.WilayahViewHolder>() {
    private var listData = ArrayList<LocationEntity>()
    private var typeSelected = "province"
    private var userEntity = UserEntity()

    fun setData(listData: List<LocationEntity>?, userEntity: UserEntity, type: String) {
        if (listData == null) return
        this.listData.clear()
        this.listData.addAll(listData)
        this.userEntity = userEntity
        this.typeSelected = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WilayahViewHolder {
        val itemBinding =
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WilayahViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: WilayahViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data, listener)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class WilayahViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: LocationEntity, listener: (LocationEntity) -> Unit) {
            with(binding) {
                textViewName.text = entity.name

                when (typeSelected) {
                    "province" -> {
                        if (userEntity.provinceKode == entity.kode) {
                            textViewName.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.teal_200
                                )
                            )
                        } else {
                            textViewName.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.black
                                )
                            )
                        }
                    }
                    "city" -> {
                        if (userEntity.cityKode == entity.kode) {
                            textViewName.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.teal_200
                                )
                            )
                        } else {
                            textViewName.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.black
                                )
                            )
                        }
                    }
                }
            }

            itemView.setOnClickListener {
                listener(entity)
            }

        }
    }

}