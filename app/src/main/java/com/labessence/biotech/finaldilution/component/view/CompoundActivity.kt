package com.labessence.biotech.finaldilution.component.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ToggleButton
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.component.Component
import com.labessence.biotech.finaldilution.component.concentration.ConcentrationFactory
import com.labessence.biotech.finaldilution.component.concentration.ConcentrationType
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.peripherals.view.Anim
import com.labessence.biotech.finaldilution.solution.view.EditActivity
import java.util.*

class CompoundActivity : Activity() {

    internal lateinit var compound: Compound
    internal var desiredConcType: ConcentrationType? = ConcentrationType.MOLAR
    internal var stockConcType: ConcentrationType? = null
    private var desiredViewsList: MutableList<View>? = null
    private var stockViewsList: MutableList<View>? = null
    private var desiredButtonList: MutableList<RadioButton>? = null
    private var stockButtonList: MutableList<RadioButton>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_compound)

        desiredButtonList = ArrayList()
        desiredButtonList!!.add(findViewById<View>(R.id.desiredPercentageConcButton) as RadioButton)
        desiredButtonList!!.add(findViewById<View>(R.id.desiredMolarConcButton) as RadioButton)
        desiredButtonList!!.add(findViewById<View>(R.id.desiredMilimolarConcButton) as RadioButton)
        desiredButtonList!!.add(findViewById<View>(R.id.desiredMgMlConcButton) as RadioButton)

        stockButtonList = ArrayList()
        stockButtonList!!.add(findViewById<View>(R.id.stockPercentageConcButton) as RadioButton)
        stockButtonList!!.add(findViewById<View>(R.id.stockMolarConcButton) as RadioButton)
        stockButtonList!!.add(findViewById<View>(R.id.stockMilimolarConcButton) as RadioButton)
        stockButtonList!!.add(findViewById<View>(R.id.stockMgMlConcButton) as RadioButton)

        desiredViewsList = ArrayList()
        desiredViewsList!!.add(findViewById(R.id.desiredConcEditText) as View)
        desiredViewsList!!.add(findViewById(R.id.desiredConcButtonsBar) as View)
        desiredViewsList!!.add(findViewById(R.id.desiredConcTextView) as View)
        desiredViewsList!!.addAll(desiredButtonList!!)

        stockViewsList = ArrayList()
        stockViewsList!!.add(findViewById(R.id.stockConcEditText) as View)
        stockViewsList!!.add(findViewById(R.id.stockConcButtonsBar) as View)
        stockViewsList!!.add(findViewById(R.id.stockConcTextView) as View)
        stockViewsList!!.addAll(stockButtonList!!)

        compound = intent.getSerializableExtra("compound") as Compound
        title = "Add " + compound.shortName

        (findViewById(R.id.desiredConcButtonsBar) as View).viewTreeObserver.addOnGlobalLayoutListener({ this.renderButtonsSquare() })

        setKeyboardOnInputs()
        bindListeners()
        setConcentrationButtonsState(false)
        fillComponentFields()
        toggleSolutionFromStock()
    }

    private fun bindListeners() {
        findViewById<View>(R.id.desiredPercentageConcButton).setOnClickListener { v ->
            desiredConcType = ConcentrationType.PERCENTAGE
            (findViewById<View>(R.id.desiredConcEditText) as EditText).hint = "%"
        }

        findViewById<View>(R.id.desiredMolarConcButton).setOnClickListener { v ->
            desiredConcType = ConcentrationType.MOLAR
            (findViewById<View>(R.id.desiredConcEditText) as EditText).hint = "M/l"
        }

        findViewById<View>(R.id.desiredMilimolarConcButton).setOnClickListener { v ->
            desiredConcType = ConcentrationType.MILIMOLAR
            (findViewById<View>(R.id.desiredConcEditText) as EditText).hint = "mM/l"
        }

        findViewById<View>(R.id.desiredMgMlConcButton).setOnClickListener { v ->
            desiredConcType = ConcentrationType.MILIGRAM_PER_MILLILITER
            (findViewById<View>(R.id.desiredConcEditText) as EditText).hint = "mg/ml"
        }

        findViewById<View>(R.id.stockPercentageConcButton).setOnClickListener { v ->
            stockConcType = ConcentrationType.PERCENTAGE
            (findViewById<View>(R.id.stockConcEditText) as EditText).hint = "%"
        }

        findViewById<View>(R.id.stockMolarConcButton).setOnClickListener { v ->
            stockConcType = ConcentrationType.MOLAR
            (findViewById<View>(R.id.stockConcEditText) as EditText).hint = "M/l"
        }

        findViewById<View>(R.id.stockMilimolarConcButton).setOnClickListener { v ->
            stockConcType = ConcentrationType.MILIMOLAR
            (findViewById<View>(R.id.stockConcEditText) as EditText).hint = "mM/l"
        }

        findViewById<View>(R.id.stockMgMlConcButton).setOnClickListener { v ->
            stockConcType = ConcentrationType.MILIGRAM_PER_MILLILITER
            (findViewById<View>(R.id.stockConcEditText) as EditText).hint = "mg/ml"
        }

        findViewById<View>(R.id.enableStockDilutionButton).setOnClickListener { toggleSolutionFromStock() }

        findViewById<View>(R.id.buttonAddCompoundDone).setOnClickListener { onAcceptComponent() }

        findViewById<View>(R.id.buttonAddCompoundCancel).setOnClickListener { onCancelComponent() }

        findViewById<View>(R.id.buttonAddCompoundDelete).setOnClickListener { onDeleteComponent() }

    }

    private fun fillComponentFields() {
        val appState = applicationContext as ApplicationContext
        val component = appState.currentSolution.getComponentWithCompound(compound)
        if (component != null) {
            desiredConcType = component.desiredConcentration!!.type
            val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
            desiredConcEditText.setText(java.lang.Double.toString(component.desiredConcentration!!.concentration))
            if (component.fromStock) {
                val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
                stockConcType = component.availableConcentration!!.type
                stockConcEditText.setText(java.lang.Double.toString(component.availableConcentration!!.concentration))

            }
            setConcentrationButtonsState(component.fromStock)
        }
    }

    private fun setConcentrationButtonsState(fromStock: Boolean) {
        (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked = fromStock
        val desiredRadioGroup = findViewById<View>(R.id.desiredConcButtonsBar) as RadioGroup
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

    }

    private fun onCancelComponent() {
        val intent = Intent(this@CompoundActivity, EditActivity::class.java)
        startActivity(intent)
    }

    //TODO run this listener also on back and generally close?
    private fun onAcceptComponent() {
        val fromStock = (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked
        //TODO Add all checks!
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
        val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
        if (desiredConcEditText.text.toString().trim { it <= ' ' }.length == 0) {
            Anim().blink(desiredConcEditText)
            return
        }

        if (fromStock && stockConcEditText.text.toString().trim { it <= ' ' }.length == 0) {
            Anim().blink(stockConcEditText)
            return
        }

        if (desiredConcType == null) {
            //TODO Add animation to buttons
            return
        }

        if (fromStock && stockConcType == null) {
            //TODO Add animation to buttons
            return
        }


        val appState = applicationContext as ApplicationContext
        val currentSolution = appState.currentSolution

        var component = currentSolution.getComponentWithCompound(compound)

        if (component != null) {
            updateComponent(component)

        } else {
            component = createComponent()
        }

        val allComponentsVolume = currentSolution.allLiquidComponentsVolume
        var currentComponentVolume = 0.0
        if (fromStock) {
            currentComponentVolume = component.getQuantity(currentSolution.volume)
        }
        if (allComponentsVolume + currentComponentVolume > currentSolution.volume) {
            //TODO Color?? move this code
            //            appState.getDb().removeComponentFromCurrentSolution(component);
            //            appState.getDb().update(appState.getCurrentSolution());
            //            appState.getCurrentSolution().resetComponents();
        }

        val intent = Intent(this@CompoundActivity, EditActivity::class.java)
        startActivity(intent)
        //TODO WHEN ITEM EXISTS SHOW STH? OR BEFORE EVEN OPENING COMP.ACTIVITY?

    }

    private fun onDeleteComponent() {

        val appState = applicationContext as ApplicationContext
        val component = appState.currentSolution.getComponentWithCompound(compound)
        if (component == null) {
            onCancelComponent()
        } else {
            appState.currentSolution.removeComponent(component)
            val intent = Intent(this@CompoundActivity, EditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createComponent(): Component {
        val appState = applicationContext as ApplicationContext
        val component = Component(appState.currentSolution.volume, compound)

        updateComponent(component)

        val solution = appState.currentSolution
        solution.addComponent(component)
        return component
    }

    private fun updateComponent(component: Component) {
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
        val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
        val fromStock = (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked

        val concentrationValue = parseDoubleFromEditText(desiredConcEditText)
        component.desiredConcentration = ConcentrationFactory.createConcentration(desiredConcType!!, concentrationValue)

        if (fromStock) {
            val stockConcentrationValue = parseDoubleFromEditText(stockConcEditText)
            component.availableConcentration = ConcentrationFactory.createConcentration(stockConcType!!, stockConcentrationValue)
        }
        component.fromStock = fromStock
    }

    private fun parseDoubleFromEditText(desiredConcEditText: EditText): Double {
        return java.lang.Double.parseDouble(
                desiredConcEditText.text.toString().replace(',', '.'))
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
        val fromStock = (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked
        for (stockView in stockViewsList!!) {
            if (fromStock) {
                stockView.visibility = View.VISIBLE
            } else {
                stockView.visibility = View.INVISIBLE
            }
        }
    }


}
