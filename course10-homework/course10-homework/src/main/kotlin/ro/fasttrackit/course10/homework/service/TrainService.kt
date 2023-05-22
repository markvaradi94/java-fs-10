package ro.fasttrackit.course10.homework.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ro.fasttrackit.course10.homework.exception.ResourceNotFoundException
import ro.fasttrackit.course10.homework.model.dto.TrainDto
import ro.fasttrackit.course10.homework.model.entity.Train
import ro.fasttrackit.course10.homework.model.filters.TrainFilters
import ro.fasttrackit.course10.homework.model.mappers.TrainMapper
import ro.fasttrackit.course10.homework.repository.TrainRepository
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.mapPagedContent
import ro.fasttrackit.course10.homework.validator.TrainValidator
import java.util.*

@Service
class TrainService(
    private val repository: TrainRepository,
    private val validator: TrainValidator,
    private val objectMapper: ObjectMapper,
    private val trainMapper: TrainMapper
) {

    fun findAll(filters: TrainFilters, pageable: Pageable): Page<TrainDto> =
        mapPagedContent(
            trainMapper::toApi,
            repository.findAll(filters, pageable),
            pageable
        )

    fun findOne(trainId: String): Optional<TrainDto> = repository.findById(trainId)
        .map(trainMapper::toApi)

    fun add(newTrain: TrainDto): TrainDto {
        validator.validateNewThrow(trainMapper.toEntity(newTrain))
        return trainMapper.toApi(repository.addTrain(trainMapper.toEntity(newTrain)))
    }

    fun delete(trainId: String): Optional<TrainDto> {
        validator.validateExistsOrThrow(trainId)
        return repository.deleteById(trainId)
            .map(trainMapper::toApi)
    }

    fun patch(trainId: String, patch: JsonPatch): TrainDto {
        validator.validateExistsOrThrow(trainId)
        val trainToPatch = repository.findById(trainId)
            .orElseThrow { ResourceNotFoundException("Could not find train with id $trainId for update.") }
        val patchedTrainJson = patch.apply(objectMapper.valueToTree(trainToPatch))
        val patchedTrain = objectMapper.treeToValue(patchedTrainJson, Train::class.java)
        validator.validateReplaceOrThrow(trainId, trainToPatch, patchedTrain)
        return trainMapper.toApi(repository.addTrain(patchedTrain))
    }
}
