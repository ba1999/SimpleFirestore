package com.example.simplefirestore


import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


class Weather {

    private var id: String? = null
    private var hum = 0
    private var raum: String? = null
    private var temperatur = 0
    private var date: String? = null
    private var dateTimestamp: Date? = null


    // serverTimestamp soll automatisch vom Server gesetzt werden
    @ServerTimestamp
    private var serverTimestamp: Timestamp? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getDateTimestamp(): Date? {
        return dateTimestamp
    }

    fun setDateTimestamp(dateTimestamp: Date?) {
        this.dateTimestamp = dateTimestamp
    }

    fun getServerTimestamp(): Timestamp? {
        return serverTimestamp
    }

    override fun toString(): String {
        return "Weather{" +
                "id='" + id + '\'' +
                ", hum=" + hum +
                ", raum='" + raum + '\'' +
                ", temperatur=" + temperatur +
                ", date='" + date + '\'' +
                ", dateTimestamp=" + dateTimestamp +
                '}'
    }


    fun setServerTimestamp(serverTimestamp: Timestamp?) {
        this.serverTimestamp = serverTimestamp
    }

    fun getHum(): Int {
        return hum
    }

    fun setHum(hum: Int) {
        this.hum = hum
    }

    fun getRaum(): String? {
        return raum
    }

    fun setRaum(raum: String?) {
        this.raum = raum
    }

    fun getTemperatur(): Int {
        return temperatur
    }

    fun setTemperatur(temperatur: Int) {
        this.temperatur = temperatur
    }

    fun getDate(): String? {
        return date
    }

    fun setDate(date: String?) {
        this.date = date
    }
}