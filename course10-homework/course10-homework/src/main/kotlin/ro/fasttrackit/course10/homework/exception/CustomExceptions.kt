package ro.fasttrackit.course10.homework.exception

class ValidationException(message: String) : RuntimeException(message)

class ResourceNotFoundException(message: String) : RuntimeException(message)
