package com.example.techaudit20.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.databinding.ItemLaboratorioBinding
import com.example.techaudit20.model.Laboratorio

class LabAdapter(
    private var lista: List<Laboratorio>,
    private val onLabClick: (Laboratorio) -> Unit
) : RecyclerView.Adapter<LabAdapter.LabViewHolder>() {

    inner class LabViewHolder(val binding: ItemLaboratorioBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val binding = ItemLaboratorioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val lab = lista[position]
        holder.binding.tvNombreLab.text = lab.nombre
        holder.binding.tvEdificio.text = "Edificio: ${lab.edificio}"

        holder.itemView.setOnClickListener { onLabClick(lab) }
    }

    override fun getItemCount(): Int = lista.size

    fun updateList(newList: List<Laboratorio>) {
        lista = newList
        notifyDataSetChanged()
    }
}