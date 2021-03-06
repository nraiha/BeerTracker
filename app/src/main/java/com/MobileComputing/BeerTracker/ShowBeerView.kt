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

    private var mItemPosition : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(
        R.layout.view_show_beer, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list.setOnItemClickListener { _, _, position, _ ->
           mItemPosition = position
            activity!!.toast("Item selected")
        }

        view!!.btn_delete.setOnClickListener {
            doAsync {
                val db = Room.databaseBuilder(
                    activity!!.applicationContext,
                    AppDatabase::class.java,
                    "beers"
                ).build()
                val beers = db.beerDao().getBeers()
                db.close()
                if (beers.isNotEmpty()) {
                    beers[mItemPosition].uid?.let { it1 ->
                        db.beerDao().delete(it1)
                    }
                }
                uiThread {
                    val beersAdapter =
                        BeersAdapter(activity!!.applicationContext, beers)
                    list.adapter = beersAdapter
                    beersAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        doAsync {
            val db = Room.databaseBuilder(activity!!.applicationContext,
                AppDatabase::class.java, "beers").build()
            val beers = db.beerDao().getBeers()
            db.close()
            uiThread {
                if (beers.isNotEmpty()) {
                    val beersAdapter = BeersAdapter(
                        activity!!.applicationContext, beers)
                    list.adapter = beersAdapter
                }
            }
        }
    }

    companion object {
        fun newInstance(): ShowBeerView = ShowBeerView()
    }
}
