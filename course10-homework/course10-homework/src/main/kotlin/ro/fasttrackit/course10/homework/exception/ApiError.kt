package ro.fasttrackit.course10.homework.exception

data class ApiError(
    val errorCode: String = "DEFAULT_CODE",
    val message: String
)
