package ro.fasttrackit.course10.homework.model.pagination

data class PageInfo(
    val totalSize: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int
)
