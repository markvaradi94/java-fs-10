package ro.fasttrackit.course10.homework.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Pageable.unpaged
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils.getPage
import org.springframework.stereotype.Repository
import ro.fasttrackit.course10.homework.model.entity.Location
import ro.fasttrackit.course10.homework.model.filters.LocationFilters
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.inOrIs
import java.util.*
import java.util.Optional.ofNullable

@Repository
class LocationRepository(
    private val locationRepo: LocationRepo,
    private val mongo: MongoTemplate
) {

    fun findAll(filters: LocationFilters, pageable: Pageable = unpaged()): Page<Location> {
        val query = toQuery(filters).with(pageable)
        return getPage(
            mongo.find(query, Location::class.java),
            pageable
        ) {
            mongo.count(
                query.with(unpaged()),
                Location::class.java
            )
        }
    }

    private fun toQuery(filters: LocationFilters): Query = Query.query(toCriteria(filters))

    private fun toCriteria(filters: LocationFilters): Criteria {
        val criteria = Criteria()
        ofNullable(filters)
            .ifPresent { flt ->
                ofNullable(flt.id)
                    .ifPresent { inOrIs(criteria, it, "id") }
                ofNullable(flt.city)
                    .ifPresent { inOrIs(criteria, it, "city") }
            }
        return criteria
    }

    fun addLocation(location: Location): Location = locationRepo.save(location)

    fun findById(id: String): Optional<Location> = locationRepo.findById(id)

    fun deleteById(id: String): Optional<Location> {
        val locationToDelete = locationRepo.findById(id)
        locationToDelete.ifPresent { locationRepo.deleteById(it.id) }
        return locationToDelete
    }
}
