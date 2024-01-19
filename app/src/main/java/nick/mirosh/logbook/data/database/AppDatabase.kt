package nick.mirosh.logbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nick.mirosh.logbook.data.model.BmDatabaseEntry

const val DATABASE_NAME = "bm-measurement-db"

@Database(entities = [BmDatabaseEntry::class], version = 1)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bloodMeasurementDao(): BloodMeasurementDao
}