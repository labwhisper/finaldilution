package com.labwhisper.biotech.finaldilution.component.view

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.solution.view.EditActivity

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */

class ComponentsPanel(private val activity: EditActivity) {

    private val componentListAdapter: ChecklistAdapter
        get() {
            val components = activity.solution.components
            return ChecklistAdapter(components)
        }

    fun displayComponentList() {
        val componentsListView = activity.findViewById<View>(R.id.componentsTextView) as ListView
        componentsListView.setOnItemClickListener(componentClick)
        componentsListView.setOnItemLongClickListener(componentLongClick)
        updateComponentList()
    }

    private val componentClick: (AdapterView<*>, View, Int, Long) -> Unit =
        onClick@{ parent, _, position, _ ->
            val component = parent.adapter.getItem(position)
            if (!(component is Component))
                return@onClick
            component.compound.let { activity.startComponentEdition(it) }
        }

    private val componentLongClick: (AdapterView<*>, View, Int, Long) -> Boolean =
        { parent, _, position, _ ->
            val component = parent.adapter.getItem(position) as Component
            activity.solution.removeComponent(component)
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
        if (activity.solution.isOverflown) highlightAllLiquidComponents(componentsListView)
    }

    private fun highlightAllLiquidComponents(componentsListView: ListView) {
        for (component in activity.solution.components)
            if (component.fromStock) {
                componentsListView.setBackgroundColor(Color.YELLOW)
            }
    }


    private inner class ChecklistAdapter(private val componentList: MutableList<Component>) :
        BaseAdapter() {

        override fun getCount(): Int {
            return componentList.size + 1
        }

        override fun getItem(position: Int): Any {
            if (position >= componentList.size) {
                return "Fill"
            }
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
                    convertedView.findViewById<View>(R.id.checklist_component) as TextView
                vh.unitTextView =
                    convertedView.findViewById<View>(R.id.checklist_amount) as TextView
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

            if (position >= componentList.size) {
                holder.compoundTextView.text = "Fill up to ${activity.solution.displayVolume()}"
                holder.extraTextView.visibility = View.GONE
                holder.unitTextView.visibility = View.GONE
                holder.checkBox.isChecked = false
                return convertedView!!
            }

            val component = getItem(position) as Component
            val compound = component.compound
            Log.d(TAG, "Solution: ${activity.solution}")
            Log.d(TAG, "Component: $component")
            Log.d(TAG, "Component stock: " + component.availableConcentration)
            Log.d(TAG, "Compound: " + compound)
            holder.compoundTextView.text = component.compound.displayName
//            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
//                holder.compoundTextView, 12, 20, 1, TypedValue.COMPLEX_UNIT_SP
//            )
            holder.unitTextView.text =
                component.getAmountStringForVolume(activity.solution.volume)
            if (component.fromStock) {
                holder.extraTextView.visibility = View.VISIBLE
                holder.extraTextView.text = component.availableConcentration?.toString() ?: ""
            } else {
                holder.extraTextView.visibility = View.GONE
            }
            holder.checkBox.isChecked = false
            holder.checkBox.tag = compound

            return convertedView!!

        }

        private inner class ViewHolder {
            internal lateinit var compoundTextView: TextView
            internal lateinit var unitTextView: TextView
            internal lateinit var extraTextView: TextView
            internal lateinit var checkBox: CheckBox
        }

    }

    companion object {

        private val TAG = "Component Panel"
    }


}
