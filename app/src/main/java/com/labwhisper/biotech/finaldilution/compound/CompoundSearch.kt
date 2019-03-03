package com.labwhisper.biotech.finaldilution.compound

class CompoundSearch {

    companion object {
        fun searchForCompound(allCompounds: List<Compound>, text: String?): List<Compound> {
            if (text == null) {
                return allCompounds
            }
            return allCompounds.toMutableList()
                .filter {
                    it.iupacName.contains(text, true)
                            || it.trivialName?.contains(text, true) ?: true
                            || it.chemicalFormula?.contains(text, true) ?: true
                }.toMutableList()
        }

    }
}