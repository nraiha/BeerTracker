package com.MobileComputing.BeerTracker

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_view_item.view.*

class BeersAdapter(context: Context, private val list: List<BeerItem>)
    : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val row = inflater.inflate(R.layout.list_view_item, parent, false)

        row.beer_info.text = list[position].beer_name
        row.bottle_size.text = list[position].bottle_size.toString()
        row.percentage_info.text =list[position].percentage.toString()

        return row
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return list.size
    }


}
