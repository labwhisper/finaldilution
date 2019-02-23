package com.labessence.biotech.finaldilution.compound.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.labessence.biotech.finaldilution.R
import com.labessence.biotech.finaldilution.compound.Compound

class CompoundListAdapter :
    RecyclerView.Adapter<CompoundListAdapter.CompoundViewHolder>() {

    var compoundList: MutableList<Compound> = mutableListOf()

    class CompoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameView: TextView? = null
        var massView: TextView? = null

        init {
            nameView = itemView.findViewById(R.id.compound_list_item_name_text_view)
            massView = itemView.findViewById(R.id.compound_list_item_mass_text_view)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompoundViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.compound_list_item, parent, false)
        return CompoundViewHolder(itemView)
    }

    override fun getItemCount() = compoundList.size

    override fun onBindViewHolder(holder: CompoundViewHolder, position: Int) {
        val compound = compoundList.get(position)
        holder.nameView?.text = compound.displayName
        holder.massView?.text = compound.displayMass
    }


}