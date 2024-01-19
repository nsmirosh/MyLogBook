package nick.mirosh.logbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nick.mirosh.logbook.data.model.BmDatabaseEntry

@Dao
interface BloodMeasurementDao {

    @Query("SELECT * FROM measurement_entry")
    fun getAllEntries():  List<BmDatabaseEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: BmDatabaseEntry)
}