package com.dermatoai.helper

/**
 * class the represented state of resource.
 * @param data using for success conditions.
 * @param error using for error conditions.
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    /**
     * class using for loading state
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * class using for success state
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * class using for error state
     */
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}