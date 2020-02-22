package com.labwhisper.biotech.finaldilution.component.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.component.Component
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.util.checkBox
import com.labwhisper.biotech.finaldilution.util.imageView
import com.labwhisper.biotech.finaldilution.util.textView

class ChecklistAdapter : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    lateinit var solution: Solution
    var onClickListener: ((Component) -> Unit)? = null
    var onLongClickListener: ((Component) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.checklist, parent, false)
        return ChecklistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return solution.components.size + 1
    }

    override fun onBindViewHolder(
        holder: ChecklistViewHolder,
        position: Int
    ) {

        if (holder.adapterPosition >= solution.components.size) {
            holder.compoundTextView?.text =
                "Fill up to ${solution.displayVolume()}"
            holder.stockTextView?.visibility = View.GONE
            holder.amountTextView?.visibility = View.GONE
            holder.addDensityTextView?.visibility = View.GONE
            holder.checkBox?.isChecked = solution.isFilledInWithWater


            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.transparent)
            )

            holder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
                solution.isFilledInWithWater = isChecked
                setWellDoneIfAllChecked(holder)
            }
            return
        }

        val component = solution.components[holder.adapterPosition]
        val compound = component.compound
        holder.compoundTextView?.text =
            "${compound.displayName} ${component.availableConcentration?.toString() ?: ""}"
        holder.amountTextView?.visibility = View.VISIBLE
        holder.amountTextView?.text =
            component.getAmountStringForVolume(solution.volume)
        holder.stockTextView?.visibility = View.VISIBLE
        holder.stockTextView?.text = component.desiredConcentration.toString()
        holder.addDensityTextView?.visibility =
            if (component.noVolumeBecauseOfNoDensity) View.VISIBLE else View.GONE
        holder.checkBox?.isChecked = solution.componentsAdded.contains(component)
        holder.checkBox?.tag = compound

        if (solution.isOverflown && (compound.liquid || component.fromStock)) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.background_error)
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.transparent)
            )
        }

        holder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //TODO Write code for added components using unit TDD
                solution.componentsAdded.add(component)
            } else {
                solution.componentsAdded.remove(component)
            }
            setWellDoneIfAllChecked(holder)
        }

        holder.itemView.setOnClickListener { onClickListener?.invoke(component) }
        holder.itemView.setOnLongClickListener { onLongClickListener?.invoke(component) ?: false }
    }

    private fun setWellDoneIfAllChecked(
        holder: ChecklistViewHolder
    ) {
        if (solution.done) {
            val activity = holder.itemView.context
            if (activity is Activity) {
                Anim().animWellDone(activity.imageView(R.id.well_done))
            }
        }
    }

    class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var compoundTextView: TextView? = null
        var amountTextView: TextView? = null
        var stockTextView: TextView? = null
        var checkBox: CheckBox? = null
        var addDensityTextView: TextView? = null

        init {
            compoundTextView = itemView.textView(R.id.checklist_compound)
            amountTextView = itemView.textView(R.id.checklist_amount)
            stockTextView = itemView.textView(R.id.checklist_stock)
            checkBox = itemView.checkBox(R.id.checklist_checkBox1)
            addDensityTextView = itemView.textView(R.id.checklist_no_density)
        }
    }
}