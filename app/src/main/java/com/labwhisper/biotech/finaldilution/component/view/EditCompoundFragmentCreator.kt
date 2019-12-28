package com.labwhisper.biotech.finaldilution.component.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.view.NewCompoundFragment
import com.labwhisper.biotech.finaldilution.util.imageButton

class EditCompoundFragmentCreator {

    fun startCompoundEdition(
        activity: AppCompatActivity,
        compound: Compound? = null,
        onClose: (oldCompound: Compound?, newCompound: Compound?) -> Unit
    ) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val newCompoundFragment = NewCompoundFragment()
        compound?.let {
            val bundle = Bundle()
            bundle.putSerializable("COMPOUND", compound)
            newCompoundFragment.arguments = bundle
        }
        newCompoundFragment.setOnFragmentCloseListener { newCompound ->
            onClose(compound, newCompound)
        }
        fragmentTransaction.replace(R.id.compounds_fragment, newCompoundFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        activity.imageButton(R.id.new_compound_button).visibility = View.GONE
    }
}