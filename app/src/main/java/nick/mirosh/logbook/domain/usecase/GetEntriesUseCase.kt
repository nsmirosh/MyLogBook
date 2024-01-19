package nick.mirosh.logbook.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.di.IoDispatcher
import nick.mirosh.logbook.domain.DomainState
import javax.inject.Inject

class GetEntriesUseCase @Inject constructor(
    private val bloodMeasurementsRepository: BloodMeasurementsRepository,
) {
    suspend operator fun invoke(): Flow<DomainState<List<String>>> =
        bloodMeasurementsRepository.getEntries()

}
