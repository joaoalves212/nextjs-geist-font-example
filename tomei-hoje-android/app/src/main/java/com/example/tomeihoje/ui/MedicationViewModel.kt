package com.example.tomeihoje.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomeihoje.data.Medication
import com.example.tomeihoje.data.MedicationDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por gerenciar os dados dos medicamentos e a lógica de negócio.
 * 
 * Segue o padrão MVVM (Model-View-ViewModel) para separar a lógica de negócio
 * da interface do usuário. Utiliza StateFlow para fornecer dados reativos à UI
 * e Kotlin Coroutines para operações assíncronas de banco de dados.
 * 
 * @param medicationDao DAO para acesso aos dados dos medicamentos
 */
class MedicationViewModel(private val medicationDao: MedicationDao) : ViewModel() {
    
    // StateFlow privado para controle interno da lista de medicamentos
    private val _allMedications = MutableStateFlow<List<Medication>>(emptyList())
    
    /**
     * StateFlow público que expõe a lista de medicamentos para a UI.
     * A UI pode observar este StateFlow para receber atualizações automáticas
     * sempre que a lista de medicamentos for modificada.
     */
    val allMedications: StateFlow<List<Medication>> = _allMedications.asStateFlow()
    
    // StateFlow para controlar o estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // StateFlow para mensagens de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        // Inicia a coleta dos dados do banco de dados quando o ViewModel é criado
        loadMedications()
    }
    
    /**
     * Carrega os medicamentos do banco de dados e atualiza o StateFlow.
     * 
     * Utiliza o Flow do DAO para receber atualizações automáticas sempre que
     * os dados no banco forem modificados. Inclui tratamento de erros para
     * garantir que a aplicação não trave em caso de problemas no banco.
     */
    private fun loadMedications() {
        viewModelScope.launch {
            _isLoading.value = true
            medicationDao.getAllMedications()
                .catch { exception ->
                    // Trata erros na coleta de dados
                    _errorMessage.value = "Erro ao carregar medicamentos: ${exception.message}"
                    _isLoading.value = false
                }
                .collect { medicationList ->
                    _allMedications.value = medicationList
                    _isLoading.value = false
                    _errorMessage.value = null
                }
        }
    }
    
    /**
     * Adiciona um novo medicamento ao banco de dados.
     * 
     * Valida o nome do medicamento antes de inserir e executa a operação
     * em uma coroutine para não bloquear a thread principal.
     * 
     * @param name Nome do medicamento a ser adicionado
     */
    fun insertMedication(name: String) {
        // Valida se o nome não está vazio ou apenas com espaços
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            _errorMessage.value = "Nome do medicamento não pode estar vazio"
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val medication = Medication(name = trimmedName)
                medicationDao.insert(medication)
                _errorMessage.value = null
            } catch (exception: Exception) {
                _errorMessage.value = "Erro ao adicionar medicamento: ${exception.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Remove um medicamento do banco de dados.
     * 
     * Executa a operação de exclusão em uma coroutine para não bloquear
     * a thread principal e inclui tratamento de erros.
     * 
     * @param medication Medicamento a ser removido
     */
    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                medicationDao.delete(medication)
                _errorMessage.value = null
            } catch (exception: Exception) {
                _errorMessage.value = "Erro ao excluir medicamento: ${exception.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Limpa mensagens de erro.
     * Útil para quando a UI já exibiu o erro e quer limpar o estado.
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
