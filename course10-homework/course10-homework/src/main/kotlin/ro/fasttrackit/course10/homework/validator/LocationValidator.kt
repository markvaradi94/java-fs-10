package ro.fasttrackit.course10.homework.validator

import com.google.common.base.Strings.isNullOrEmpty
import org.springframework.stereotype.Component
import ro.fasttrackit.course10.homework.exception.ValidationException
import ro.fasttrackit.course10.homework.model.entity.Location
import ro.fasttrackit.course10.homework.repository.LocationRepo
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

@Component
class LocationValidator(private val locationRepo: LocationRepo) {

    fun validateNewThrow(location: Location) = validate(location, true).ifPresent { throw it }

    fun validateExistsOrThrow(locationId: String) = exists(locationId).ifPresent { throw it }

    fun validateReplaceOrThrow(locationId: String, location: Location) =
        exists(locationId)
            .or { validate(location, false) }
            .ifPresent { throw it }

    private fun validate(location: Location, newLocation: Boolean): Optional<ValidationException> {
        val errorMessages = mutableListOf<String>()
        if (isNullOrEmpty(location.id) && !newLocation) {
            errorMessages.add("Location id cannot be null or empty.")
        } else if (isNullOrEmpty(location.city)) {
            errorMessages.add("Location must have a valid city name.")
        } else if (newLocation && locationRepo.existsById(location.id)) {
            errorMessages.add("Cannot have duplicate id locations.")
        } else if (newLocation && locationRepo.existsByCityIgnoreCase(location.city)) {
            errorMessages.add("Cannot have duplicate cities.")
        }

        return if (errorMessages.isEmpty()) empty()
        else of(ValidationException(errorMessages.joinToString { s -> s }))
    }

    private fun exists(locationId: String): Optional<ValidationException> {
        return if (locationRepo.existsById(locationId)) empty()
        else of(ValidationException("Location with id $locationId could not be found."))
    }

}
