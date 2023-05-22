package ro.fasttrackit.course10.homework.validator

import com.google.common.base.Strings.isNullOrEmpty
import org.springframework.stereotype.Component
import ro.fasttrackit.course10.homework.exception.ValidationException
import ro.fasttrackit.course10.homework.model.entity.Route
import ro.fasttrackit.course10.homework.repository.LocationRepo
import ro.fasttrackit.course10.homework.repository.RouteRepo
import ro.fasttrackit.course10.homework.repository.TrainRepo
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

@Component
class RouteValidator(
    private val routeRepo: RouteRepo,
    private val locationRepo: LocationRepo,
    private val trainRepo: TrainRepo
) {
    fun validateNewThrow(route: Route) = validate(route, true).ifPresent { throw it }

    fun validateExistsOrThrow(routeId: String) = exists(routeId).ifPresent { throw it }

    fun validateReplaceOrThrow(routeId: String, route: Route) =
        exists(routeId)
            .or { validate(route, false) }
            .ifPresent { throw it }

    private fun validate(route: Route, newRoute: Boolean): Optional<ValidationException> {
        val errorMessages = mutableListOf<String>()
        if (isNullOrEmpty(route.id) && !newRoute) {
            errorMessages.add("Route id cannot be null or empty.")
        } else if (newRoute && isNullOrEmpty(route.trainId) || isNullOrEmpty(route.startId) || isNullOrEmpty(route.destinationId)) {
            errorMessages.add("Route must have valid train, start and destination ids.")
        } else if (newRoute && !validAssignedTrain(route)) {
            errorMessages.add("Route start must be the same as the assigned train's location.")
        } else if (newRoute && !trainExists(route.trainId) || !locationExists(route.startId) || !locationExists(route.destinationId)) {
            errorMessages.add("Route must be assigned to existing locations and train.")
        } else if (newRoute && routeRepo.existsById(route.id)) {
            errorMessages.add("Cannot have duplicate id routes.")
        } else if (route.startId == route.destinationId) {
            errorMessages.add("Cannot create route with same start and destination.")
        }

        return if (errorMessages.isEmpty()) empty()
        else of(ValidationException(errorMessages.joinToString { s -> s }))
    }

    private fun validAssignedTrain(route: Route): Boolean {
        return trainRepo.findById(route.trainId)
            .map { it.locationId == route.startId }
            .orElse(false)
    }

    private fun exists(routeId: String): Optional<ValidationException> {
        return if (routeRepo.existsById(routeId)) empty()
        else of(ValidationException("Route with id $routeId could not be found."))
    }

    private fun trainExists(trainId: String): Boolean = trainRepo.existsById(trainId)

    private fun locationExists(locationId: String): Boolean = locationRepo.existsById(locationId)
}
