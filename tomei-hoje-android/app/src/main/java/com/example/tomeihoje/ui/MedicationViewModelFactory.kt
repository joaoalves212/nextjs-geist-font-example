package com.example.tomeihoje.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tomeihoje.data.MedicationDao

/**
 * Factory para criar instâncias do MedicationViewModel com injeção de dependências.
 * 
 * O ViewModelProvider.Factory é necessário quando o ViewModel possui parâmetros
 * no construtor (neste caso, o MedicationDao). Esta factory permite que o
 * ViewModelProvider crie corretamente o ViewModel com as dependências necessárias.
 * 
 * Esta abordagem segue o padrão de Injeção de Dependências, permitindo que
 * o ViewModel receba suas dependências de forma controlada e testável.
 * 
 * @param medicationDao DAO que será injetado no MedicationViewModel
 */
class MedicationViewModelFactory(
    private val medicationDao: MedicationDao
) : ViewModelProvider.Factory {
    
    /**
     * Cria uma nova instância do ViewModel solicitado.
     * 
     * Este método é chamado pelo ViewModelProvider quando uma nova instância
     * do ViewModel é necessária. Verifica se o tipo solicitado é compatível
     * com MedicationViewModel e cria a instância com as dependências injetadas.
     * 
     * @param modelClass Classe do ViewModel a ser criado
     * @return Nova instância do ViewModel solicitado
     * @throws IllegalArgumentException se o tipo do ViewModel não for suportado
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se o tipo solicitado é MedicationViewModel
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            // Cria e retorna uma nova instância do MedicationViewModel
            // com o MedicationDao injetado
            return MedicationViewModel(medicationDao) as T
        }
        
        // Se o tipo não for suportado, lança uma exceção
        throw IllegalArgumentException("Classe ViewModel desconhecida: ${modelClass.name}")
    }
}
