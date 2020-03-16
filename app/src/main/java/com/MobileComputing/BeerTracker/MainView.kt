package com.MobileComputing.BeerTracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.view_main.*
import kotlinx.android.synthetic.main.view_main.view.*

class MainView : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.view_main, container, false)
        view.btn_addBeer.setOnClickListener { view ->
            val intent = Intent(getActivity(), AddBeerActivity::class.java)
            startActivity(intent)
        }
        return view
    }
        //    : View? = inflater.inflate(R.layout.view_main, container, false)



   /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_main)
        btn_addBeer.setOnClickListener {
            val intent =

        }
    }
    */

    companion object {
        fun newInstance(): MainView = MainView()
    }
}