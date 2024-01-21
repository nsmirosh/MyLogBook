package nick.mirosh.logbook.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import nick.mirosh.logbook.data.database.BloodMeasurementDao
import nick.mirosh.logbook.di.IoDispatcher
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.toBmDatabaseEntry
import javax.inject.Inject

/**
 * Architecture: For clean architecture move the interface to domain layer.
 */
interface BloodMeasurementsRepository {
    suspend fun saveEntry(bmEntry: BmEntry): Flow<DomainState<Unit>>
    suspend fun getEntries(): Flow<DomainState<List<BmEntry>>>
}

/**
 * Naming: BloodMeasureRepositorySql to give a better understanding of the implementation.
 * Let's say you have 2 different implementation of the same repository, they can't both
 * be named *Impl
 */
class BloodMeasureRepositoryImpl @Inject constructor(
    private val bloodMeasurementDao: BloodMeasurementDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BloodMeasurementsRepository {

    override suspend fun saveEntry(bmEntry: BmEntry): Flow<DomainState<Unit>> =
        flow {
            emit(DomainState.Loading)
            bloodMeasurementDao.insert(bmEntry.toBmDatabaseEntry())
            emit(DomainState.Success(Unit))
        }.catch {
            emit(DomainState.Error(message = it.message))
        }.flowOn(dispatcher)

    override suspend fun getEntries(): Flow<DomainState<List<BmEntry>>> =
        flow {
            emit(DomainState.Loading)
            val entries = bloodMeasurementDao.getAllEntries().map { it.toBmEntry() }
            val dataState =
                if (entries.isEmpty()) DomainState.Empty else DomainState.Success(entries)
            emit(dataState)
        }.catch {
            emit(DomainState.Error(message = it.message))
        }.flowOn(dispatcher)
}