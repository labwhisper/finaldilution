package com.labwhisper.biotech.finaldilution.component.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.component.appmodel.ComponentEditAppModel
import com.labwhisper.biotech.finaldilution.component.concentration.Concentration
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationFactory
import com.labwhisper.biotech.finaldilution.component.concentration.ConcentrationType
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.NoMolarMassException
import com.labwhisper.biotech.finaldilution.genericitem.putExtraAnItem
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.SolutionCareTaker
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity
import com.labwhisper.biotech.finaldilution.util.imageButton
import com.labwhisper.biotech.finaldilution.util.textView
import java.util.*

class EditComponentActivity : Activity() {

    private lateinit var solution: Solution
    internal lateinit var compound: Compound
    private var desiredConcType: ConcentrationType = ConcentrationType.MOLAR
    private var stockConcType: ConcentrationType? = null
    private lateinit var desiredViewsList: MutableList<View>
    private lateinit var stockViewsList: MutableList<View>
    private lateinit var desiredButtonList: MutableList<RadioButton>
    private lateinit var stockButtonList: MutableList<RadioButton>
    private lateinit var solutionCareTaker: SolutionCareTaker

    val appModel = ComponentEditAppModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.component_edit)

        // TODO use getChildAt instead of creaing such lists

        desiredButtonList = ArrayList()
        desiredButtonList.add(findViewById<View>(R.id.desiredPercentageConcButton) as RadioButton)
        desiredButtonList.add(findViewById<View>(R.id.desiredMolarConcButton) as RadioButton)
        desiredButtonList.add(findViewById<View>(R.id.desiredMilimolarConcButton) as RadioButton)
        desiredButtonList.add(findViewById<View>(R.id.desiredMgMlConcButton) as RadioButton)

        stockButtonList = ArrayList()
        stockButtonList.add(findViewById<View>(R.id.stockPercentageConcButton) as RadioButton)
        stockButtonList.add(findViewById<View>(R.id.stockMolarConcButton) as RadioButton)
        stockButtonList.add(findViewById<View>(R.id.stockMilimolarConcButton) as RadioButton)
        stockButtonList.add(findViewById<View>(R.id.stockMgMlConcButton) as RadioButton)

        desiredViewsList = ArrayList()
        desiredViewsList.add(findViewById<View>(R.id.desiredConcEditText))
        desiredViewsList.add(findViewById<View>(R.id.desiredConcButtonsBar))
        desiredViewsList.add(findViewById<View>(R.id.desiredConcTextView))
        desiredViewsList.addAll(desiredButtonList)

        stockViewsList = ArrayList()
        stockViewsList.add(findViewById<View>(R.id.stockConcEditText))
        stockViewsList.add(findViewById<View>(R.id.stockConcButtonsBar))
        stockViewsList.add(findViewById<View>(R.id.stockConcTextView))
        stockViewsList.addAll(stockButtonList)

        compound = intent.getSerializableExtra("COMPOUND") as Compound
        solution = intent.getSerializableExtra("SOLUTION") as Solution
        solutionCareTaker = intent.getSerializableExtra("CARE_TAKER") as SolutionCareTaker
        title = compound.displayName
        findViewById<TextView>(R.id.new_component_title).text = title

