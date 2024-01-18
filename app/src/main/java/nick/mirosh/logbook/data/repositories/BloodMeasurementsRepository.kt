package nick.mirosh.logbook.data.repositories

import java.math.BigDecimal

interface BloodMeasurementsRepository {
    suspend fun saveBloodMeasurement(mmolPerL: BigDecimal)
}
class BloodMeasureRepositoryImpl: BloodMeasurementsRepository {

    override suspend fun saveBloodMeasurement(mmolPerL: BigDecimal) {


    }

}