package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.domain.model.BmEntry
import javax.inject.Inject

class SaveBloodMeasurementUseCase @Inject constructor(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository
) {
    suspend operator fun invoke(bmEntry: BmEntry) {
        bloodMeasurementsRepository.saveEntry(bmEntry)
    }
}