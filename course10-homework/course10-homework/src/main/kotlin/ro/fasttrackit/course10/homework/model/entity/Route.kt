package ro.fasttrackit.course10.homework.model.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID.randomUUID

@Document(collection = "routes")
data class Route(
    val id: String = randomUUID().toString(),
    val startId: String,
    val destinationId: String,
    val trainId: String
)
