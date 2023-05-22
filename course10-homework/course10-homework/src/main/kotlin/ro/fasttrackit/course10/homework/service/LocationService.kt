package ro.fasttrackit.course10.homework.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ro.fasttrackit.course10.homework.exception.ResourceNotFoundException
import ro.fasttrackit.course10.homework.model.dto.LocationDto
import ro.fasttrackit.course10.homework.model.entity.Location
import ro.fasttrackit.course10.homework.model.filters.LocationFilters
import ro.fasttrackit.course10.homework.model.mappers.LocationMapper
import ro.fasttrackit.course10.homework.repository.LocationRepository
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.mapPagedContent
import ro.fasttrackit.course10.homework.validator.LocationValidator
import java.util.*


@Service
class LocationService(
    private val repository: LocationRepository,
    private val validator: LocationValidator,
    private val objectMapper: ObjectMapper,
    private val locationMapper: LocationMapper
) {

    fun findAll(filters: LocationFilters, pageable: Pageable): Page<LocationDto> =
        mapPagedContent(
            locationMapper::toApi,
            repository.findAll(filters, pageable),
            pageable
        )

    fun findOne(locationId: String): Optional<LocationDto> = repository.findById(locationId)
        .map(locationMapper::toApi)

    fun add(newLocation: LocationDto): LocationDto {
        validator.validateNewThrow(locationMapper.toEntity(newLocation))
        return locationMapper.toApi(repository.addLocation(locationMapper.toEntity(newLocation)))
    }

    fun delete(locationId: String): Optional<LocationDto> {
        validator.validateExistsOrThrow(locationId)
        return repository.deleteById(locationId)
            .map(locationMapper::toApi)
    }

    fun patch(locationId: String, patch: JsonPatch): LocationDto {
        validator.validateExistsOrThrow(locationId)
        val locationToPatch = repository.findById(locationId)
            .orElseThrow { ResourceNotFoundException("Could not find location with id $locationId for update.") }
        val patchedLocationJson = patch.apply(objectMapper.valueToTree(locationToPatch))
        val patchedLocation = objectMapper.treeToValue(patchedLocationJson, Location::class.java)
        validator.validateReplaceOrThrow(locationId, patchedLocation)
        return locationMapper.toApi(repository.addLocation(patchedLocation))
    }
}
