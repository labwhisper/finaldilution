package com.labwhisper.biotech.finaldilution.compound.view

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.solution.Solution

class CompoundListAdapter :
    RecyclerView.Adapter<CompoundListAdapter.CompoundViewHolder>() {

    var compoundList: MutableList<Compound> = mutableListOf()
    var compoundsInSolution: MutableList<Compound> = mutableListOf()
    var solution: Solution? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompoundViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.compound_list_item, parent, false)
        return CompoundViewHolder(itemView)
    }

    override fun getItemCount() = compoundList.size

    override fun onBindViewHolder(holder: CompoundViewHolder, position: Int) {
        val compound = compoundList[position]
        holder.nameView?.text = compound.displayName
        holder.massView?.text = compound.displayMass


        val color = if (compoundsInSolution.contains(compound))
            R.color.background_accent
        else
            R.color.transparent

        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, color)
        )
    }


    class CompoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameView: TextView? = null
        var massView: TextView? = null

        init {
            nameView = itemView.findViewById(R.id.compound_list_item_name_text_view)
            massView = itemView.findViewById(R.id.compound_list_item_mass_text_view)
        }

    }

}