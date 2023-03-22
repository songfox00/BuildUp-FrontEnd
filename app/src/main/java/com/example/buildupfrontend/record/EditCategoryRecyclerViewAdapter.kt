package com.example.buildupfrontend.record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class EditCategoryRecyclerViewAdapter(
    private val context: Context,
    private var dataList: ArrayList<RecordRecyclerViewData>
): RecyclerView.Adapter<EditCategoryRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView =itemView.findViewById(R.id.image_category)
        var category: TextView =itemView.findViewById(R.id.tv_category)

        fun bind(data: RecordRecyclerViewData){
            image.setImageResource(data.image)
            category.text=data.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_category,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}