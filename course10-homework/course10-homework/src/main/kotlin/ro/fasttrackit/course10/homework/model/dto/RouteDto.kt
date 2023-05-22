package ro.fasttrackit.course10.homework.model.dto

import java.util.UUID.randomUUID

data class RouteDto(
    val id: String = randomUUID().toString(),
    val startId: String,
    val destinationId: String,
    val trainId: String
)
