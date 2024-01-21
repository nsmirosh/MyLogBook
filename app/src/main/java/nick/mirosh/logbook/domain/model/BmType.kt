package nick.mirosh.logbook.domain.model


enum class BmType(val unit: String) {
    Mmol("mmol/L"),
    Mg("mg/Dl")
}

const val MMOL_CONVERSION_FACTOR = 18.0182

