package com.labessence.biotech.finaldilution.compound

class CompoundValidator {

    companion object {
        fun validateNewCompound(compound: Compound): Boolean {
            if (!validateCompoundName(compound.name)) return false
            if (compound.molarMass <= 0) return false
            return true
        }

        fun validateCompoundName(name: String): Boolean {
            if (name.isBlank()) return false
            //if(name.contains())
            return true
        }
    }
}