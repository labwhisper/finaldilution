package com.labessence.biotech.finaldilution.compound

class CompoundValidator {

    companion object {
        fun validateNewCompound(compound: Compound): Boolean {
            return when {
                !validateStoreName(compound.name) -> false
                !validateMolarMass(compound.molarMass) -> false
                else -> true
            }
        }

        private fun validateStoreName(name: String): Boolean {
            if (name.isBlank()) return false
            //if(name.contains())
            return true
        }

        private fun validateMolarMass(molarMass: Double) = molarMass > 0
    }
}

//Symptoms:
// TODO variable mass ( converted to 0.0?) ... rethink
// TODO Invalid (variable) molar mass converted to -1 in newCompound!
// - spaces in name -> %20 (Web notation) -> N
// - special chars  -> %23 :)

// Validation should happen prior to displaying the compound

// - in liquid state
// - with empty mass
// - with var/variable mass
// - with 0 mass
// - with negative mass


// .. allow null mass, and for null:
// - display info of not available molar concentrations
// - allow percentage concentrations with null parameter
// - disable molar buttons
