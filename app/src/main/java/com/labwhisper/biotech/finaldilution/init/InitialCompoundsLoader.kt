package com.labwhisper.biotech.finaldilution.init

import android.content.Context
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.CompoundValidator
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
            val state = record[2]
            val density = record[3]
            val formula = record[4]
            val molarMass = record[5]
            Compound(
                iupacName = iupacName,
                liquid = state == "liquid",
                density = density.ifEmpty { null }?.toDouble(),
                molarMass = molarMass.ifEmpty { null }?.toDouble(),
                trivialName = trivialName,
                chemicalFormula = formula
            ).takeIf { CompoundValidator.validateNewCompound(it) }
        }
    }
}

