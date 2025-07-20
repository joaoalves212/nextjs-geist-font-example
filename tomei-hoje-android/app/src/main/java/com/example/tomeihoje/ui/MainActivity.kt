package com.example.tomeihoje.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tomeihoje.R
import com.example.tomeihoje.data.AppDatabase
import com.example.tomeihoje.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * Activity principal do aplicativo "Tomei hoje?".
 * 
 * Responsável por:
 * - Configurar a interface do usuário
 * - Inicializar o ViewModel e o banco de dados
 * - Gerenciar o RecyclerView com a lista de medicamentos
 * - Implementar a funcionalidade de adicionar novos medicamentos
 * - Observar mudanças nos dados e atualizar a UI
 * 
 * Segue o padrão MVVM, onde a Activity atua como View, observando
 * o ViewModel para mudanças nos dados e reagindo adequadamente.
 */
class MainActivity : AppCompatActivity() {
    
    // ViewBinding para acesso type-safe às views do layout
    private lateinit var binding: ActivityMainBinding
    
    // Adapter do RecyclerView para exibir a lista de medicamentos
    private lateinit var medicationAdapter: MedicationAdapter
    
    // ViewModel que gerencia os dados e lógica de negócio
    private lateinit var viewModel: MedicationViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configura ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Inicializa componentes
        setupDatabase()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }
    
    /**
     * Configura o banco de dados e inicializa o ViewModel.
     * 
     * Cria a instância do banco de dados usando o padrão Singleton,
     * obtém o DAO necessário e inicializa o ViewModel através da Factory.
     */
    private fun setupDatabase() {
        // Obtém instância do banco de dados
        val database = AppDatabase.getInstance(applicationContext)
        val medicationDao = database.medicationDao()
        
        // Cria o ViewModel usando a Factory para injeção de dependências
        val factory = MedicationViewModelFactory(medicationDao)
        viewModel = ViewModelProvider(this, factory)[MedicationViewModel::class.java]
    }
    
    /**
     * Configura o RecyclerView com o adapter e layout manager.
     * 
     * Define o LinearLayoutManager para exibição vertical da lista
     * e configura o adapter com callback para exclusão de medicamentos.
     */
    private fun setupRecyclerView() {
        medicationAdapter = MedicationAdapter(
            context = this,
            onDeleteClick = { medication ->
                // Mostra diálogo de confirmação antes de excluir
                showDeleteConfirmationDialog(medication)
            }
        )
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = medicationAdapter
            
            // Melhora a performance quando o tamanho da lista é fixo
            setHasFixedSize(true)
        }
    }
    
    /**
     * Configura o FloatingActionButton para adicionar novos medicamentos.
     * 
     * Define o listener que abre um diálogo para inserção do nome
     * do novo medicamento.
     */
    private fun setupFab() {
        binding.floatingActionButton.setOnClickListener {
            showAddMedicationDialog()
        }
    }
    
    /**
     * Configura a observação dos dados do ViewModel.
     * 
     * Utiliza lifecycleScope com repeatOnLifecycle para garantir que
     * as observações sejam pausadas/retomadas conforme o ciclo de vida
     * da Activity, evitando vazamentos de memória.
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            // Garante que a coleta só acontece quando a Activity está ativa
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                // Observa mudanças na lista de medicamentos
                launch {
                    viewModel.allMedications.collect { medications ->
                        medicationAdapter.submitList(medications)
                    }
                }
                
                // Observa mensagens de erro
                launch {
                    viewModel.errorMessage.collect { errorMessage ->
                        errorMessage?.let {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                            viewModel.clearErrorMessage()
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Exibe um diálogo para adicionar um novo medicamento.
     * 
     * Cria um AlertDialog com um EditText para o usuário inserir
     * o nome do medicamento. Valida a entrada e chama o ViewModel
     * para inserir o novo medicamento.
     */
    private fun showAddMedicationDialog() {
        // Cria EditText para entrada do nome do medicamento
        val editText = EditText(this).apply {
            hint = getString(R.string.medication_name_hint)
            setPadding(50, 30, 50, 30)
        }
        
        // Cria e exibe o diálogo
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_medication))
            .setView(editText)
            .setPositiveButton(getString(R.string.add)) { dialog, _ ->
                val medicationName = editText.text.toString().trim()
                
                if (medicationName.isNotEmpty()) {
                    // Chama o ViewModel para inserir o medicamento
                    viewModel.insertMedication(medicationName)
                } else {
                    Toast.makeText(
                        this,
                        "Nome do medicamento não pode estar vazio",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    /**
     * Exibe um diálogo de confirmação antes de excluir um medicamento.
     * 
     * Garante que o usuário realmente deseja excluir o medicamento,
     * evitando exclusões acidentais.
     * 
     * @param medication Medicamento a ser excluído
     */
    private fun showDeleteConfirmationDialog(medication: com.example.tomeihoje.data.Medication) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Medicamento")
            .setMessage("Tem certeza que deseja excluir \"${medication.name}\"?")
            .setPositiveButton("Excluir") { dialog, _ ->
                viewModel.deleteMedication(medication)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
