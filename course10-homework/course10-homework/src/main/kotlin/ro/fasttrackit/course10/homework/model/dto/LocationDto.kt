package ro.fasttrackit.course10.homework.model.dto

import java.util.UUID.randomUUID

data class LocationDto(
    val id: String = randomUUID().toString(),
    val city: String
)
