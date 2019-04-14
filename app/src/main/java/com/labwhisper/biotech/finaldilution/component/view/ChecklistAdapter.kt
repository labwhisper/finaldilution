package com.labwhisper.biotech.finaldilution.component.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component

class ChecklistAdapter(
    private var componentList: MutableList<Component>,
    private val componentsPanel: ComponentsPanel
) : BaseAdapter() {

    var overflown: Boolean = false

    fun updateComponents(components: MutableList<Component>) {
        componentList = components
        this.notifyDataSetChanged()
    }

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
            val vi = componentsPanel.activity.getSystemService(
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
        }

        if (position >= componentList.size) {
            holder.compoundTextView.text =
                "Fill up to ${componentsPanel.activity.solution.displayVolume()}"
            holder.extraTextView.visibility = View.GONE
            holder.unitTextView.visibility = View.GONE
            holder.checkBox.isChecked = false
            return convertedView!!
        }

        val component = getItem(position) as Component
        val compound = component.compound
        holder.compoundTextView.text = component.compound.displayName
        holder.unitTextView.text =
            component.getAmountStringForVolume(componentsPanel.activity.solution.volume)
        if (component.fromStock) {
            holder.extraTextView.visibility = View.VISIBLE
            holder.extraTextView.text = component.availableConcentration?.toString() ?: ""
        } else {
            holder.extraTextView.visibility = View.GONE
        }
        holder.checkBox.isChecked = false
        holder.checkBox.tag = compound

        if (overflown && (compound.liquid || component.fromStock)) {
            (convertedView as ConstraintLayout).setBackgroundColor(
                ContextCompat.getColor(convertedView.context, R.color.background_error)
            )
        } else {
            (convertedView as ConstraintLayout).setBackgroundColor(
                ContextCompat.getColor(convertedView.context, R.color.transparent)
            )
        }

        return convertedView

    }

    private inner class ViewHolder {
        internal lateinit var compoundTextView: TextView
        internal lateinit var unitTextView: TextView
        internal lateinit var extraTextView: TextView
        internal lateinit var checkBox: CheckBox
    }

}