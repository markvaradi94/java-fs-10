package ro.fasttrackit.course10.homework.controller

import com.github.fge.jsonpatch.JsonPatch
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import ro.fasttrackit.course10.homework.exception.ResourceNotFoundException
import ro.fasttrackit.course10.homework.model.dto.LocationDto
import ro.fasttrackit.course10.homework.model.filters.LocationFilters
import ro.fasttrackit.course10.homework.model.pagination.CollectionResponse
import ro.fasttrackit.course10.homework.service.LocationService
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.toCollectionResponse

@RestController
@RequestMapping("locations")
class LocationController(private val service: LocationService) {

    @GetMapping
    fun findAll(locationFilters: LocationFilters, pageable: Pageable): CollectionResponse<LocationDto> =
        toCollectionResponse(service.findAll(locationFilters, pageable))

    @GetMapping("{locationId}")
    fun findOne(@PathVariable locationId: String): LocationDto = service.findOne(locationId)
        .orElseThrow { ResourceNotFoundException("Could not find location with id $locationId") }

    @PostMapping
    fun add(@RequestBody newLocation: LocationDto): LocationDto = service.add(newLocation)

    @DeleteMapping("{locationId}")
    fun delete(@PathVariable locationId: String): LocationDto = service.delete(locationId)
        .orElseThrow { ResourceNotFoundException("Could not find location with id $locationId") }

    @PatchMapping("{locationId}")
    fun patch(@PathVariable locationId: String, @RequestBody jsonPatch: JsonPatch): LocationDto =
        service.patch(locationId, jsonPatch)
}
