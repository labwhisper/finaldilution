package com.labessence.biotech.finaldilution.compound.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.compound.CompoundValidator
import com.labessence.biotech.finaldilution.util.editText
import com.labessence.biotech.finaldilution.util.editTextValue

class NewCompoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.compound_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCancelButton()
        initDoneButton()
        setProceedOnLastEditText()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initCancelButton() {
        val cancelButton =
            requireActivity().findViewById<ImageButton>(R.id.button_new_compound_cancel)
        cancelButton.setOnClickListener { fragmentManager?.popBackStack() }
    }

    private fun initDoneButton() {
        val doneButton =
            requireActivity().findViewById<ImageButton>(R.id.button_new_compound_done)
        doneButton.setOnClickListener {
            onProceed()
        }
    }

    private fun onProceed() {
        val compound = attemptToCreateCompoundFromFields()
        if (CompoundValidator.validateNewCompound(compound)) {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText(R.id.editTextFormula).windowToken, 0)
            val appState: ApplicationContext =
                requireActivity().applicationContext as ApplicationContext
            appState.compoundGateway.save(compound)
        }
        fragmentManager?.popBackStack()
    }

    private fun setProceedOnLastEditText() {
        val lastEditText = editText(R.id.editTextFormula)
        lastEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onProceed()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun attemptToCreateCompoundFromFields(): Compound {

        val iupacName = editTextValue(R.id.editTextIupac)
        val molarMass = try {
            editTextValue(R.id.editTextMolarMass).toDouble()
        } catch (e: NumberFormatException) {
            return Compound(iupacName = iupacName, liquid = false, molarMass = null)
        }
        val trivialName = editTextValue(R.id.editTextTrivialName)
        val formula = editTextValue(R.id.editTextFormula)
        return Compound(iupacName, false, molarMass, trivialName, formula)
    }

    private var onFragmentCloseListener: (() -> Unit)? = null

    fun setOnFragmentCloseListener(function: () -> Unit) {
        onFragmentCloseListener = function
    }

    override fun onStop() {
        onFragmentCloseListener?.invoke()
        super.onStop()
    }
}