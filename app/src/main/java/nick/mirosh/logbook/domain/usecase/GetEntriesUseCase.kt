package nick.mirosh.logbook.domain.usecase

import kotlinx.coroutines.flow.Flow
import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import javax.inject.Inject

class GetEntriesUseCase @Inject constructor(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository,
) {
    suspend operator fun invoke(): Flow<DomainState<List<BloodGlucoseEntry>>> =
        bloodMeasurementsRepository.getEntries()

}
