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
import com.labwhisper.biotech.finaldilution.component.ComponentQuantityCalculator
import com.labwhisper.biotech.finaldilution.component.concentration.CongruentConcentrationsInteractor
import com.labwhisper.biotech.finaldilution.peripherals.view.Anim
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.SolutionVolumeCalculator
import com.labwhisper.biotech.finaldilution.util.checkBox
import com.labwhisper.biotech.finaldilution.util.imageView
import com.labwhisper.biotech.finaldilution.util.textView

class ChecklistAdapter : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    lateinit var solution: Solution
    var onClickListener: ((Component) -> Unit)? = null
    var onLongClickListener: ((Component) -> Boolean)? = null
    var solutionUpdate: ((Solution) -> Unit)? = null

    val congruentConcentrationsInteractor = CongruentConcentrationsInteractor()
    private val componentQuantityCalculator =
        ComponentQuantityCalculator(congruentConcentrationsInteractor)
    private val solutionVolumeCalculator = SolutionVolumeCalculator(componentQuantityCalculator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.checklist, parent, false)
        return ChecklistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (::solution.isInitialized) solution.components.size + 1 else 0
    }

    override fun onBindViewHolder(
        holder: ChecklistViewHolder,
        position: Int
    ) {

        if (holder.adapterPosition >= solution.components.size) {
            holder.compoundTextView?.text =
                holder.itemView.resources.getString(
                    R.string.fill_up_to_quantity,
                    solution.displayVolume()
                )
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
            componentQuantityCalculator.getAmountStringForVolume(component, solution.volume)
        holder.stockTextView?.visibility = View.VISIBLE
        holder.stockTextView?.text = component.desiredConcentration.toString()
        holder.addDensityTextView?.visibility =
            if (componentQuantityCalculator.noVolumeBecauseOfNoDensity(component)) View.VISIBLE
            else View.GONE

        if (solutionVolumeCalculator.isOverflown(solution)
            && (compound.liquid || component.fromStock)
        ) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.background_error)
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.transparent)
            )
        }

        holder.checkBox?.setOnCheckedChangeListener(null)
        holder.checkBox?.isChecked = solution.componentsAdded.contains(component)
        holder.checkBox?.tag = compound
        holder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
            val updatedSolution = solution.deepCopy()
            if (isChecked) {
                //TODO Write code for added components using unit TDD
                updatedSolution.componentsAdded.add(component)
            } else {
                updatedSolution.componentsAdded.remove(component)
            }
            solutionUpdate?.invoke(updatedSolution)
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