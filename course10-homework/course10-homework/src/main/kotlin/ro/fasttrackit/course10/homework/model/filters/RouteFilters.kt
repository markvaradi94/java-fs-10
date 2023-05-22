package ro.fasttrackit.course10.homework.model.filters

data class RouteFilters(
    val id: List<String>?,
    val startId: List<String>?,
    val destinationId: List<String>?,
    val trainId: List<String>?
)
