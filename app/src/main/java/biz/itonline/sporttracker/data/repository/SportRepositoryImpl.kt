package biz.itonline.sporttracker.data.repository

import biz.itonline.sporttracker.data.local.dao.SportDao
import biz.itonline.sporttracker.data.remote.model.RemoteSportDto
import biz.itonline.sporttracker.data.toDomain
import biz.itonline.sporttracker.data.toEntity
import biz.itonline.sporttracker.data.toRemoteDto
import biz.itonline.sporttracker.domain.model.SportRecord
import biz.itonline.sporttracker.domain.model.StorageType
import biz.itonline.sporttracker.domain.repository.SportRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SportRepositoryImpl @Inject constructor(
    private val dao: SportDao,
    private val firestore: FirebaseFirestore
) : SportRepository {

    override fun getAllSports(): Flow<List<SportRecord>> {

        // 1. Stream z lokální DB
        val localFlow = dao.getAllSports().map { entities ->
            entities.map { it.toDomain() }
        }

        // 2. Stream z Firebase
        val remoteFlow = firestore.collection("sports")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(RemoteSportDto::class.java).map { it.toDomain() }
            }
            .onStart { emit(emptyList()) }

        // 3. Spojené streamy
        return combine(localFlow, remoteFlow) { local, remote ->
            (local + remote).sortedByDescending { it.date }
        }
    }

    override suspend fun saveSport(record: SportRecord) {
        when (record.type) {
            StorageType.LOCAL -> {
                dao.insertSport(record.toEntity())
            }

            StorageType.REMOTE -> {
                firestore.collection("sports")
                    .document(record.id)
                    .set(record.toRemoteDto())
                    .await()
            }
        }
    }

    override suspend fun deleteSport(record: SportRecord) {
        when (record.type) {
            StorageType.LOCAL -> {
                dao.deleteSportById(record.id)
            }

            StorageType.REMOTE -> {
                firestore.collection("sports")
                    .document(record.id)
                    .delete()
                    .await()
            }
        }
    }
}