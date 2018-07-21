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
import com.labessence.biotech.finaldilution.component.concentration.Concentration
import com.labessence.biotech.finaldilution.component.concentration.ConcentrationFactory
import com.labessence.biotech.finaldilution.component.concentration.ConcentrationType
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.peripherals.view.Anim
import com.labessence.biotech.finaldilution.peripherals.view.StartupActivity
import com.labessence.biotech.finaldilution.solution.view.EditActivity
import java.util.*

class CompoundActivity : Activity() {

    internal lateinit var compound: Compound
    internal var desiredConcType: ConcentrationType = ConcentrationType.MOLAR
    internal var stockConcType: ConcentrationType? = null
    private lateinit var desiredViewsList: MutableList<View>
    private lateinit var stockViewsList: MutableList<View>
    private lateinit var desiredButtonList: MutableList<RadioButton>
    private lateinit var stockButtonList: MutableList<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_compound)

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

        compound = intent.getSerializableExtra("compound") as Compound
        title = "Add " + compound.shortName

        (findViewById<View>(R.id.desiredConcButtonsBar)).viewTreeObserver.addOnGlobalLayoutListener(
            { this.renderButtonsSquare() })

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
        val component = appState.currentSolution?.getComponentWithCompound(compound)
        if (component != null) {
            component.desiredConcentration?.let {
                desiredConcType = it.type
                val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
                desiredConcEditText.setText(java.lang.Double.toString(it.concentration))
            }
            if (component.fromStock) {
                component.availableConcentration?.let {
                    val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
                    stockConcType = it.type
                    stockConcEditText.setText(java.lang.Double.toString(it.concentration))
                }

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

        val appState = applicationContext as ApplicationContext

        if (appState.currentSolution == null) {
            val intent = Intent(this@CompoundActivity, StartupActivity::class.java)
            startActivity(intent)
        }

        val fromStock =
            (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked
        //TODO Add all checks!
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
        val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
        if (desiredConcEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
            Anim().blink(desiredConcEditText)
            return
        }

        if (fromStock && stockConcEditText.text.toString().trim { it <= ' ' }.length == 0) {
            Anim().blink(stockConcEditText)
            return
        }

        if (fromStock && stockConcType == null) {
            //TODO Add animation to buttons
            return
        }


        val component: Component = (appState.currentSolution?.getComponentWithCompound(compound)
                ?: createComponent()).also { updateComponent(it) }

        var currentComponentVolume = 0.0
        if (fromStock) {
            appState.currentSolution?.run {
                currentComponentVolume = component.getQuantity(volume)
            }
        }
        appState.currentSolution?.run {
            val allComponentsVolume = allLiquidComponentsVolume
            if (allComponentsVolume + currentComponentVolume > volume) {
                //TODO Color?? move this code
                //            appState.getDb().removeComponentFromCurrentSolution(component);
                //            appState.getDb().update(appState.getCurrentSolution());
                //            appState.getCurrentSolution().resetComponents();
            }
        }

        val intent = Intent(this@CompoundActivity, EditActivity::class.java)
        startActivity(intent)
        //TODO WHEN ITEM EXISTS SHOW STH? OR BEFORE EVEN OPENING COMP.ACTIVITY?

    }

    private fun onDeleteComponent() {

        val appState = applicationContext as ApplicationContext
        val component = appState.currentSolution?.getComponentWithCompound(compound)
        if (component == null) {
            onCancelComponent()
        } else {
            appState.currentSolution?.removeComponent(component)
            val intent = Intent(this@CompoundActivity, EditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createComponent(): Component {
        val appState = applicationContext as ApplicationContext
        val component = Component(compound, retrieveDesiredConc(), retrieveStockConc())

        appState.currentSolution?.addComponent(component)
        return component
    }

    private fun retrieveStockConc(): Concentration? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun retrieveDesiredConc(): Concentration {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun updateComponent(component: Component) {
        val desiredConcEditText = findViewById<View>(R.id.desiredConcEditText) as EditText
        val stockConcEditText = findViewById<View>(R.id.stockConcEditText) as EditText
        val fromStock =
            (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked

        val concentrationValue = parseDoubleFromEditText(desiredConcEditText)
        component.desiredConcentration =
                ConcentrationFactory.createConcentration(desiredConcType, concentrationValue)

        if (fromStock) {
            val stockConcentrationValue = parseDoubleFromEditText(stockConcEditText)
            stockConcType?.let {
                component.availableConcentration =
                        ConcentrationFactory.createConcentration(it, stockConcentrationValue)
            }
        }
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
        val fromStock =
            (findViewById<View>(R.id.enableStockDilutionButton) as ToggleButton).isChecked
        for (stockView in stockViewsList) {
            if (fromStock) {
                stockView.visibility = View.VISIBLE
            } else {
                stockView.visibility = View.INVISIBLE
            }
        }
    }


}
