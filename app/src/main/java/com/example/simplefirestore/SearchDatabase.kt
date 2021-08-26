package com.example.simplefirestore

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SearchDatabase : AppCompatActivity() {

    private val radioGroup : RadioGroup by lazy { findViewById(R.id.radioGroup)}
    private val etFilter : EditText by lazy { findViewById(R.id.etFilter) }
    private val btnFilter : Button by lazy { findViewById(R.id.btnFilter)}
    private val listView: ListView by lazy { findViewById(R.id.listview)}

    private val mFirebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private lateinit var dbList: List<Weather>

    private lateinit var adapter: ArrayAdapter<Weather>

    private val TAG = "SearchDatabase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_database)


        // Adapter initialisieren, Layout und Liste zuweisen
        loadDbList()

        btnFilter.setOnClickListener {

            // Welcher Button ist ausgewählt
            val id = radioGroup.checkedRadioButtonId
            val sFilter: String = etFilter.text.toString()



            when (id) {
                R.id.radioButton1 -> {
                    findRoom(sFilter)
                    Toast.makeText(
                        applicationContext,
                        "Raumfilter $sFilter", Toast.LENGTH_LONG
                    ).show()
                }
                R.id.radioButton2 -> {
                    // Filter auf Temperatur
                    // Ist Filterwert ein int?
                    if (!checkInt(sFilter)) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.filterwertfehler), Toast.LENGTH_LONG
                        ).show()
                    }else {
                        val iFilter = Integer.valueOf(sFilter)
                        // Datenbankabfrage
                        findTemp(iFilter)
                    }
                }
                R.id.radioButton3 -> {
                    findDate(sFilter)
                    Toast.makeText(
                        applicationContext,
                        "Datum Filter $sFilter", Toast.LENGTH_LONG
                    ).show()
                }
                R.id.radioButton4 -> {
                    findHottest()
                    Toast.makeText(
                        applicationContext,
                        "Top 5 Filter $sFilter", Toast.LENGTH_LONG
                    ).show()
                }
                R.id.radioButton5 -> {
                    // Löschanfrage
                    deleteSelected(sFilter)
                    // Liste nach Löschen neu laden
                    loadDbList()
                }
                else -> Log.e(TAG, "onClickFilterButton default. Sollte nicht passieren")
            }
        }

    }

    // Alle Documents in der Messungen Collection des users abrufen
    fun loadDbList() {

        // Einstiegspunkt für die Abfrage ist users/uid/Messungen
        val uid = mFirebaseAuth.currentUser!!.uid
        db.collection("users").document(uid).collection("Messungen") // alle Einträge abrufen
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateListView(task)
                } else {
                    Log.d(TAG, "FEHLER: Daten lesen ", task.exception)
                }
            }
    }

    // Alle Documents eines spezifischen Raums in der Messungen Collection des users abrufen
    fun findRoom(room: String?) {

        // Einstiegspunkt für die Abfrage ist users/uid/Messungen
        val uid = mFirebaseAuth.currentUser!!.uid
        db.collection("users").document(uid).collection("Messungen") // Filter
            .whereEqualTo("raum", room) // alle Einträge abrufen
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateListView(task)
                } else {
                    Log.d(TAG, "FEHLER: Daten lesen ", task.exception)
                }
            }
    }

    // Alle Documents mit Temperaturen >= temperature
    fun findTemp(temperature: Int) {

        // Einstiegspunkt für die Abfrage ist users/uid/Messungen
        val uid = mFirebaseAuth.currentUser!!.uid
        db.collection("users").document(uid).collection("Messungen") // Filter
            .whereGreaterThanOrEqualTo("temperatur", temperature) // abrufen
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateListView(task)
                } else {
                    Log.d(TAG, "FEHLER: Daten lesen ", task.exception)
                }
            }
    }

    // Alle Documents eines bestimmten Tages
    fun findDate(date: String?) {

        // Wandle String date (im Format yyyy-MM-dd !!!) um in ein Date Objekt
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var datetimestamp: Date? = null
        try {
            datetimestamp = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Einstiegspunkt für die Abfrage ist users/uid/Messungen
        val uid = mFirebaseAuth.currentUser!!.uid
        db.collection("users").document(uid).collection("Messungen") // Filter
            .whereEqualTo("dateTimestamp", datetimestamp) // abrufen
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateListView(task)
                } else {
                    Log.d(TAG, "FEHLER: Daten lesen ", task.exception)
                }
            }
    }

    // Die 5 Documente mit den höchsten Temperaturen
    fun findHottest() {

        // Einstiegspunkt für die Abfrage ist users/uid/Messungen
        val uid = mFirebaseAuth.currentUser!!.uid
        db.collection("users").document(uid).collection("Messungen") // Filter
            .orderBy("temperatur", Query.Direction.DESCENDING).limit(5) // abrufen
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateListView(task)
                } else {
                    Log.d(TAG, "FEHLER: Daten lesen ", task.exception)
                }
            }
    }

    // Document mit der ID id loeschen
    fun deleteSelected(id: String?) {
        // Einstiegspunkt für die Abfrage ist users/uid/Messungen
        val uid = mFirebaseAuth.currentUser!!.uid
        db.collection("users").document(uid).collection("Messungen") // loesche
            .document(id!!)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "Document gelöscht!") }
            .addOnFailureListener { e -> Log.w(TAG, "FEHLER, Document nicht gelöscht", e) }
    }

    private fun checkInt(s: String): Boolean {
        return try {
            val i = Integer.valueOf(s)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun updateListView(task: Task<QuerySnapshot>) {
        // Einträge in dbList kopieren, um sie im ListView anzuzeigen
        dbList = ArrayList()
        // Diese for schleife durchläuft alle Documents der Abfrage
        for (document in task.result!!) {
            val messung = document.toObject(Weather::class.java)
            val id = document.id
            messung.setId(id)
            (dbList as ArrayList<Weather>).add(messung)
            Log.d(TAG, document.id + " => " + document.data)
        }
        // jetzt liegt die vollständige Liste vor und
        // kann im ListView angezeigt werden
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, dbList
        )
        listView.adapter = adapter
    }
}