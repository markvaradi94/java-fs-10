package ro.fasttrackit.course10.homework.model.mappers

import org.springframework.stereotype.Component
import ro.fasttrackit.course10.homework.model.dto.RouteDto
import ro.fasttrackit.course10.homework.model.entity.Route

@Component
class RouteMapper : ModelMapper<RouteDto, Route> {
    override fun toApi(source: Route): RouteDto =
        RouteDto(
            id = source.id,
            startId = source.startId,
            destinationId = source.destinationId,
            trainId = source.trainId
        )

    override fun toEntity(source: RouteDto): Route =
        Route(
            id = source.id,
            startId = source.startId,
            destinationId = source.destinationId,
            trainId = source.trainId
        )
}
