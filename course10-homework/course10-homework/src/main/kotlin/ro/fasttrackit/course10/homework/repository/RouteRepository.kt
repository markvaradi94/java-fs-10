package ro.fasttrackit.course10.homework.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Pageable.unpaged
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils.getPage
import org.springframework.stereotype.Repository
import ro.fasttrackit.course10.homework.model.entity.Route
import ro.fasttrackit.course10.homework.model.filters.RouteFilters
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.inOrIs
import java.util.*
import java.util.Optional.ofNullable

@Repository
class RouteRepository(
    private val routeRepo: RouteRepo,
    private val mongo: MongoTemplate
) {

    fun findAll(filters: RouteFilters, pageable: Pageable = unpaged()): Page<Route> {
        val query = toQuery(filters).with(pageable)
        return getPage(
            mongo.find(query, Route::class.java),
            pageable
        ) {
            mongo.count(query.with(unpaged()), Route::class.java)
        }
    }

    private fun toQuery(filters: RouteFilters): Query = Query.query(toCriteria(filters))

    private fun toCriteria(filters: RouteFilters): Criteria {
        val criteria = Criteria()
        ofNullable(filters)
            .ifPresent { flt ->
                ofNullable(flt.id).ifPresent { inOrIs(criteria, it, "id") }
                ofNullable(flt.startId).ifPresent { inOrIs(criteria, it, "start.id") }
                ofNullable(flt.destinationId).ifPresent { inOrIs(criteria, it, "destination.id") }
                ofNullable(flt.trainId).ifPresent { inOrIs(criteria, it, "trainId") }
            }
        return criteria
    }

    fun addRoute(route: Route): Route = routeRepo.save(route)

    fun findById(id: String): Optional<Route> = routeRepo.findById(id)

    fun deleteById(id: String): Optional<Route> {
        val routeToDelete = routeRepo.findById(id)
        routeToDelete.ifPresent { routeRepo.deleteById(it.id) }
        return routeToDelete
    }
}
