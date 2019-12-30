package com.labwhisper.biotech.finaldilution.component.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.component.appmodel.ComponentEditAppModel
import com.labwhisper.biotech.finaldilution.component.concentration.Concentration
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationFactory
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.genericitem.putExtraAnItem
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import com.labwhisper.biotech.finaldilution.util.*
import java.util.*

class EditComponentFragment : Fragment() {

    private lateinit var solution: Solution
    internal lateinit var compound: Compound
    private val initialConcentrationType = ConcentrationType.MOLAR
    private var desiredConcType: ConcentrationType = ConcentrationType.MOLAR
    private var stockConcType: ConcentrationType? = null
    private lateinit var desiredViewsList: MutableList<View>
    private lateinit var stockViewsList: MutableList<View>
    private lateinit var desiredButtonList: MutableList<RadioButton>
    private lateinit var stockButtonList: MutableList<RadioButton>
    private lateinit var careTaker: CareTaker<Solution>

    val appModel = ComponentEditAppModel()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getSerializable("COMPOUND").let { compound = it as Compound }
        arguments?.getSerializable("SOLUTION").let { solution = it as Solution }
        arguments?.getSerializable("CARE_TAKER").let { careTaker = it as CareTaker<Solution> }
        return inflater.inflate(R.layout.component_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // TODO create buttons according to concentration Types
        // TODO use getChildAt instead of creaing such lists

        desiredButtonList = ArrayList()
        desiredButtonList.add(radioButton(R.id.desiredPercentageConcButton))
        desiredButtonList.add(radioButton(R.id.desiredMolarConcButton))
        desiredButtonList.add(radioButton(R.id.desiredMilimolarConcButton))
        desiredButtonList.add(radioButton(R.id.desiredMgMlConcButton))
        desiredButtonList.add(radioButton(R.id.desiredNxConcButton))

        stockButtonList = ArrayList()
        stockButtonList.add(radioButton(R.id.stockPercentageConcButton))
        stockButtonList.add(radioButton(R.id.stockMolarConcButton))
        stockButtonList.add(radioButton(R.id.stockMilimolarConcButton))
        stockButtonList.add(radioButton(R.id.stockMgMlConcButton))
        stockButtonList.add(radioButton(R.id.stockNxConcButton))

        desiredViewsList = ArrayList()
        desiredViewsList.add(editText(R.id.desiredConcEditText))
        desiredViewsList.add(radioGroup(R.id.desiredConcButtonsBar))
        desiredViewsList.add(textView(R.id.desiredConcTextView))
        desiredViewsList.addAll(desiredButtonList)

        stockViewsList = ArrayList()
        stockViewsList.add(editText(R.id.stockConcEditText))
        stockViewsList.add(radioGroup(R.id.stockConcButtonsBar))
        stockViewsList.add(textView(R.id.stockConcTextView))
        stockViewsList.addAll(stockButtonList)

        textView(R.id.new_component_title).text = compound.displayName

        setKeyboardOnInputs()
        bindListeners()
        setConcentrationButtonsState(initialConcentrationType)
        fillComponentFields()
        refreshFromStock()
    }


    private fun bindListeners() {

        //FIXME 2 Add observers
        for ((i, button) in desiredButtonList.withIndex()) {
            button.setOnClickListener { changeDesireConcentration(ConcentrationType.fromInt(i)) }
        }

        for ((i, button) in stockButtonList.withIndex()) {
            button.setOnClickListener { changeStockConcentration(ConcentrationType.fromInt(i)) }
        }

        imageButton(R.id.enableStockDilutionButton).setOnClickListener { toggleSolutionFromStock() }

        imageButton(R.id.buttonAddCompoundDone).setOnClickListener { onAcceptComponent() }

        imageView(R.id.backToSolution).setOnClickListener { onCancelComponent() }

    }

    private fun changeDesireConcentration(type: ConcentrationType?) {
        applyDesiredConcButtonCheck(type ?: ConcentrationType.MOLAR)
        if (desiredConcType == ConcentrationType.NX) {
            appModel.fromStock = true
            refreshFromStock()
            checkStockConcButton(ConcentrationType.NX)
        } else {
            if (stockConcType == ConcentrationType.NX) {
                checkStockConcButton(ConcentrationType.MILIGRAM_PER_MILLILITER)
            }
        }
    }

