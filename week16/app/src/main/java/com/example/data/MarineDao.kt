package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MarineDao {
    @Query("SELECT * FROM marine_species ORDER BY nameZh ASC")
    fun getAllSpecies(): Flow<List<MarineSpecies>>

    @Query("SELECT * FROM marine_species WHERE id = :id")
    fun getSpeciesById(id: Int): Flow<MarineSpecies?>

    @Query("SELECT * FROM marine_species WHERE isFavorite = 1")
    fun getFavoriteSpecies(): Flow<List<MarineSpecies>>

    @Query("UPDATE marine_species SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecies(species: MarineSpecies)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(speciesList: List<MarineSpecies>)

    @Query("SELECT COUNT(*) FROM marine_species")
    suspend fun getSpeciesCount(): Int
}
