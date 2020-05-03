package com.MobileComputing.BeerTracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.view_main.view.*
import org.jetbrains.anko.doAsync
import java.time.Instant
import java.util.*

class MainView : Fragment() {

    private val TAG = "MainView"
    private var receivedSex : Int = 0
    private var receivedWeight : Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.view_main, container, false)

        // Return the fragment view/layout
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doAsync {
            val db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase2::class.java, "user"
            ).build()
            receivedSex = db.userDao().getSex()
            receivedWeight = db.userDao().getWeight()
            db.close()

            /* Check if weight and sex is added. Print text if not */
            if (receivedSex == -1 || receivedWeight == 0.0) {
                view.welcome.text = "Please input user info!"
                view.welcome.setTextColor(
                    ContextCompat.getColor(context!!, R.color.error))
            } else {
                val x: Double = calculatePerMils(receivedSex, receivedWeight)
                val str: String = String.format(
                    "Your bloods alcoholic content is: %.2f", x)
                view.welcome.text = str
                view.welcome.setTextColor(
                    ContextCompat.getColor(context!!, R.color.allGood))
            }
        }

            view.btn_addBeer.setOnClickListener {
                val intent = Intent(activity, AddBeerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
    }

    private fun getTimeLimit(): Long
    {
        val cal: Calendar = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.HOUR, -12)
        return cal.time.time
    }

    private fun convertToSD(size: Double, percent: Double): Double
    {
        /* 0.789 is density of ethanol at room temperature */
        return size * percent * 0.789
    }

    private fun calculatePerMils(sex: Int?, Wt: Double?): Double
    {
        /*
         * Per mils are calculated with formula:
         *      (((0.806 * SD * 1.2) / (BW * Wt)) - MR * DP) * 10
         *
         * Where:
         *      SD = standard drinks.
         *          1 SD is one III beer. (3.3dl)
         *          IV beer is 1.4 SD (3.3dl)
         *
         *      SD is calculated:
         *          L * % * 0.789
         *          where:
         *              L = amount in litres
         *              % = alcohol percent
         *
         *      BW = body water constant (0.58 M / 0.49 F)
         *      Wt = body weight
         *      MR = metabolism constant (0.015 M / 0.017 F)
         *      DP = Drinking period hours
         */

        var SD: Double = 0.0
        var BW: Double = 0.0
        var MR: Double = 0.0
        var DP: Double = 0.0
        var perMils: Double = 0.0

        /* 0 is male, 1 is female */
	when (sex) {
            0 -> { BW = 0.58; MR = 0.015 }
            1 -> { BW = 0.49; MR = 0.017 }
            /* We should never go here, what do? */
            else -> { return 100.0 }
        }

        /* Use only beers from the last 12 hours */
        val timeLimit: Long = getTimeLimit()

        /* Query the beers where timestamp is greater than the timeLimit */
        val db = Room.databaseBuilder(activity!!.applicationContext,
            AppDatabase::class.java, "beers").build()
        val beers = db.beerDao().getBeersWithTimestamp(timeLimit)

        var flag: Int = 0

        for (beer in beers) {
            if (flag == 0) {
                flag = 1
                var now: Calendar = Calendar.getInstance()
                now.time = Date()
                var beerTime = Date(beer.time)
                var s: Double = 0.0
                s = ((now.time.time - beerTime.time) / 1000 ).toDouble()
                Log.d(TAG, "Seconds: $s")
                DP = s/60/60
                Log.d(TAG, "DP: $DP")

            }
            /* Convert the beer size and alcohol percent to SD's */
            SD += convertToSD(beer.bottle_size!!, beer.percentage!!.toDouble())
        }


        /* Calculate the per mils */
        perMils = (((0.086 * SD * 1.2) / (BW * Wt!!)) - MR * DP) * 10
        if (perMils < 0)
            return 0.00

        Log.d(TAG, "Per mils : $perMils")

        return perMils
    }

    companion object {
        fun newInstance(): MainView = MainView()
    }
}
