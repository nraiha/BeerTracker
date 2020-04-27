package com.MobileComputing.BeerTracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.view_main.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainView : Fragment() {


    private fun get_sex(): Int? {
        var sex: Int? = null
        doAsync {
            val db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase::class.java, "user"
            ).build()
            sex = db.userDao().getSex()
            db.close()
        }
        return sex
    }

    private fun get_weight(): Double? {
        var weight: Double? = null
        doAsync {
            val db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase::class.java, "user"
            ).build()
            weight = db.userDao().getWeight()
            db.close()
        }
        return weight

    }

    fun calculateDrunkness(sex: Int, Wt: Double): Double
    {
        /*
         * Permilles are calculated with formula:
         *      (((0.806 * SD * 1.2) / (BW * Wt)) - MR * DP) * 10
         *
         * Where:
         *      SD = standard drinks.
         *          1 SD is one III beer. (3.3dl)
         *          IV beer is 1.4 SD (3.3dl)
         *      BW = body water constant (0.58 M / 0.49 F)
         *      Wt = body weight
         *      MR = metabolism constant (0.015 M / 0.017 F)
         *      DP = Drinking period hours
         */

        var SD: Double = 0.0
        var BW: Double = 0.0
        var Wt: Double = 0.0
        var MR: Double = 0.0
        var DP: Double = 0.0
        var permilles: Double = 0.0

        /* Male */
        if (sex == 0) {
            BW = 0.58
            MR = 0.015
        /* Female */
        } else if (sex == 1) {
            BW = 0.49
            MR = 0.017
        } else {
            /* Never should go here?? */
            return 100.0
        }

        /*
         * SD = getDrinks??
         * DP = getTime??
         */

        permilles = (((0.806 * SD *1.2) / (BW * Wt)) - MR * DP) * 10
        if (permilles < 0) {
            return 0.0
        }
        return permilles
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.view_main, container, false)
        var sex: Int? = get_sex()
        var weight: Double? = get_weight()

        var str: String = "\nSex: $sex\nWeight: $weight"
        Log.d("DEBUG", str)

        /* Check if weight and sex is added. Print text if not */
        if (sex == -1 || sex == null || weight == 0.0 || weight == null) {
            view.welcome.setText("Please input user info!")
            view.welcome.setTextColor(resources.getColor(R.color.error))
        } else {
            var x: Double = calculateDrunkness(sex, weight)
            var str: String = "Your bloods alcoholic content is: $x\n"
            view.welcome.text = str
            view.welcome.setTextColor(resources.getColor(R.color.allGood))
        }

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
}