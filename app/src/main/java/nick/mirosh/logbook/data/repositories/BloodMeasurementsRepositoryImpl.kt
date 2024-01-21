package nick.mirosh.logbook.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import nick.mirosh.logbook.data.database.BloodMeasurementDao
import nick.mirosh.logbook.data.mappers.toBmDatabaseEntry
import nick.mirosh.logbook.data.mappers.toBmEntry
import nick.mirosh.logbook.di.IoDispatcher
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.data.BloodMeasurementsRepository
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import javax.inject.Inject


class BloodMeasurementRepositoryImpl @Inject constructor(
    private val bloodMeasurementDao: BloodMeasurementDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BloodMeasurementsRepository {

    override suspend fun saveEntry(bloodGlucoseEntry: BloodGlucoseEntry): Flow<DomainState<Unit>> =
        flow {
            emit(DomainState.Loading)
            bloodMeasurementDao.insert(bloodGlucoseEntry.toBmDatabaseEntry())
            emit(DomainState.Success(Unit))
        }.catch {
            emit(DomainState.Error(message = it.message))
        }.flowOn(dispatcher)

    override suspend fun getEntries(): Flow<DomainState<List<BloodGlucoseEntry>>> =
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