package nick.mirosh.logbook.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.di.IoDispatcher
import javax.inject.Inject

class GetEntriesUseCase @Inject constructor(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<String> {
        return withContext(dispatcher) {
            bloodMeasurementsRepository.getEntries()
        }
    }
}
