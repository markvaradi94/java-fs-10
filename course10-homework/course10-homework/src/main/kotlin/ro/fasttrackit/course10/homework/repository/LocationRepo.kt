package ro.fasttrackit.course10.homework.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.fasttrackit.course10.homework.model.entity.Location

interface LocationRepo : MongoRepository<Location, String> {
    fun existsByCityIgnoreCase(city: String): Boolean
}
