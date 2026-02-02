package com.example.bjjcompanion.data.repository

import com.example.bjjcompanion.data.local.database.dao.TechniqueDao
import com.example.bjjcompanion.data.local.database.entity.TechniqueEntity
import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.model.TechniqueCategory
import com.example.bjjcompanion.domain.repository.TechniqueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TechniqueRepositoryImpl @Inject constructor(
    private val techniqueDao: TechniqueDao
) : TechniqueRepository {

    override suspend fun addTechnique(technique: Technique) {
        techniqueDao.insertTechnique(technique.toEntity())
    }

    override suspend fun updateTechnique(technique: Technique) {
        techniqueDao.updateTechnique(technique.toEntity())
    }

    override suspend fun deleteTechnique(technique: Technique) {
        techniqueDao.deleteTechniqueById(technique.id)
    }

    override fun getAllTechniques(): Flow<List<Technique>> {
        return techniqueDao.getAllTechniques().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTechniquesByCategory(category: String): Flow<List<Technique>> {
        return techniqueDao.getTechniquesByCategory(category).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun searchTechniques(query: String): Flow<List<Technique>> {
        return techniqueDao.searchTechniques(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTechniqueById(id: Long): Technique? {
        return techniqueDao.getTechniqueById(id)?.toDomain()
    }

    // Mapping functions
    private fun TechniqueEntity.toDomain() = Technique(
        id = id,
        name = name,
        category = TechniqueCategory.valueOf(category.uppercase()),
        description = description,
        createdAt = createdAt
    )

    private fun Technique.toEntity() = TechniqueEntity(
        id = id,
        name = name,
        category = category.name.lowercase(),
        description = description,
        createdAt = createdAt
    )
}
