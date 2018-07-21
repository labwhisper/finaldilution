package com.labessence.biotech.finaldilution.component.view

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.labessence.biotech.finaldilution.ApplicationContext
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.component.Component
import com.labessence.biotech.finaldilution.solution.view.EditActivity

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class ComponentsPanel(private val activity: EditActivity) {
    private val appState: ApplicationContext

    private val componentListAdapter: ChecklistAdapter
        get() {
            val components = appState.currentSolution?.components ?: ArrayList()
            return ChecklistAdapter(components)
        }

    init {
        appState = activity.applicationContext as ApplicationContext
    }

    fun displayComponentList() {
        val componentsListView = activity.findViewById<View>(R.id.componentsTextView) as ListView
        componentsListView.setOnItemClickListener(componentClick)
        componentsListView.setOnItemLongClickListener(componentLongClick)
        updateComponentList()
    }

    private val componentClick: (AdapterView<*>, View, Int, Long) -> Unit =
        onClick@{ parent, view, position, id ->
            val component = parent.adapter.getItem(position) as Component
            component.compound.let { activity.startComponentEdition(it) }
        }

    private val componentLongClick: (AdapterView<*>, View, Int, Long) -> Boolean =
        { parent, view, position, id ->
            val component = parent.adapter.getItem(position) as Component
            appState.currentSolution?.removeComponent(component)
            activity.refresh()
            true
        }

    fun updateComponentList() {
        val componentsListView = activity.findViewById<View>(R.id.componentsTextView) as ListView
        componentsListView.adapter = componentListAdapter
        updateOverflowState(componentsListView)
    }

    private fun updateOverflowState(componentsListView: ListView) {
        componentsListView.setBackgroundColor(Color.WHITE)
        appState.currentSolution?.let {
            if (it.isOverflown) highlightAllLiquidComponents(componentsListView)
        }
    }

    private fun highlightAllLiquidComponents(componentsListView: ListView) {
        appState.currentSolution?.let {
            for (component in it.components)
                if (component.fromStock) {
                    componentsListView.setBackgroundColor(Color.YELLOW)
                }
        }
    }


    private inner class ChecklistAdapter(private val componentList: ArrayList<Component>) :
        BaseAdapter() {

        override fun getCount(): Int {
            return componentList.size
        }

        override fun getItem(position: Int): Any {
            return componentList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertedView = convertView

            val holder: ViewHolder = if (convertedView != null) convertedView.tag as ViewHolder
            else {
                val vi = activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
                ) as LayoutInflater
                convertedView = vi.inflate(R.layout.checklist, null)

                val vh = ViewHolder()
                vh.compoundTextView =
                        convertedView.findViewById<View>(R.id.checklist_mainTextView) as TextView
                vh.percentageTextView =
                        convertedView.findViewById<View>(R.id.checklist_percentageTextView) as TextView
                vh.unitTextView =
                        convertedView.findViewById<View>(R.id.checklist_unitextView) as TextView
                vh.extraTextView =
                        convertedView.findViewById<View>(R.id.checklist_extraTextView) as TextView
                vh.checkBox = convertedView.findViewById<View>(R.id.checklist_checkBox1) as CheckBox
                convertedView.tag = vh
                vh

                //                vh.name.setOnClickListener(new View.OnClickListener() {
                //                    public void onClick(View view) {
                //                        CheckBox cb = (CheckBox) view;
                //                        Compound compound = (Compound) cb.getTag();
                //                        compound.setSelected(cb.isChecked());
                //                    }
                //                });
            }


            val appState = activity.applicationContext as ApplicationContext

            val solution = appState.currentSolution
            val component = getItem(position) as Component
            val compound = component.compound
            Log.d(TAG, "Solution: $solution")
            Log.d(TAG, "Component: $component")
            Log.d(TAG, "Component stock: " + component.availableConcentration)
            Log.d(TAG, "Compound: " + compound)
            holder.compoundTextView.text = component.compound.shortName
            if (solution != null) {
                holder.unitTextView.text = component.getAmountString(solution.volume)
            }
            if (component.fromStock) {
                holder.extraTextView.text = component.availableConcentration?.toString() ?: ""
            }
            holder.checkBox.isChecked = false
            holder.checkBox.tag = compound

            return convertedView!!

        }

        private inner class ViewHolder {
            internal lateinit var compoundTextView: TextView
            internal lateinit var percentageTextView: TextView
            internal lateinit var unitTextView: TextView
            internal lateinit var extraTextView: TextView
            internal lateinit var checkBox: CheckBox
        }

    }

    companion object {

        private val TAG = "Component Panel"
    }


}
