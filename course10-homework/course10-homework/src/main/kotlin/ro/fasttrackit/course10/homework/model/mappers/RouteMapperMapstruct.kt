package ro.fasttrackit.course10.homework.model.mappers

import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ro.fasttrackit.course10.homework.model.dto.RouteDto
import ro.fasttrackit.course10.homework.model.entity.Route

@Mapper
interface RouteMapperMapstruct {
    companion object {
        @JvmField
        val INSTANCE: RouteMapperMapstruct = Mappers.getMapper(RouteMapperMapstruct::class.java)
    }

    @Mapping(source = "start.city", target = "start")
    @Mapping(source = "destination.city", target = "destination")
    fun toApi(route: Route): RouteDto

    @InheritInverseConfiguration
    fun toEntity(routeDto: RouteDto): Route
}
