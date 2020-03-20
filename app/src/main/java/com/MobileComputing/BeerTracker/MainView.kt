package com.MobileComputing.BeerTracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.view_main.view.*

class MainView : Fragment(), IfOnBackButtonPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        /* Check if weight and sex is added. Print text if not */

        val view: View = inflater.inflate(R.layout.view_main, container, false)

        view.btn_addBeer.setOnClickListener {
            val intent = Intent(activity, AddBeerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Return the fragment view/layout
        return view
    }

    companion object {
        fun newInstance(): MainView = MainView()
    }

    override fun onBackPressed(): Boolean {
        val dialogBuilder = context?.let { AlertDialog.Builder(it) }

        //val dialog: AlertDialog = dialogBuilder.create()
        return true
    }
}