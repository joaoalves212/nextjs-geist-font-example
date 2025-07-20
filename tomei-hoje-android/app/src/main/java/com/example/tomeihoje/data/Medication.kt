package com.example.tomeihoje.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade Room que representa um medicamento no banco de dados.
 * 
 * Esta classe define a estrutura da tabela "medications" no banco de dados local.
 * Cada medicamento possui um ID único (gerado automaticamente) e um nome.
 * 
 * @param id Identificador único do medicamento (auto-gerado)
 * @param name Nome do medicamento inserido pelo usuário
 */
@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
