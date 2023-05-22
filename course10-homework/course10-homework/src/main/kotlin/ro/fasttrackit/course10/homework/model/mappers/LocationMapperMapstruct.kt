package ro.fasttrackit.course10.homework.model.mappers

import org.mapstruct.Mapper
import ro.fasttrackit.course10.homework.model.dto.LocationDto
import ro.fasttrackit.course10.homework.model.entity.Location

@Mapper(componentModel = "spring")
interface LocationMapperMapstruct {
    fun toApi(location: Location): LocationDto
    fun toEntity(locationDto: LocationDto): Location
    fun toApi(locations: List<Location>): List<LocationDto>
    fun toEntity(locationDtos: List<LocationDto>): List<Location>
}
