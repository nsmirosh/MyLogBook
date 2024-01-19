package nick.mirosh.logbook.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.di.IoDispatcher
import nick.mirosh.logbook.domain.model.BmEntry
import javax.inject.Inject

class SaveBloodMeasurementUseCase @Inject constructor(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bmEntry: BmEntry) {
        withContext(dispatcher) {
            bloodMeasurementsRepository.saveEntry(bmEntry)
        }
    }
}