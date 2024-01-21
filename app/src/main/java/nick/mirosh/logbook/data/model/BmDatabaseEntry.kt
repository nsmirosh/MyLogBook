package nick.mirosh.logbook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal

/**
 * Naming: I prefer to call these Dto to distinguish them from the domain object,
 * I would call it Database entry either, suggestion BloodGlucoseEntryDto
 */
@Entity(tableName = "measurement_entry")
data class BmDatabaseEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: BmType,
    val value: BigDecimal
) {
    /**
     * I would move this to it's own file BloodGlucoseEntryDtoExt.kt as an extension function
     * Since there might be several to-targets.
     */
    fun toBmEntry() = BmEntry(
        type = type,
        value = value
    )

    /**
     * Don't think yo need this since it's a data class.
     */
    override fun toString(): String {
        return "BmDatabaseEntry(id=$id, type=$type, value=$value)"
    }
}

