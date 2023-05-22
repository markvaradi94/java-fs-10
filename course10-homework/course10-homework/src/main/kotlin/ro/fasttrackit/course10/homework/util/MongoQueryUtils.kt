package ro.fasttrackit.course10.homework.util

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.query.Criteria
import ro.fasttrackit.course10.homework.model.pagination.CollectionResponse
import ro.fasttrackit.course10.homework.model.pagination.PageInfo
import java.util.function.Function

object MongoQueryUtils {
    fun <T> inOrIs(criteria: Criteria, tokens: List<T>, key: String) {
        if (tokens.size == 1) {
            criteria.and(key).`is`(tokens[0])
        } else {
            criteria.and(key).`in`(tokens)
        }
    }

    fun <T> toCollectionResponse(elements: Page<T>): CollectionResponse<T> =
        CollectionResponse(
            items = elements.content,
            pageInfo = PageInfo(
                totalSize = elements.totalElements,
                totalPages = elements.totalPages,
                currentPage = elements.pageable.pageNumber,
                pageSize = elements.pageable.pageSize
            )
        )

    fun <D, T> mapPagedContent(mappingFunction: Function<D, T>, page: Page<D>, pageable: Pageable): Page<T> {
        return PageImpl(page.content.map { mappingFunction.apply(it) }, pageable, page.totalElements)
    }

    fun multipleIgnoreCase(criteria: Criteria, values: List<String>, fieldName: String) {
        val valueCriteria = values.map { Criteria.where(fieldName).regex(escapeSpecialRegexCharacters(it), "i") }
            .toMutableList()
            .toTypedArray()
        criteria.orOperator(*valueCriteria)
    }

    private fun escapeSpecialRegexCharacters(input: String): String =
        input.replace("([\\\\.*\\[\\]^$+?{}()|])".toRegex(), "\\\\$1")
}
