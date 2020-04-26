package com.MobileComputing.BeerTracker

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.view_settings.*
import kotlinx.android.synthetic.main.view_settings.view.*
import kotlinx.android.synthetic.main.view_settings.view.weight_box
import org.jetbrains.anko.doAsync

class SettingsView : Fragment() {
    var weight: Double = 0.0
    var sex: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) : View? {

        val view: View =  inflater.inflate(R.layout.view_settings,
                container, false)

        /* Radio buttons functionality */
        view.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radio) {
                radioButtonMale -> {
                    sex = 0
                }
                radioButtonFemale -> {
                    sex = 1
                }
            }
        }

        /* Save to database */
        view.btn_save.setOnClickListener {
            /* This is a problem? */
            var weight_str: String = weight_box.text.toString()
            if (weight_str.isEmpty()) {
                weight = 0.0
            } else {
                weight = weight_box.text.toString().toDouble()
            }

            val str: String = "sex: $sex\nweight: $weight"
            Toast.makeText(view.context, str, Toast.LENGTH_SHORT).show()

            val userInfo = UserInfo(
                uid = null,
                weight = weight,
                sex = sex
            )

            doAsync {
                val db = Room.databaseBuilder(view.context,
                        AppDatabase::class.java, "user").build()
                val uid = db.userDao().insert(userInfo).toInt()
                userInfo.uid = uid

                db.close()

            }

        }

        return view
    }


    companion object {
        fun newInstance(): SettingsView = SettingsView()
    }
}