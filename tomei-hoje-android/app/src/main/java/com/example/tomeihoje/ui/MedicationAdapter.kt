package com.example.tomeihoje.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tomeihoje.data.Medication
import com.example.tomeihoje.databinding.ListItemMedicationBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter para o RecyclerView que exibe a lista de medicamentos.
 * 
 * Utiliza ListAdapter com DiffUtil para atualizações eficientes da lista,
 * calculando automaticamente as diferenças entre listas antigas e novas.
 * Implementa ViewBinding para acesso type-safe às views do layout.
 * 
 * Funcionalidades principais:
 * - Exibe nome do medicamento
 * - CheckBox para marcar se foi tomado hoje (estado salvo em SharedPreferences)
 * - Botão de exclusão para remover medicamentos
 * 
 * @param context Contexto necessário para SharedPreferences e inflação de layouts
 * @param onDeleteClick Callback executado quando o botão de exclusão é clicado
 */
class MedicationAdapter(
    private val context: Context,
    private val onDeleteClick: (Medication) -> Unit
) : ListAdapter<Medication, MedicationAdapter.MedicationViewHolder>(DIFF_CALLBACK) {
    
    companion object {
        /**
         * DiffUtil.ItemCallback para calcular diferenças entre itens da lista.
         * Permite atualizações eficientes do RecyclerView, animando apenas
         * os itens que realmente mudaram.
         */
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Medication>() {
            /**
             * Verifica se dois itens representam o mesmo medicamento.
             * Usado para determinar se um item foi movido na lista.
             */
            override fun areItemsTheSame(oldItem: Medication, newItem: Medication): Boolean {
                return oldItem.id == newItem.id
            }
            
            /**
             * Verifica se o conteúdo de dois itens é idêntico.
             * Usado para determinar se um item precisa ser atualizado.
             */
            override fun areContentsTheSame(oldItem: Medication, newItem: Medication): Boolean {
                return oldItem == newItem
            }
        }
    }
    
    /**
     * Cria um novo ViewHolder quando necessário.
     * Infla o layout do item e cria o ViewHolder com ViewBinding.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val binding = ListItemMedicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MedicationViewHolder(binding)
    }
    
    /**
     * Vincula os dados do medicamento ao ViewHolder.
     * Chamado para cada item visível na lista.
     */
    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = getItem(position)
        holder.bind(medication)
    }
    
    /**
     * ViewHolder que mantém as referências das views de cada item da lista.
     * Utiliza ViewBinding para acesso type-safe às views.
     */
    inner class MedicationViewHolder(
        private val binding: ListItemMedicationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        /**
         * Vincula os dados do medicamento às views do item.
         * 
         * Configura:
         * - Nome do medicamento
         * - Estado do CheckBox baseado em SharedPreferences (por data)
         * - Listener para salvar estado do CheckBox
         * - Listener para botão de exclusão
         * 
         * @param medication Dados do medicamento a serem exibidos
         */
        fun bind(medication: Medication) {
            // Define o nome do medicamento
            binding.medicationName.text = medication.name
            
            // Gera chave única para SharedPreferences baseada no ID e data atual
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(Date())
            val preferenceKey = "status_${medication.id}_$today"
            
            // Obtém SharedPreferences para armazenar estado diário dos medicamentos
            val sharedPreferences = context.getSharedPreferences(
                "medication_prefs", 
                Context.MODE_PRIVATE
            )
            
            // Remove listener temporariamente para evitar chamadas desnecessárias
            binding.medicationCheckbox.setOnCheckedChangeListener(null)
            
            // Define o estado do CheckBox baseado no valor salvo (padrão: false)
            binding.medicationCheckbox.isChecked = sharedPreferences.getBoolean(
                preferenceKey, 
                false
            )
            
            // Configura listener para salvar estado quando CheckBox for alterado
            binding.medicationCheckbox.setOnCheckedChangeListener { _, isChecked ->
                // Salva o novo estado no SharedPreferences
                sharedPreferences.edit()
                    .putBoolean(preferenceKey, isChecked)
                    .apply()
            }
            
            // Configura listener para o botão de exclusão
            binding.deleteButton.setOnClickListener {
                onDeleteClick(medication)
            }
        }
    }
}
