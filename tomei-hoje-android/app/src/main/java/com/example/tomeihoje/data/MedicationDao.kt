package com.example.tomeihoje.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para operações de banco de dados relacionadas aos medicamentos.
 * 
 * Esta interface define todas as operações de banco de dados que podem ser realizadas
 * na tabela de medicamentos. O Room gera automaticamente a implementação desta interface.
 * 
 * Todas as operações são assíncronas usando Kotlin Coroutines para evitar bloqueio
 * da thread principal (UI thread).
 */
@Dao
interface MedicationDao {
    
    /**
     * Insere um novo medicamento no banco de dados.
     * 
     * @param medication O medicamento a ser inserido
     */
    @Insert
    suspend fun insert(medication: Medication)
    
    /**
     * Remove um medicamento do banco de dados.
     * 
     * @param medication O medicamento a ser removido
     */
    @Delete
    suspend fun delete(medication: Medication)
    
    /**
     * Obtém todos os medicamentos do banco de dados ordenados alfabeticamente.
     * 
     * Retorna um Flow que emite automaticamente uma nova lista sempre que
     * os dados no banco são modificados, permitindo atualizações reativas da UI.
     * 
     * @return Flow contendo a lista de todos os medicamentos ordenados por nome
     */
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<Medication>>
}
