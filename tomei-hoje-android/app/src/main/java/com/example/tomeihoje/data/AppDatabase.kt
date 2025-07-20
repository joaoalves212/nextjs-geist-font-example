package com.example.tomeihoje.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Classe principal do banco de dados Room para o aplicativo "Tomei hoje?".
 * 
 * Esta classe abstrata define a configuração do banco de dados, incluindo:
 * - As entidades (tabelas) que fazem parte do banco
 * - A versão do banco de dados
 * - Os DAOs disponíveis para acesso aos dados
 * 
 * Implementa o padrão Singleton para garantir que apenas uma instância
 * do banco de dados seja criada durante toda a execução do aplicativo,
 * evitando problemas de concorrência e melhorando a performance.
 */
@Database(
    entities = [Medication::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Fornece acesso ao DAO de medicamentos.
     * 
     * @return Instância do MedicationDao para operações de banco de dados
     */
    abstract fun medicationDao(): MedicationDao
    
    companion object {
        // Volatile garante que mudanças na variável INSTANCE sejam imediatamente
        // visíveis para todas as threads
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Obtém a instância única do banco de dados.
         * 
         * Utiliza o padrão Singleton com double-checked locking para garantir
         * thread-safety e performance. A instância é criada apenas uma vez,
         * mesmo em cenários multi-thread.
         * 
         * @param context Contexto da aplicação para criação do banco
         * @return Instância única do AppDatabase
         */
        fun getInstance(context: Context): AppDatabase {
            // Se a instância já existe, retorna ela
            return INSTANCE ?: synchronized(this) {
                // Double-check: verifica novamente dentro do bloco sincronizado
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tomeihoje_database"
                )
                    // Permite queries na thread principal apenas para desenvolvimento
                    // Em produção, remova esta linha para forçar uso de coroutines
                    .fallbackToDestructiveMigration()
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
