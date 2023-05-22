package ro.fasttrackit.course10.homework.model.mappers

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ro.fasttrackit.course10.homework.model.dto.TrainDto
import ro.fasttrackit.course10.homework.model.entity.Train

@Mapper
interface TrainMapperMapstruct {
    companion object {
        @JvmField
        val INSTANCE: TrainMapperMapstruct = Mappers.getMapper(TrainMapperMapstruct::class.java)
    }

    fun toApi(train: Train): TrainDto

    fun toEntity(trainDto: TrainDto): Train
}
