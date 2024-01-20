package nick.mirosh.logbook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal

@Entity(tableName = "measurement_entry")
data class BmDatabaseEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: BmType,
    val value: BigDecimal
) {
    fun toBmEntry() = BmEntry(
        type = type,
        value = value
    )
    override fun toString(): String {
        return "BmDatabaseEntry(id=$id, type=$type, value=$value)"
    }
}

