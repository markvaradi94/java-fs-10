package ro.fasttrackit.course10.homework.validator

import com.google.common.base.Strings.isNullOrEmpty
import org.springframework.stereotype.Component
import ro.fasttrackit.course10.homework.exception.ValidationException
import ro.fasttrackit.course10.homework.model.entity.Train
import ro.fasttrackit.course10.homework.repository.TrainRepo
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

@Component
class TrainValidator(private val trainRepo: TrainRepo) {

    fun validateNewThrow(train: Train) = validate(train, null, true).ifPresent { throw it }

    fun validateExistsOrThrow(trainId: String) = exists(trainId).ifPresent { throw it }

    fun validateReplaceOrThrow(trainId: String, originalTrain: Train, updatedTrain: Train) =
        exists(trainId)
            .or { validate(originalTrain, updatedTrain, false) }
            .ifPresent { throw it }

    private fun validate(updatedTrain: Train, originalTrain: Train?, newTrain: Boolean): Optional<ValidationException> {
        val errorMessages = mutableListOf<String>()
        if (isNullOrEmpty(updatedTrain.id) && !newTrain) {
            errorMessages.add("Train id cannot be null or empty.")
        } else if (isNullOrEmpty(updatedTrain.locationId)) {
            errorMessages.add("Train must have a valid location assigned to it.")
        } else if (updatedTrain.carts < 0 || updatedTrain.carts > 25) {
            errorMessages.add("Train must have a valid number of carts assigned to it.")
        } else if (isNullOrEmpty(updatedTrain.model)) {
            errorMessages.add("Train model must be valid.")
        } else if (originalTrain != null && !validFieldUpdates(originalTrain, updatedTrain)) {
            errorMessages.add("Only the location and the number of carts of a train can be updated.")
        }

        return if (errorMessages.isEmpty()) empty()
        else of(ValidationException(errorMessages.joinToString { s -> s }))
    }

    private fun validFieldUpdates(originalTrain: Train, updatedTrain: Train): Boolean =
        originalTrain.model != updatedTrain.model || originalTrain.id != updatedTrain.id


    private fun exists(trainId: String): Optional<ValidationException> {
        return if (trainRepo.existsById(trainId)) empty()
        else of(ValidationException("Train with id $trainId could not be found."))
    }
}
