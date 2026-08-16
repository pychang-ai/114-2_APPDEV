package com.example.data

import kotlinx.coroutines.flow.Flow

class MarineRepository(private val marineDao: MarineDao) {

    val allSpecies: Flow<List<MarineSpecies>> = marineDao.getAllSpecies()
    val favoriteSpecies: Flow<List<MarineSpecies>> = marineDao.getFavoriteSpecies()

    fun getSpeciesById(id: Int): Flow<MarineSpecies?> {
        return marineDao.getSpeciesById(id)
    }

    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        marineDao.updateFavorite(id, isFavorite)
    }

    suspend fun preseedDatabaseIfEmpty() {
        if (marineDao.getSpeciesCount() == 0) {
            marineDao.insertAll(SeedData.speciesList)
        }
    }
}
