package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "marine_species")
data class MarineSpecies(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameZh: String,
    val nameEn: String,
    val scientificName: String,
    val categoryZh: String,
    val categoryEn: String,
    val depth: String,
    val size: String,
    val habitatZh: String,
    val habitatEn: String,
    val dietZh: String,
    val dietEn: String,
    val descriptionZh: String,
    val descriptionEn: String,
    val funFactZh: String,
    val funFactEn: String,
    val isFavorite: Boolean = false,
    val illustrationKey: String,
    val dangerLevel: Int = 1 // 1 to 5 scale
) : Serializable
