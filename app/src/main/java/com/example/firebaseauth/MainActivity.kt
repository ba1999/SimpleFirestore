package com.example.simplefirestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import splitties.toast.toast
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val etRoom : EditText by lazy { findViewById(R.id.etRoom)}
    private val etTemp : EditText by lazy { findViewById(R.id.etTemp)}
    private val etDate : EditText by lazy { findViewById(R.id.etDate)}
    private val etHum : EditText by lazy { findViewById(R.id.etHum)}
    private val btnSave : Button by lazy { findViewById(R.id.btnSave)}
    private val btnAnalyze : Button by lazy { findViewById(R.id.btnAnalyze)}
    private val tvLogStatus : TextView by lazy { findViewById(R.id.tvLogStatus) }
    private val mFirebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db : FirebaseFirestore by lazy { FirebaseFirestore.getInstance()  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mFirebaseAuth.currentUser == null) {
            tvLogStatus.text = getString(R.string.logged_out)
        } else {
            tvLogStatus.text = getString(R.string.logged_in)
        }

        btnSave.setOnClickListener {
            val sRaum: String = etRoom.text.toString()
            val sTemp: String = etTemp.text.toString()
            val sHum: String = etHum.text.toString()
            val sDate: String = etDate.text.toString()

            if (sRaum.isEmpty() || sTemp.isEmpty() || sHum.isEmpty() || sDate.isEmpty()) {
               toast(getString(R.string.eingabefehler))
            } else {
                insertDataInDb(sRaum, Integer.valueOf(sTemp), Integer.valueOf(sHum), sDate)
            }
        }

        btnAnalyze.setOnClickListener {

        }
    }

    private fun insertDataInDb(raum: String, temp: Int, hum: Int, date: String) {

        // Weather Objekt mit Daten befüllen (ID wird automatisch ergänzt)
        val weather = Weather()
        weather.setRaum(raum)
        weather.setTemperatur(temp)
        weather.setHum(hum)
        weather.setDate(date)

        // Schreibe Daten als Document in die Collection Messungen in DB;
        // Eine id als Document Name wird automatisch vergeben
        db.collection("Messungen")
            .add(weather)
            .addOnSuccessListener { documentReference ->
                toast(getString(R.string.save))
            }
            .addOnFailureListener { e ->
                toast(getString(R.string.not_save))
            }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.itLogin -> {
                onClickMenuItem_1()
                true
            }
            R.id.itLogout -> {
                onClickMenuItem_2()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Einloggen und Registrieren
    fun onClickMenuItem_1() {
        // ist der Nutzer eingelogged?
        if (mFirebaseAuth.currentUser != null) {
            Toast.makeText(this, resources.getString(R.string.logged_in),
                    Toast.LENGTH_LONG).show()
            tvLogStatus.text = getString(R.string.alreadyLoggedIn)
        } else {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }

    // Ausloggen
    fun onClickMenuItem_2() {
        if (mFirebaseAuth.currentUser == null) {
            Toast.makeText(applicationContext,
                    R.string.alreadyLoggedOut, Toast.LENGTH_LONG).show()
        } else {
            //Befehl zum Ausloggen
            mFirebaseAuth.signOut()
            tvLogStatus.text = getString(R.string.logged_out)
        }
    }

}