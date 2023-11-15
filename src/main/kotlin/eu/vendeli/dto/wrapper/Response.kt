package eu.vendeli.dto.wrapper

import com.fasterxml.jackson.annotation.JsonValue

sealed class Response<T> {
    data class Success<T>(@JsonValue val data: BaseResponse<T>) : Response<T>()

    data class Failure(@JsonValue val failure: BaseResponse<Any?>) : Response<Any?>()
}