package com.example.lab8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val data: MutableList<Contact>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder class to hold and bind view elements
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        private val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)

        fun bind(item: Contact) {
            tvName.text = item.name
            tvPhone.text = item.phone
            imgDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    data.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    // Inflate the item layout and create the holder
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_row, viewGroup, false)
        return ViewHolder(v)
    }

    // Return the size of your dataset
    override fun getItemCount() = data.size

    // Bind data to the holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

