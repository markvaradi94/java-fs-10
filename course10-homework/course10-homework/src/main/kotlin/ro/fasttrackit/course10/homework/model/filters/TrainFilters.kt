package ro.fasttrackit.course10.homework.model.filters

data class TrainFilters(
    val id: List<String>?,
    val model: List<String>?,
    val locationId: List<String>?,
    val minCarts: Int?,
    val maxCarts: Int?
)
