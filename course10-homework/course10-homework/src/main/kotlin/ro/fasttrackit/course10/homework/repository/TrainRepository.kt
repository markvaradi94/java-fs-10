package ro.fasttrackit.course10.homework.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Pageable.unpaged
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils.getPage
import org.springframework.stereotype.Repository
import ro.fasttrackit.course10.homework.model.entity.Train
import ro.fasttrackit.course10.homework.model.filters.TrainFilters
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.inOrIs
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.multipleIgnoreCase
import java.util.*
import java.util.Optional.ofNullable

@Repository
class TrainRepository(
    private val trainRepo: TrainRepo,
    private val mongo: MongoTemplate
) {

    fun findAll(filters: TrainFilters, pageable: Pageable = unpaged()): Page<Train> {
        val query = toQuery(filters).with(pageable)
        return getPage(
            mongo.find(query, Train::class.java),
            pageable
        ) {
            mongo.count(query.with(unpaged()), Train::class.java)
        }
    }

    private fun toQuery(filters: TrainFilters): Query = Query.query(toCriteria(filters))

    private fun toCriteria(filters: TrainFilters): Criteria {
        val criteria = Criteria()
        ofNullable(filters)
            .ifPresent { flt ->
                ofNullable(flt.id).ifPresent { inOrIs(criteria, it, "id") }
                ofNullable(flt.model).ifPresent { multipleIgnoreCase(criteria, it, "model") }
                ofNullable(flt.locationId).ifPresent { inOrIs(criteria, it, "locationId") }
                ofNullable(flt.minCarts).ifPresent { criteria.and("carts").gte(it) }
                ofNullable(flt.maxCarts).ifPresent { criteria.and("carts").lte(it) }
            }
        return criteria
    }

    fun addTrain(train: Train): Train = trainRepo.save(train)

    fun findById(id: String): Optional<Train> = trainRepo.findById(id)

    fun deleteById(id: String): Optional<Train> {
        val trainToDelete = trainRepo.findById(id)
        trainToDelete.ifPresent { trainRepo.deleteById(it.id) }
        return trainToDelete
    }
}
