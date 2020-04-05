package com.MobileComputing.BeerTracker

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SettingsView : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.view_settings, container, false)

    companion object {
        fun newInstance(): SettingsView = SettingsView()
    }
}