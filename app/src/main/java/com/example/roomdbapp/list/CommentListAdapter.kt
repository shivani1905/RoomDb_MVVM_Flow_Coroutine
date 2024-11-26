package com.example.roomdbapp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdbapp.R
import com.example.roomdbapp.data.models.Comment

class CommentListAdapter(val mArrayList: List<Comment>, val onItemClickListener: (Comment) -> Unit, val onItemLongClickListener: (Comment) -> Unit) :
    RecyclerView.Adapter<CommentListAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_comment, parent, false)
        return NameViewHolder(view)
    }

    override fun getItemCount() = mArrayList.size

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.bind(mArrayList[position])
    }

    inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTitle: TextView = itemView.findViewById(R.id.txtTitle)
        var mDesc: TextView = itemView.findViewById(R.id.txtDesc)
        var mDate: TextView = itemView.findViewById(R.id.txtDate)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                onItemClickListener.invoke(mArrayList[position])
            }
            itemView.setOnLongClickListener {
                val position = adapterPosition
                onItemLongClickListener.invoke(mArrayList[position])
                return@setOnLongClickListener true
            }
        }

        fun bind(item: Comment) {
            mTitle.text = item.body
            mDesc.text = item.user.fullName
            mDate.text = item.user.username
        }
    }
}