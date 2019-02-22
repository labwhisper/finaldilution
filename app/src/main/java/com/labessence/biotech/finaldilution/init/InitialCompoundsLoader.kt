package com.labessence.biotech.finaldilution.init

import android.content.Context
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.compound.CompoundValidator
import com.opencsv.CSVReader
import java.io.InputStreamReader

fun loadDefaultCompounds(context: Context): List<Compound?> {
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
                iupacName = iupacName,
                molarMass = molarMass.ifEmpty { "0" }.toDouble(),
                trivialName = trivialName,
                chemicalFormula = formula
            ).takeIf { CompoundValidator.validateNewCompound(it) }
        }
    }
}

