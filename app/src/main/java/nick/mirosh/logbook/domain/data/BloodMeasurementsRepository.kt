package nick.mirosh.logbook.domain.data

import kotlinx.coroutines.flow.Flow
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry

interface BloodMeasurementsRepository {
    suspend fun saveEntry(bloodGlucoseEntry: BloodGlucoseEntry): Flow<DomainState<Unit>>
    suspend fun getEntries(): Flow<DomainState<List<BloodGlucoseEntry>>>
}
