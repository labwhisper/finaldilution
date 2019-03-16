package com.labwhisper.biotech.finaldilution.compound.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.text.method.DigitsKeyListener
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
import com.labwhisper.biotech.finaldilution.util.*

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
        textView(R.id.form_name, R.id.textView).text = getString(R.string.iupac_name)
        textView(R.id.form_trivial_name, R.id.textView).text = getString(R.string.trivial_name)
        textView(R.id.form_molar_mass, R.id.textView).text = getString(R.string.molar_mass)
        editText(R.id.form_molar_mass, R.id.editText).inputType = InputType.TYPE_CLASS_NUMBER
        editText(R.id.form_molar_mass, R.id.editText).keyListener =
            DigitsKeyListener.getInstance("0123456789.")
        textView(R.id.form_formula, R.id.textView).text = getString(R.string.chemical_formula)
        initialCompound?.let {
            textView(R.id.new_compound_title).text = "Edit ${it.iupacName}"
            populateCompoundIntoFields(it)
        }
        initAdvancedButton()
        initCancelButton()
        initDoneButton()
        setProceedOnLastEditText()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun populateCompoundIntoFields(compound: Compound) {
        radioGroup(R.id.radio_group_state_of_matter).check(
            when {
                compound.liquid -> R.id.radioButtonLiquid
                else -> R.id.radioButtonSolid
            }
        )
        constraintLayout(R.id.form_name).editText(R.id.editText).setText(compound.iupacName)
        compound.molarMass?.let {
            constraintLayout(R.id.form_molar_mass).editText(R.id.editText)
                .setText(it.toString())
        }
        compound.trivialName?.let {
            constraintLayout(R.id.form_trivial_name).editText(R.id.editText).setText(it)
        }
        compound.chemicalFormula?.let {
            constraintLayout(R.id.form_formula).editText(
                R.id.editText
            ).setText(it)
        }
    }

    private fun initAdvancedButton() {
        val button = imageButton(R.id.form_expand_advanced)
        button.setOnClickListener {
            val toBeCollapsed = constraintLayout(R.id.form_trivial_name).visibility == View.VISIBLE
            constraintLayout(R.id.form_trivial_name).visibility =
                if (toBeCollapsed) View.INVISIBLE else View.VISIBLE
            constraintLayout(R.id.form_formula).visibility =
                if (toBeCollapsed) View.INVISIBLE else View.VISIBLE
            button.setImageDrawable(
                if (toBeCollapsed)
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_expand_more_black_24dp
                    )
                else
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_expand_less_black_24dp
                    )
            )
        }
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
        val lastEditText =
            constraintLayout(R.id.form_name).editText(R.id.editText)
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
        imm.hideSoftInputFromWindow(editText(R.id.form_name, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_trivial_name, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_formula, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_molar_mass, R.id.editText).windowToken, 0)
        val appState: ApplicationContext =
            requireActivity().applicationContext as ApplicationContext
        //TODO update should ask for update / save new
        //TODO safeSave -> on error the question replace should be asked
        initialCompound?.let { appState.updateCompound(compound) }
            ?: appState.safeSaveCompound(compound)
        fragmentManager?.popBackStack()
    }

    private fun attemptToCreateCompoundFromFields(): Compound {
        val iupacName = editTextValue(R.id.form_name, R.id.editText)
        val trivialName = editTextValue(R.id.form_trivial_name, R.id.editText)
        val formula = editTextValue(R.id.form_formula, R.id.editText)
        val molarMass = try {
            editTextValue(R.id.form_molar_mass, R.id.editText)
                .ifBlank { null }?.toDouble()
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