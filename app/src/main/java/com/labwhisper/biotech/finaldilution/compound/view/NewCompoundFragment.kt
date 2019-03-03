package com.labwhisper.biotech.finaldilution.compound.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.CompoundValidator
import com.labwhisper.biotech.finaldilution.util.editText
import com.labwhisper.biotech.finaldilution.util.editTextValue
import com.labwhisper.biotech.finaldilution.util.textView

class NewCompoundFragment : Fragment() {

    var initialCompound: Compound? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getSerializable("COMPOUND").let { initialCompound = it as Compound? }
        return inflater.inflate(R.layout.compound_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialCompound?.let {
            textView(R.id.new_compound_title).text = "Edit ${it.iupacName}"
            populateCompoundIntoFields(it)
        }
        initCancelButton()
        initDoneButton()
        setProceedOnLastEditText()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun populateCompoundIntoFields(compound: Compound) {
        editText(R.id.editTextIupac).setText(compound.iupacName)
        compound.molarMass?.let { editText(R.id.editTextMolarMass).setText(it.toString()) }
        compound.trivialName?.let { editText(R.id.editTextTrivialName).setText(it) }
        compound.chemicalFormula?.let { editText(R.id.editTextFormula).setText(it) }
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

    private fun onProceed() {
        val compound = attemptToCreateCompoundFromFields()
        if (!CompoundValidator.validateNewCompound(compound)) {
            fragmentManager?.popBackStack()
            return
        }
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText(R.id.editTextFormula).windowToken, 0)
        val appState: ApplicationContext =
            requireActivity().applicationContext as ApplicationContext
        //TODO update should ask for update / save new
        //TODO safeSave -> on error the question replace should be asked
        initialCompound?.let { appState.updateCompound(compound) }
            ?: appState.safeSaveCompound(compound)
        fragmentManager?.popBackStack()
    }

    private fun attemptToCreateCompoundFromFields(): Compound {

        val iupacName = editTextValue(R.id.editTextIupac)
        val trivialName = editTextValue(R.id.editTextTrivialName)
        val formula = editTextValue(R.id.editTextFormula)
        val molarMass = try {
            editTextValue(R.id.editTextMolarMass).ifBlank { null }?.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
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