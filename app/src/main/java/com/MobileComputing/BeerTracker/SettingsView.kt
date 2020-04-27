package com.MobileComputing.BeerTracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.view_settings.*
import kotlinx.android.synthetic.main.view_settings.view.*
import org.jetbrains.anko.doAsync

class SettingsView : Fragment() {
    private var sex: Int = -1
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
            /* Check that sex is selected */
            if (sex == -1) {
                Toast.makeText(view.context, "Choose sex!",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* Check that weight has been given */
            val weight: Double? = weight_box.text.toString().toDoubleOrNull()
            if (weight == 0.0 || weight == null) {
                Toast.makeText(view.context, "Set weight!",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* Insert into database */
            val userInfo = UserInfo(
                uid = null,
                weight = weight,
                sex = sex
            )

            doAsync {
                val db = Room.databaseBuilder(view.context,
                        AppDatabase::class.java, "user").build()
                db.userDao().update(userInfo)
                db.close()
            }
            Toast.makeText(view.context, "Settings saved!",
                Toast.LENGTH_SHORT).show()
        }

        return view
    }

    companion object {
        fun newInstance(): SettingsView = SettingsView()
    }
}