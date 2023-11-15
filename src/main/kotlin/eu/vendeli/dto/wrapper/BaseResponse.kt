package eu.vendeli.dto.wrapper

data class BaseResponse<T>(
    val code: Int,
    val data: T
)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> BaseResponse<T>.asSuccess() = Response.Success(this)
@Suppress("NOTHING_TO_INLINE")
inline fun BaseResponse<Any?>.asFailure() = Response.Failure(this)

inline fun <T> successResponse(code: Int = 200, block: () -> T) = BaseResponse(code, block()).asSuccess()
inline fun failureResponse(code: Int = 400, block: () -> Any?) = BaseResponse(code, block()).asFailure()