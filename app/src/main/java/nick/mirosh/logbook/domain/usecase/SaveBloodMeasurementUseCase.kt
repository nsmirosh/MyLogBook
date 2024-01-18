package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import java.math.BigDecimal

class SaveBloodMeasurementUseCase(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository

) {
    suspend operator fun invoke(mmolPerL: BigDecimal) {
        bloodMeasurementsRepository.saveBloodMeasurement(mmolPerL)
    }
}