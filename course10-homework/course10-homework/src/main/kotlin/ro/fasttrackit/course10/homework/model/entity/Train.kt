package ro.fasttrackit.course10.homework.model.entity

import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID.randomUUID

@Document(collection = "trains")
data class Train(
    val id: String = randomUUID().toString(),
    val model: String,
    val carts: Int,
    val locationId: String
)
