package nick.mirosh.logbook.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.data.repositories.BloodMeasurementRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindBloodMeasureRepository(
        bloodMeasureRepository: BloodMeasurementRepositoryImpl
    ): BloodMeasurementsRepository
}
