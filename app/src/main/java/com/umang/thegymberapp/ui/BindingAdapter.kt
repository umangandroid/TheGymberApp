package com.umang.thegymberapp.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umang.thegymberapp.data.db.GymDb
import com.umang.thegymberapp.ui.adapters.GymsAdapter
import com.umang.thegymberapp.ui.adapters.GymsMatchesListAdapter
import com.umang.thegymberapp.utils.loadImage

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<GymDb>) {

        when (listView.adapter) {
            is GymsAdapter ->
                (listView.adapter as GymsAdapter).submitList(items)
            else -> {
                (listView.adapter as GymsMatchesListAdapter).submitList(items)
            }

        }


}

@BindingAdapter("app:image")
fun setImage(imageView: ImageView, url: String) {
    imageView.loadImage(url)

}