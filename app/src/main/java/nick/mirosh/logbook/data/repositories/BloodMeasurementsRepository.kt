package nick.mirosh.logbook.data.repositories

import nick.mirosh.logbook.data.database.BloodMeasurementDao
import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.toBmDatabaseEntry
import javax.inject.Inject

interface BloodMeasurementsRepository {
    suspend fun saveEntry(bmEntry: BmEntry)
    suspend fun getEntries(): List<String>
}

class BloodMeasureRepositoryImpl @Inject constructor(
    private val bloodMeasurementDao: BloodMeasurementDao
) : BloodMeasurementsRepository {

    override suspend fun saveEntry(bmEntry: BmEntry) {
        bloodMeasurementDao.insert(bmEntry.toBmDatabaseEntry())
    }
    override suspend fun getEntries(): List<String> {
        return bloodMeasurementDao.getAllEntries().map { it.toString() }
    }

//    override suspend fun getEntries() : Flow<List<String>> =
//        flow {
//            emit(DataState.Loading)
//            val hashtags = assetService.getHashtags()
//            val dataState = if (hashtags.isEmpty()) DataState.Empty else DataState.Success(hashtags)
//            emit(dataState)
//        }.catch {
//            emit(DataState.Error(message = "Error parsing json"))
//        }

}