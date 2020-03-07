package com.labwhisper.biotech.finaldilution.compound.view

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.CompoundValidator
import com.labwhisper.biotech.finaldilution.compound.appmodel.NewCompoundAppModel
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import com.labwhisper.biotech.finaldilution.util.*
import io.reactivex.disposables.CompositeDisposable

class NewCompoundFragment : Fragment() {

    lateinit var appModel: NewCompoundAppModel

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.compound_new, container, false)
        appModel = NewCompoundAppModel((requireActivity() as EditActivity).appModel)
        arguments?.getSerializable("COMPOUND").let { appModel.initialCompound = it as Compound? }
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView(R.id.form_name, R.id.textView).text = getString(R.string.iupac_name)
        textView(R.id.form_trivial_name, R.id.textView).text = getString(R.string.trivial_name)
        textView(R.id.form_molar_mass, R.id.textView).text = getString(R.string.molar_mass)
        editText(R.id.form_molar_mass, R.id.editText).inputType = InputType.TYPE_CLASS_NUMBER
        editText(R.id.form_molar_mass, R.id.editText).keyListener =
            DigitsKeyListener.getInstance("0123456789.")
        textView(R.id.form_density, R.id.textView).text = getString(R.string.density)
        editText(R.id.form_density, R.id.editText).keyListener =
            DigitsKeyListener.getInstance("0123456789.")
        textView(R.id.form_formula, R.id.textView).text = getString(R.string.chemical_formula)
        appModel.initialCompound?.let {
            textView(R.id.new_compound_title).text = "Edit ${it.iupacName}"
            populateCompoundIntoFields(it)
        }
        initAdvancedButton()
        initCancelButton()
        initDoneButton()
        setProceedOnLastEditText()
        requireActivity().findViewById<AddComponentButtonView>(R.id.compoundsListHeaderNew)
            .setOnClickListener {
                if (!isExpanded()) expand() else collapse()
            }
        radioGroup(R.id.radio_group_state_of_matter).setOnCheckedChangeListener { _, buttonId ->
            appModel.densityOpen.onNext(buttonId == R.id.radioButtonLiquid)
        }
        disposable.add(appModel.densityOpen.subscribe {
            constraintLayout(R.id.form_density).visibility =
                if (it == true) View.VISIBLE else View.INVISIBLE

        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun isExpanded(): Boolean {
        val compoundsListView = constraintLayout(R.id.compounds_fragment)
        val state = BottomSheetBehavior.from(compoundsListView).state
        return state != BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun expand() {
        val compoundsListView = constraintLayout(R.id.compounds_fragment)
        BottomSheetBehavior.from(compoundsListView).state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun collapse() {
        val compoundsListView = constraintLayout(R.id.compounds_fragment)
        BottomSheetBehavior.from(compoundsListView).state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun populateCompoundIntoFields(compound: Compound) {
        radioGroup(R.id.radio_group_state_of_matter).apply {
            check(
                when {
                    compound.liquid -> R.id.radioButtonLiquid
                    else -> R.id.radioButtonSolid
                }
            )
            jumpDrawablesToCurrentState()
        }
        constraintLayout(R.id.form_name).editText(R.id.editText).setText(compound.iupacName)
        compound.molarMass?.let {
            constraintLayout(R.id.form_molar_mass).editText(R.id.editText)
                .setText(it.toString())
        }
        compound.density?.let {
            constraintLayout(R.id.form_density).editText(R.id.editText)
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
        disposable.add(appModel.advancedOptions.subscribe {
            constraintLayout(R.id.form_trivial_name).visibility =
                if (it == true) View.VISIBLE else View.INVISIBLE
            constraintLayout(R.id.form_formula).visibility =
                if (it == true) View.VISIBLE else View.INVISIBLE
            imageButton(R.id.form_expand_advanced).setImageDrawable(
                if (it == true)
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_expand_less_black_24dp
                    )
                else
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_expand_more_black_24dp
                    )
            )
        })
        imageButton(R.id.form_expand_advanced).setOnClickListener {
            appModel.advancedOptions.onNext(appModel.advancedOptions.value != true)
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

    @VisibleForTesting
    internal fun onProceed() {
        val compound = attemptToCreateCompoundFromFields()
        if (!CompoundValidator.validateNewCompound(compound)) {
            fragmentManager?.popBackStack()
            return
        }
        hideKeyboard()
        //TODO update should ask for update / save new
        //TODO safeSave -> on error the question replace should be asked
        appModel.proceedWithCompound(compound)
        fragmentManager?.popBackStack()
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText(R.id.form_name, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_trivial_name, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_formula, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_molar_mass, R.id.editText).windowToken, 0)
        imm.hideSoftInputFromWindow(editText(R.id.form_density, R.id.editText).windowToken, 0)
    }

    private fun attemptToCreateCompoundFromFields(): Compound {
        val liquid = radioGroup(R.id.radio_group_state_of_matter)
            .checkedRadioButtonId == R.id.radioButtonLiquid
        val iupacName = editTextValue(R.id.form_name, R.id.editText)
        val trivialName = editTextValue(R.id.form_trivial_name, R.id.editText)
        val formula = editTextValue(R.id.form_formula, R.id.editText)
        val molarMass = try {
            editTextValue(R.id.form_molar_mass, R.id.editText)
                .ifBlank { null }?.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
        val density =
            if (!liquid) null
            else {
                try {
                    editTextValue(R.id.form_density, R.id.editText)
                        .ifBlank { null }?.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
            }
        return Compound(
            iupacName = iupacName,
            liquid = liquid,
            molarMass = molarMass,
            trivialName = trivialName,
            chemicalFormula = formula,
            density = density
        )
    }

    private var onFragmentCloseListener: ((compound: Compound?) -> Unit)? = null

    fun setOnFragmentCloseListener(function: (compound: Compound?) -> Unit) {
        onFragmentCloseListener = function
    }

    override fun onStop() {
        onFragmentCloseListener?.invoke(appModel.newCompound)
        appModel.newCompound = null
        super.onStop()
        disposable.clear()
    }

    companion object {
        val TAG = "New Compound"
    }
}