    private fun changeStockConcentration(type: ConcentrationType?) {
        applyStockConcButtonCheck(type ?: ConcentrationType.MOLAR)
        if (stockConcType == ConcentrationType.NX) {
            checkDesiredConcButton(ConcentrationType.NX)
        } else {
            if (desiredConcType == ConcentrationType.NX) {
                checkDesiredConcButton(ConcentrationType.MILIGRAM_PER_MILLILITER)
            }
        }
    }

    private fun checkDesiredConcButton(type: ConcentrationType) {
        radioGroup(R.id.desiredConcButtonsBar).check(getDesiredConcButton(type))
        applyDesiredConcButtonCheck(type)
    }

    private fun applyDesiredConcButtonCheck(type: ConcentrationType) {
        desiredConcType = type
        editText(R.id.desiredConcEditText).hint = desiredConcType.hint()
    }

    private fun checkStockConcButton(type: ConcentrationType) {
        radioGroup(R.id.stockConcButtonsBar).check(getStockConcButton(type))
        applyStockConcButtonCheck(type)
    }

    private fun applyStockConcButtonCheck(type: ConcentrationType) {
        stockConcType = type
        editText(R.id.stockConcEditText).hint = stockConcType?.hint() ?: ""
    }

    private fun getDesiredConcButton(type: ConcentrationType): Int {
        return when (type) {
            ConcentrationType.PERCENTAGE -> R.id.desiredPercentageConcButton
            ConcentrationType.MOLAR -> R.id.desiredMolarConcButton
            ConcentrationType.MILIMOLAR -> R.id.desiredMilimolarConcButton
            ConcentrationType.MILIGRAM_PER_MILLILITER -> R.id.desiredMgMlConcButton
            ConcentrationType.NX -> R.id.desiredNxConcButton
        }
    }

    private fun getStockConcButton(type: ConcentrationType): Int {
        return when (type) {
            ConcentrationType.PERCENTAGE -> R.id.stockPercentageConcButton
            ConcentrationType.MOLAR -> R.id.stockMolarConcButton
            ConcentrationType.MILIMOLAR -> R.id.stockMilimolarConcButton
            ConcentrationType.MILIGRAM_PER_MILLILITER -> R.id.stockMgMlConcButton
            ConcentrationType.NX -> R.id.stockNxConcButton
        }
    }

    private fun fillComponentFields() {
        val component = solution.getComponentWithCompound(compound) ?: return

        editText(R.id.desiredConcEditText).apply {
            setText(component.desiredConcentration.concentration.toString())
        }

        component.availableConcentration?.let {
            editText(R.id.stockConcEditText).apply {
                setText(it.concentration.toString())
            }
        }

        setConcentrationButtonsState(
            component.desiredConcentration.type,
            component.availableConcentration?.type
        )
    }

    //TODO Change signature to component and concentration type...
    private fun setConcentrationButtonsState(
        desiredType: ConcentrationType,
        stockType: ConcentrationType? = null
    ) {
        appModel.fromStock = stockType != null
        if (molarWithNoMolarMass(compound.molarMass, desiredType)) {
            checkDesiredConcButton(ConcentrationType.PERCENTAGE)
        } else {
            checkDesiredConcButton(desiredType)
        }

        stockType?.let { checkStockConcButton(it) }

        setMolarButtonsVisibility(compound.molarMass)
    }

    private fun setMolarButtonsVisibility(molarMass: Double?) {
        val molarEnabled = molarMass != null
        radioButton(R.id.desiredMolarConcButton).isEnabled = molarEnabled
        radioButton(R.id.desiredMilimolarConcButton).isEnabled = molarEnabled
        radioButton(R.id.stockMolarConcButton).isEnabled = molarEnabled
        radioButton(R.id.stockMilimolarConcButton).isEnabled = molarEnabled
    }

