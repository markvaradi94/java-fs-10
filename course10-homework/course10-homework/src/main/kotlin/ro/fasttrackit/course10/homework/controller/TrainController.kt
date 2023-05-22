package ro.fasttrackit.course10.homework.controller

import com.github.fge.jsonpatch.JsonPatch
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import ro.fasttrackit.course10.homework.exception.ResourceNotFoundException
import ro.fasttrackit.course10.homework.model.dto.TrainDto
import ro.fasttrackit.course10.homework.model.filters.TrainFilters
import ro.fasttrackit.course10.homework.model.pagination.CollectionResponse
import ro.fasttrackit.course10.homework.service.TrainService
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.toCollectionResponse

@RestController
@RequestMapping("trains")
class TrainController(private val service: TrainService) {

    @GetMapping
    fun findAll(filters: TrainFilters, pageable: Pageable): CollectionResponse<TrainDto> =
        toCollectionResponse(service.findAll(filters, pageable))

    @GetMapping("{trainId}")
    fun findOne(@PathVariable trainId: String): TrainDto = service.findOne(trainId)
        .orElseThrow { ResourceNotFoundException("Could not find train with id $trainId.") }

    @PostMapping
    fun add(@RequestBody newTrain: TrainDto): TrainDto = service.add(newTrain)

    @DeleteMapping("{trainId}")
    fun delete(@PathVariable trainId: String): TrainDto = service.delete(trainId)
        .orElseThrow { ResourceNotFoundException("Could not find train with id $trainId.") }

    @PatchMapping("{trainId}")
    fun patch(@PathVariable trainId: String, @RequestBody jsonPatch: JsonPatch): TrainDto =
        service.patch(trainId, jsonPatch)
}
