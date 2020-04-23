package com.MobileComputing.BeerTracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.view_show_beer.*

class ShowBeerView : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(
                    R.layout.view_show_beer, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data = arrayOf("halpa", "halvempi")

        // Better hope fragment is attached, otherwise this will cause memory leak / crash
        val beersAdapter = BeersAdapter(activity!!.applicationContext, data)
        list.adapter = beersAdapter
    }




    companion object {
        fun newInstance(): ShowBeerView = ShowBeerView()
    }
}