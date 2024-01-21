package nick.mirosh.logbook.domain.model

/**
 * Convert to sealed class and use as data value.
 */
enum class BmType(val unit: String) {
    Mmol("mmol/L"),
    Mg("mg/Dl")
}