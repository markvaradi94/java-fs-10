package ro.fasttrackit.course10.homework.model.dto

import java.util.UUID.randomUUID

data class TrainDto(
    val id: String = randomUUID().toString(),
    val model: String,
    val carts: Int,
    val locationId: String
)
