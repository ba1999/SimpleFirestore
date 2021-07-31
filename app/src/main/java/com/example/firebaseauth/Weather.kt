package com.example.simplefirestore


import com.google.firebase.firestore.ServerTimestamp



class Weather {

    private var hum = 0
    private var raum: String? = null
    private var temperatur = 0
    private var date: String? = null


    // serverTimestamp soll automatisch vom Server gesetzt werden
    @ServerTimestamp

    override fun toString(): String {
        return "Weather{" +
                ", hum=" + hum +
                ", raum='" + raum + '\'' +
                ", temperatur=" + temperatur +
                ", date='" + date + '\'' +
                '}'
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