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
import androidx.lifecycle.Observer
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.component.concentration.*
import com.labwhisper.biotech.finaldilution.component.validation.ComponentValidateInteractor
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
    private var desiredConcType: ConcentrationType = ConcentrationType.MOLAR
    private var stockConcType: ConcentrationType = ConcentrationType.MOLAR
    private lateinit var desiredViewsList: MutableList<View>
    private lateinit var stockViewsList: MutableList<View>
    private lateinit var desiredButtonList: MutableList<RadioButton>
    private lateinit var stockButtonList: MutableList<RadioButton>
    private lateinit var careTaker: CareTaker<Solution>

    val appModel =
        ComponentEditAppModel()
    val presenter = EditComponentPresenter(appModel)
    val possibleConcentrationsInteractor = PossibleConcentrationsInteractor()
    val chooseMostSuitableConcentrationInteractor = ChooseMostSuitableConcentrationInteractor()
    val compatibleConcentrationsInteractor = CompatibleConcentrationsInteractor()

    val componentValidateInputPort = ComponentValidateInteractor(
        presenter,
        possibleConcentrationsInteractor,
        chooseMostSuitableConcentrationInteractor,
        compatibleConcentrationsInteractor
    )

    val controller = EditComponentController(componentValidateInputPort)

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

        setNoMolarWarning()
        disableMgMlForLiquids()
        setKeyboardOnInputs()
        bindListeners()
        fillComponentFields()
    }

    private fun setNoMolarWarning() {
        textView(R.id.warningTextView).visibility =
            if (compound.molarMass == null) View.VISIBLE else View.GONE
        textView(R.id.warningTextView).text =
            "No molar mass entered."
    }

    private fun disableMgMlForLiquids() {
        radioButton(R.id.desiredMgMlConcButton).isEnabled = !compound.liquid
        radioButton(R.id.stockMgMlConcButton).isEnabled = !compound.liquid
    }

    private fun bindListeners() {

        for ((i, button) in desiredButtonList.withIndex()) {
            button.setOnClickListener { changeDesireConcentration(ConcentrationType.fromInt(i)) }
        }

        for ((i, button) in stockButtonList.withIndex()) {
            button.setOnClickListener { changeStockConcentration(ConcentrationType.fromInt(i)) }
        }

        imageButton(R.id.enableStockDilutionButton).setOnClickListener { toggleSolutionFromStock() }

        imageButton(R.id.buttonAddCompoundDone).setOnClickListener { onAcceptComponent() }

        imageView(R.id.backToSolution).setOnClickListener { onCancelComponent() }

        appModel.desiredConcentrationType.observe(this, Observer { checkDesiredConcButton(it) })
        appModel.stockConcentrationType.observe(this, Observer { checkStockConcButton(it) })
        appModel.stockConcentrationsAvailable.observe(
            this,
            Observer { enableStockConcentrations(it) })
        appModel.fromStock.observe(this, Observer { refreshFromStock(it) })
    }

    private fun enableStockConcentrations(concentrations: List<ConcentrationType>) {
        stockButtonList.filterNot { button ->
            concentrations.map { radioButton(getStockConcButton(it)) }.contains(button)
        }
            .forEach { it.isEnabled = false }
        concentrations.forEach { radioButton(getStockConcButton(it)).isEnabled = true }
    }

    private fun changeDesireConcentration(type: ConcentrationType?) {
        if (type == null) return
        controller.changeDesireConcentration(
            compound,
            type,
            stockConcType,
            appModel.fromStock.value ?: false
        )
    }

    private fun changeStockConcentration(type: ConcentrationType?) {
        if (type == null) return
        controller.changeStockConcentration(
            compound,
            desiredConcType,
            type,
            appModel.fromStock.value ?: false
        )
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
        editText(R.id.stockConcEditText).hint = stockConcType.hint()
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

    }

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

        if ((appModel.fromStock.value == true) && stockConcEditText.text.toString().trim {
                it <= ' '
            }.isEmpty()
        ) {
            Anim().blink(stockConcEditText)
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
        if (appModel.fromStock.value == null) {
            return null
        }
        val stockConcEditText = editText(R.id.stockConcEditText)
        val stockConcentrationValue = parseDoubleFromEditText(stockConcEditText)
        return ConcentrationFactory.createConcentration(stockConcType, stockConcentrationValue)
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
        controller.toggleStock(
            compound = compound,
            desiredConcentrationType = desiredConcType,
            stockConcentrationType = stockConcType,
            wasStockOpen = appModel.fromStock.value ?: false
        )
    }

    private fun refreshFromStock(fromStock: Boolean) {
        for (stockView in stockViewsList) {
            val expandButton = imageButton(R.id.enableStockDilutionButton)
            val fromStockText = textView(R.id.from_stock)
            if (fromStock) {
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
