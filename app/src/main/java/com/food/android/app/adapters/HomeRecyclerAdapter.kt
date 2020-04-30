package com.food.android.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.food.android.app.R
import com.food.android.app.models.Item
import com.food.android.app.ui.activities.DetailsActivity
import com.food.android.app.utils.Utility
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class HomeRecyclerAdapter(val items: ArrayList<Item>, val context: Context?) : RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>(), Filterable {

    private var mData: List<Item> = items
    private var mFilteredList: List<Item> = items

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString().toLowerCase()

                when {
                    charString.isEmpty() -> mFilteredList = mData
                    else -> {
                        val filteredList = ArrayList<Item>()
                        for (item in mData) {
                            if (!item.name.isNullOrBlank()){
                                if (item.name!!.toLowerCase().contains(charString)) {
                                    filteredList.add(item)
                                }
                            }
                        }
                        mFilteredList = filteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults?) {
                mFilteredList = filterResults?.values as List<Item>
                notifyDataSetChanged()
            }
        }

    }

    var onClick: (view: View, position: Int) -> Unit = { _, _ -> }

    override fun getItemCount(): Int = mFilteredList.size

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position, onClick)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val itemImage = view.itemImage
        val parentLayout = view.parentLayout
        val titleTv = view.titleTv
        val descriptionTv = view.descriptionTv



        fun bindView(position: Int, onClick: (view: View, position: Int) -> Unit) {

            val data = mFilteredList.get(position)
           // animalImage.setImageResource(resId)
            itemView.setOnClickListener {
                onClick(itemView, position)
            }

            titleTv.setText(Utility.toTitleCase(data.name))
            descriptionTv.setText(data.description)
            Glide.with(descriptionTv.context).load(data.image).placeholder(R.drawable.ic_placeholder).into(itemImage)

            parentLayout.setOnClickListener {
               // parentLayout.context.startActivity(Intent(parentLayout.context, DetailsActivity::class.java).putExtra("data",da))

                val intent = Intent(parentLayout.context, DetailsActivity::class.java)
                intent.putExtra("data", data)

                parentLayout.context.startActivity(intent)

            }
        }
    }

}

