package ro.fasttrackit.course10.homework.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ro.fasttrackit.course10.homework.exception.ResourceNotFoundException
import ro.fasttrackit.course10.homework.model.dto.RouteDto
import ro.fasttrackit.course10.homework.model.entity.Route
import ro.fasttrackit.course10.homework.model.filters.RouteFilters
import ro.fasttrackit.course10.homework.model.mappers.RouteMapper
import ro.fasttrackit.course10.homework.repository.RouteRepository
import ro.fasttrackit.course10.homework.util.MongoQueryUtils.mapPagedContent
import ro.fasttrackit.course10.homework.validator.RouteValidator
import java.util.*

@Service
class RouteService(
    private val repository: RouteRepository,
    private val validator: RouteValidator,
    private val routeMapper: RouteMapper,
    private val objectMapper: ObjectMapper
) {

    fun findAll(filters: RouteFilters, pageable: Pageable): Page<RouteDto> =
        mapPagedContent(
            routeMapper::toApi,
            repository.findAll(filters, pageable),
            pageable
        )

    fun findOne(routeId: String): Optional<RouteDto> = repository.findById(routeId).map(routeMapper::toApi)

    fun add(newRoute: RouteDto): RouteDto {
        validator.validateNewThrow(routeMapper.toEntity(newRoute))
        return routeMapper.toApi(repository.addRoute(routeMapper.toEntity(newRoute)))
    }

    fun delete(routeId: String): Optional<RouteDto> {
        validator.validateExistsOrThrow(routeId)
        return repository.deleteById(routeId)
            .map(routeMapper::toApi)
    }

    fun patch(routeId: String, patch: JsonPatch): RouteDto {
        validator.validateExistsOrThrow(routeId)
        val routeToPatch = repository.findById(routeId)
            .orElseThrow { ResourceNotFoundException("Could not find route with id $routeId for update.") }
        val patchedRouteJson = patch.apply(objectMapper.valueToTree(routeToPatch))
        val patchedRoute = objectMapper.treeToValue(patchedRouteJson, Route::class.java)
        validator.validateReplaceOrThrow(routeId, patchedRoute)
        return routeMapper.toApi(repository.addRoute(patchedRoute))
    }
}
