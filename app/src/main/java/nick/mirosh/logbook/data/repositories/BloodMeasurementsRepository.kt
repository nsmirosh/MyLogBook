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

interface BloodMeasurementsRepository {
    suspend fun saveEntry(bmEntry: BmEntry)
    suspend fun getEntries(): Flow<DomainState<List< BmEntry>>>
}

class BloodMeasureRepositoryImpl @Inject constructor(
    private val bloodMeasurementDao: BloodMeasurementDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BloodMeasurementsRepository {

    override suspend fun saveEntry(bmEntry: BmEntry) {
        bloodMeasurementDao.insert(bmEntry.toBmDatabaseEntry())
    }

    override suspend fun getEntries(): Flow<DomainState<List<BmEntry>>> =
        flow {
            emit(DomainState.Loading)
            val entries = bloodMeasurementDao.getAllEntries().map { it.toBmEntry()}
            val dataState =
                if (entries.isEmpty()) DomainState.Empty else DomainState.Success(entries)
            emit(dataState)
        }.catch {
            emit(DomainState.Error(message = it.message))
        }.flowOn(dispatcher)
}