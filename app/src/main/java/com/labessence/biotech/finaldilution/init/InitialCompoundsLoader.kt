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
            val iupacName = record[0]
            val trivialName = record[1]
            val formula = record[3]
            val molarMass = record[4]
            Compound(
                trivialName = trivialName,
                molarMass = molarMass.ifEmpty { "0" }.toDouble(),
                iupacName = iupacName,
                chemicalFormula = formula
            )
        }
    }
}

//TODO Validator - move from NewCompoundF to

//Symptoms:
// - variable mass ( converted to 0.0?) ... rethink
// - spaces in name -> %20 (Web notation)
// - special chars  -> %23 :)

// Validation should happen prior to displaying the compound
