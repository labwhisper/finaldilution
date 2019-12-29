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

class ChecklistAdapter(
    private var solution: Solution
) : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    var overflown: Boolean = false

    var onClickListener: ((Component) -> Unit)? = null
    var onLongClickListener: ((Component) -> Boolean)? = null

    fun updateComponents(solution: Solution) {
        this.solution = solution
        this.notifyDataSetChanged()
    }

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
            holder.extraTextView?.visibility = View.GONE
            holder.unitTextView?.visibility = View.GONE
            holder.checkBox?.isChecked = solution.isFilledInWithWater

            holder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
                solution.isFilledInWithWater = isChecked
                setWellDoneIfAllChecked(holder)
            }
            return
        }

        val component = solution.components[holder.adapterPosition]
        val compound = component.compound
        holder.compoundTextView?.text = compound.displayName
        holder.unitTextView?.visibility = View.VISIBLE
        holder.unitTextView?.text =
            component.getAmountStringForVolume(solution.volume)
        if (component.fromStock) {
            holder.extraTextView?.visibility = View.VISIBLE
            holder.extraTextView?.text = component.availableConcentration?.toString() ?: ""
        } else {
            holder.extraTextView?.visibility = View.GONE
        }
        holder.checkBox?.isChecked = solution.componentsAdded.contains(component)
        holder.checkBox?.tag = compound

        if (overflown && (compound.liquid || component.fromStock)) {
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
        var unitTextView: TextView? = null
        var extraTextView: TextView? = null
        var checkBox: CheckBox? = null

        init {
            compoundTextView = itemView.textView(R.id.checklist_component)
            unitTextView = itemView.textView(R.id.checklist_amount)
            extraTextView = itemView.textView(R.id.checklist_extraTextView)
            checkBox = itemView.checkBox(R.id.checklist_checkBox1)
        }
    }
}