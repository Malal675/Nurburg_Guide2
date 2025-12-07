package com.example.nurburg_guide.ui.features.map.data

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.data.autovermietung.VermietungLocations
import com.example.nurburg_guide.ui.features.map.data.gastronomie.GastronomieLocations
import com.example.nurburg_guide.ui.features.map.data.ladesaeulen.LadesaeulenLocations
import com.example.nurburg_guide.ui.features.map.data.nurburg.NurburgLocations
import com.example.nurburg_guide.ui.features.map.data.parkplatz.ParkplatzLocations
import com.example.nurburg_guide.ui.features.map.data.querungen.QuerungenLocations
import com.example.nurburg_guide.ui.features.map.data.tankstelle.TankstellenLocations
import com.example.nurburg_guide.ui.features.map.data.taxi.TaxiLocations

object AllLocations {

    val all: List<GuideLocation> =
        GastronomieLocations.all +
                NurburgLocations.all +
                ParkplatzLocations.all +
                QuerungenLocations.all +
                TankstellenLocations.all +
                TaxiLocations.all +
                VermietungLocations.all +
                LadesaeulenLocations.all
}