    private fun molarWithNoMolarMass(molarMass: Double?, type: ConcentrationType) =
        molarMass == null && (type == ConcentrationType.MOLAR || type == ConcentrationType.MILIMOLAR)

    private fun onCancelComponent() {
        requireActivity().onBackPressed()
    }

    //TODO run this listener also on back and generally close?
    private fun onAcceptComponent() {

        //TODO Add all checks!
        val desiredConcEditText = editText(R.id.desiredConcEditText)
        val stockConcEditText = editText(R.id.stockConcEditText)
        if (desiredConcEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
            Anim().blink(desiredConcEditText)
            return
        }

        if (appModel.fromStock && stockConcEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
            Anim().blink(stockConcEditText)
            return
        }

        if (appModel.fromStock && stockConcType == null) {
            //TODO Add animation to buttons
            return
        }


        (solution.getComponentWithCompound(compound)?.also {
            updateComponent(it)
        } ?: createComponent())

        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtraAnItem(solution)
        intent.putExtra("CARE_TAKER", careTaker)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        //TODO WHEN ITEM EXISTS SHOW STH? OR BEFORE EVEN OPENING COMP.ACTIVITY?

    }

    private fun createComponent(): Component {
        val component =
            Component(compound, retrieveDesiredConcFromInput(), retrieveStockConcFromInput())

        solution.addComponent(component)
        return component
    }

    private fun retrieveStockConcFromInput(): Concentration? {
        if (!appModel.fromStock) {
            return null
        }
        val stockConcEditText = editText(R.id.stockConcEditText)
        val stockConcentrationValue = parseDoubleFromEditText(stockConcEditText)
        stockConcType?.let {
            return ConcentrationFactory.createConcentration(it, stockConcentrationValue)
        }
        return null
    }

    private fun retrieveDesiredConcFromInput(): Concentration {
        val desiredConcEditText = editText(R.id.desiredConcEditText)
        val concentrationValue = parseDoubleFromEditText(desiredConcEditText)
        return ConcentrationFactory.createConcentration(desiredConcType, concentrationValue)
    }

    private fun updateComponent(component: Component) {
        component.desiredConcentration = retrieveDesiredConcFromInput()
        component.availableConcentration = retrieveStockConcFromInput()
    }

    private fun parseDoubleFromEditText(desiredConcEditText: EditText): Double {
        return java.lang.Double.parseDouble(
            desiredConcEditText.text.toString().replace(',', '.')
        )
    }

    private fun setKeyboardOnInputs() {
        val concEditTexts = ArrayList<EditText>()
        concEditTexts.add(editText(R.id.desiredConcEditText))
        concEditTexts.add(editText(R.id.stockConcEditText))
        for (editText in concEditTexts) {

            editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            editText.setRawInputType(Configuration.KEYBOARD_12KEY)
            editText.setOnEditorActionListener { v, keyCode, event ->
                //TODO: Check this code for different versions of Android
                if (keyCode == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    // hide virtual keyboard
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun toggleSolutionFromStock() {
        appModel.fromStock = !appModel.fromStock
        refreshFromStock()
        if (!appModel.fromStock) {
            if (desiredConcType == ConcentrationType.NX) {
                checkDesiredConcButton(ConcentrationType.MILIGRAM_PER_MILLILITER)
            }
            if (stockConcType == ConcentrationType.NX) {
                checkStockConcButton(ConcentrationType.MILIGRAM_PER_MILLILITER)
            }
        }
    }

    private fun refreshFromStock() {
        for (stockView in stockViewsList) {
            val expandButton = imageButton(R.id.enableStockDilutionButton)
            val fromStockText = textView(R.id.from_stock)
            if (appModel.fromStock) {
                stockView.visibility = View.VISIBLE
                expandButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_expand_less_black_24dp
                    )
                )
                fromStockText.visibility = View.INVISIBLE
            } else {
                stockView.visibility = View.INVISIBLE
                expandButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_expand_more_black_24dp
                    )
                )
                fromStockText.visibility = View.VISIBLE
            }

        }
    }

    companion object {
        val TAG = "Edit Component"
    }

}
