package ro.fasttrackit.course10.homework.model.mappers

import org.springframework.stereotype.Component
import ro.fasttrackit.course10.homework.model.dto.LocationDto
import ro.fasttrackit.course10.homework.model.entity.Location

@Component
class LocationMapper : ModelMapper<LocationDto, Location> {
    override fun toApi(source: Location): LocationDto = LocationDto(id = source.id, city = source.city)

    override fun toEntity(source: LocationDto): Location = Location(id = source.id, city = source.city)
}
