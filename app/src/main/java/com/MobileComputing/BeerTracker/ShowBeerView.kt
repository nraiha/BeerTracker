package com.MobileComputing.BeerTracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.view_show_beer.*
import kotlinx.android.synthetic.main.view_show_beer.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ShowBeerView : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(
        R.layout.view_show_beer, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list.setOnItemClickListener { parent, view, position, id ->
            context!!.toast("Selected item")
        }

        view!!.btn_delete.setOnClickListener {
            context!!.toast("Delete item")
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun getBeers() : Array<BeerItem>
    {
        val db = Room.databaseBuilder(activity!!.applicationContext, AppDatabase::class.java, "beers").build()
        val beers = db.beerDao().getBeers()
        db.close()
        return beers
    }

    private fun refreshList() {
        doAsync {
            val beers = getBeers()
            uiThread {
                if (beers.isNotEmpty())
                {
                    val beersAdapter = BeersAdapter(activity!!.applicationContext, beers)
                    list.adapter = beersAdapter
                }
            }
        }
    }

    companion object {
        fun newInstance(): ShowBeerView = ShowBeerView()
    }
}