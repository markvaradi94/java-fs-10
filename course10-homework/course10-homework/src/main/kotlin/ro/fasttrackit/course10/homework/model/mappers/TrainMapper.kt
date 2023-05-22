package ro.fasttrackit.course10.homework.model.mappers

import org.springframework.stereotype.Component
import ro.fasttrackit.course10.homework.model.dto.TrainDto
import ro.fasttrackit.course10.homework.model.entity.Train

@Component
class TrainMapper : ModelMapper<TrainDto, Train> {
    override fun toApi(source: Train): TrainDto =
        TrainDto(
            id = source.id,
            model = source.model,
            carts = source.carts,
            locationId = source.locationId
        )

    override fun toEntity(source: TrainDto): Train =
        Train(
            id = source.id,
            model = source.model,
            carts = source.carts,
            locationId = source.locationId
        )
}