//        (findViewById<View>(R.id.desiredConcButtonsBar)).viewTreeObserver
//            .addOnGlobalLayoutListener { this.renderButtonsSquare() }
        setKeyboardOnInputs()
        bindListeners()
        setConcentrationButtonsState(false, compound.molarMass)
        fillComponentFields()
        refreshFromStock()
    }


    private fun bindListeners() {

        for ((i, concentrationButton) in desiredButtonList.withIndex()) {
            concentrationButton.setOnClickListener {
                desiredConcType = (ConcentrationType.fromInt(i) ?: ConcentrationType.MOLAR).apply {
                    (findViewById<View>(R.id.desiredConcEditText) as EditText).hint = hint()
                }
            }
        }

        for ((i, concentrationButton) in stockButtonList.withIndex()) {
            concentrationButton.setOnClickListener {
                stockConcType = (ConcentrationType.fromInt(i) ?: ConcentrationType.MOLAR).apply {
                    (findViewById<View>(R.id.stockConcEditText) as EditText).hint = hint()
                }
            }
        }

        findViewById<View>(R.id.enableStockDilutionButton).setOnClickListener { toggleSolutionFromStock() }

        findViewById<View>(R.id.buttonAddCompoundDone).setOnClickListener { onAcceptComponent() }

        findViewById<View>(R.id.buttonAddCompoundCancel).setOnClickListener { onCancelComponent() }

        findViewById<View>(R.id.buttonAddCompoundDelete).setOnClickListener { onDeleteComponent() }

    }

    private fun fillComponentFields() {
        val component = solution.getComponentWithCompound(compound)
            ?: return
        desiredConcType = component.desiredConcentration.type
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
        desiredConcEditText.setText(java.lang.Double.toString(component.desiredConcentration.concentration))

        if (component.fromStock) {
            component.availableConcentration?.let {
                val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
                stockConcType = it.type
                stockConcEditText.setText(java.lang.Double.toString(it.concentration))
            }

        }
        setConcentrationButtonsState(component.fromStock, component.compound.molarMass)
    }

    private fun setConcentrationButtonsState(fromStock: Boolean, molarMass: Double?) {
        appModel.fromStock = fromStock
        val desiredRadioGroup = findViewById<View>(R.id.desiredConcButtonsBar) as RadioGroup
        if (molarMass == null && (desiredConcType == ConcentrationType.MOLAR)) {
            desiredConcType = ConcentrationType.PERCENTAGE
        }
        when (desiredConcType) {
            ConcentrationType.PERCENTAGE -> desiredRadioGroup.check(R.id.desiredPercentageConcButton)
            ConcentrationType.MOLAR -> desiredRadioGroup.check(R.id.desiredMolarConcButton)
            ConcentrationType.MILIMOLAR -> desiredRadioGroup.check(R.id.desiredMilimolarConcButton)
            ConcentrationType.MILIGRAM_PER_MILLILITER -> desiredRadioGroup.check(R.id.desiredMgMlConcButton)
        }

        if (fromStock) {
            val stockRadioGroup = findViewById<View>(R.id.stockConcButtonsBar) as RadioGroup
            when (stockConcType) {
                ConcentrationType.PERCENTAGE -> stockRadioGroup.check(R.id.stockPercentageConcButton)
                ConcentrationType.MOLAR -> stockRadioGroup.check(R.id.stockMolarConcButton)
                ConcentrationType.MILIMOLAR -> stockRadioGroup.check(R.id.stockMilimolarConcButton)
                ConcentrationType.MILIGRAM_PER_MILLILITER -> stockRadioGroup.check(R.id.stockMgMlConcButton)
            }
        }

        val molarEnabled = molarMass != null
        findViewById<Button>(R.id.desiredMolarConcButton).isEnabled = molarEnabled
        findViewById<Button>(R.id.desiredMilimolarConcButton).isEnabled = molarEnabled
        findViewById<Button>(R.id.stockMolarConcButton).isEnabled = molarEnabled
        findViewById<Button>(R.id.stockMilimolarConcButton).isEnabled = molarEnabled
    }

    private fun onCancelComponent() {
        onBackPressed()
    }

    //TODO run this listener also on back and generally close?
    private fun onAcceptComponent() {

        //TODO Add all checks!
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
        val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
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


        val component: Component =
            (solution.getComponentWithCompound(compound)?.also {
                updateComponent(it)
            } ?: createComponent())

        var currentComponentVolume = 0.0
        if (appModel.fromStock) {
            currentComponentVolume = try {
                component.getQuantity(solution.volume)
            } catch (e: NoMolarMassException) {
                0.0
            }
        }

        val intent = Intent(this@EditComponentActivity, EditActivity::class.java)
        intent.putExtraAnItem(solution)
        intent.putExtra("CARE_TAKER", solutionCareTaker)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        //TODO WHEN ITEM EXISTS SHOW STH? OR BEFORE EVEN OPENING COMP.ACTIVITY?

    }

    private fun onDeleteComponent() {

        val component = solution.getComponentWithCompound(compound)
        if (component == null) {
            onCancelComponent()
        } else {
            solution.removeComponent(component)
            val intent = Intent(this@EditComponentActivity, EditActivity::class.java)
            intent.putExtraAnItem(solution)
            intent.putExtra("CARE_TAKER", solutionCareTaker)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
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
        val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
        val stockConcentrationValue = parseDoubleFromEditText(stockConcEditText)
        stockConcType?.let {
            return ConcentrationFactory.createConcentration(it, stockConcentrationValue)
        }
        return null
    }

    private fun retrieveDesiredConcFromInput(): Concentration {
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
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
        concEditTexts.add(findViewById<View>(R.id.desiredConcEditText) as EditText)
        concEditTexts.add(findViewById<View>(R.id.stockConcEditText) as EditText)
        for (editText in concEditTexts) {

            editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            editText.setRawInputType(Configuration.KEYBOARD_12KEY)
            editText.setOnEditorActionListener { v, keyCode, event ->
                //TODO: Check this code for different versions of Android
                if (keyCode == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    // hide virtual keyboard
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun renderButtonsSquare() {
        val buttonsBar = findViewById<View>(R.id.desiredConcButtonsBar) as RadioGroup
        buttonsBar.layoutParams.height = buttonsBar.width / 4

        val stockButtonsBar = findViewById<View>(R.id.stockConcButtonsBar) as RadioGroup
        stockButtonsBar.layoutParams.height = buttonsBar.width / 4
    }

    private fun toggleSolutionFromStock() {
        appModel.fromStock = !appModel.fromStock
        refreshFromStock()
    }

    private fun refreshFromStock() {
        for (stockView in stockViewsList) {
            val expandButton = imageButton(R.id.enableStockDilutionButton)
            val fromStockText = textView(R.id.from_stock)
            if (appModel.fromStock) {
                stockView.visibility = View.VISIBLE
                expandButton.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_expand_less_black_24dp)
                )
                fromStockText?.visibility = View.INVISIBLE
            } else {
                stockView.visibility = View.INVISIBLE
                expandButton.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_expand_more_black_24dp)
                )
                fromStockText?.visibility = View.VISIBLE
            }

        }
    }


}
