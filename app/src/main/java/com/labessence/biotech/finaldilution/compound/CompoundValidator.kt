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

        private fun validateMolarMass(molarMass: Double?): Boolean {
            if (molarMass == null) return true
            return molarMass > 0
        }
    }
}

// TODO display info of not available molar concentrations
