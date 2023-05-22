package ro.fasttrackit.course10.homework.model.pagination

data class CollectionResponse<T>(
    val items: List<T>,
    val pageInfo: PageInfo
)
