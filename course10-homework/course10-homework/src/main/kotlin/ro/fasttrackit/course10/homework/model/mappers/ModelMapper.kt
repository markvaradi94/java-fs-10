package ro.fasttrackit.course10.homework.model.mappers

interface ModelMapper<A, E> {
    fun toApi(source: E): A
    fun toEntity(source: A): E

    fun toApi(source: List<E>): List<A> = source.map { toApi(it) }
    fun toEntity(source: List<A>): List<E> = source.map { toEntity(it) }
}
