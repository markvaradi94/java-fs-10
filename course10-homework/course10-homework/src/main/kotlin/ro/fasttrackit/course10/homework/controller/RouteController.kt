package ro.fasttrackit.course10.homework.controller

import com.github.fge.jsonpatch.JsonPatch
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import ro.fasttrackit.course10.homework.exception.ResourceNotFoundException
import ro.fasttrackit.course10.homework.model.dto.RouteDto
import ro.fasttrackit.course10.homework.model.filters.RouteFilters
import ro.fasttrackit.course10.homework.model.pagination.CollectionResponse
import ro.fasttrackit.course10.homework.service.RouteService
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.toCollectionResponse

@RestController
@RequestMapping("routes")
class RouteController(private val service: RouteService) {

    @GetMapping
    fun findAll(routeFilters: RouteFilters, pageable: Pageable): CollectionResponse<RouteDto> =
        toCollectionResponse(service.findAll(routeFilters, pageable))

    @GetMapping("{routeId}")
    fun findOne(@PathVariable routeId: String): RouteDto = service.findOne(routeId)
        .orElseThrow { ResourceNotFoundException("Could not find route with id $routeId") }

    @PostMapping
    fun add(@RequestBody newRoute: RouteDto): RouteDto = service.add(newRoute)

    @DeleteMapping("{routeId}")
    fun delete(@PathVariable routeId: String): RouteDto = service.delete(routeId)
        .orElseThrow { ResourceNotFoundException("Could not find route with id $routeId") }

    @PatchMapping("{routeId}")
    fun patch(@PathVariable routeId: String, @RequestBody jsonPatch: JsonPatch): RouteDto =
        service.patch(routeId, jsonPatch)
}
