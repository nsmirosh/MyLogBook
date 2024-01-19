package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import javax.inject.Inject

class GetEntriesUseCase @Inject constructor(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository
) {
    suspend operator fun invoke(): List<String> {
        return bloodMeasurementsRepository.getEntries()
    }
}
