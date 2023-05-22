package ro.fasttrackit.course10.homework.model.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID.randomUUID

@Document(collection = "locations")
data class Location(
    val id: String = randomUUID().toString(),
    val city: String
)
