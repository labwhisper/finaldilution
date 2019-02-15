package com.labessence.biotech.finaldilution.init

import android.content.Context
import com.labessence.biotech.finaldilution.compound.Compound
import com.opencsv.CSVReader
import java.io.InputStreamReader

fun loadDefaultCompounds(context: Context): List<Compound> {
    val csvReader = CSVReader(
        InputStreamReader(
            context.assets.open("default_compounds.csv")
        )
    )
    return csvReader.readAll().map { record ->
        record.iterator().next().run {
            val iName = record[0]
            val shortName = record[1]
            val formula = record[3]
            val molarMass = record[4]
            Compound(
                shortName = shortName,
                molarMass = molarMass.takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
            ).apply {
                iupacName = iName
                chemicalFormula = formula
            }
        }
    }
}