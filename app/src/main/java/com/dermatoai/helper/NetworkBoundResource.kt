package com.dermatoai.helper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * A helper function for managing data that comes from both a local database and a remote network source.
 * This function determines whether to fetch data from the network or use cached data in the database.
 *
 * @param ResultType The type of the data retrieved from the local database.
 * @param RequestType The type of the data retrieved from the remote network source.
 * @param query A lambda function that provides a Flow of the data from the local database.
 * @param fetch A suspend function that fetches data from the remote network source.
 * @param saveFetchResult A suspend function that saves the fetched data into the local database.
 * @param shouldFetch A lambda function that determines whether to fetch data from the network.
 *                     By default, it always returns true.
 *
 * @return A Flow emitting Resource objects, representing the state of the data:
 *         - [Resource.Loading]: Emitted when a network request is in progress.
 *         - [Resource.Success]: Emitted when data is successfully loaded from the database or network.
 *         - [Resource.Error]: Emitted when an error occurs during the network fetch, with the current database data.
 *
 * @sample
 * ```kotlin
 * val result = networkBoundResource(
 *     query = { userDao.getUser(id) },
 *     fetch = { apiService.fetchUser(id) },
 *     saveFetchResult = { userDao.insertUser(it) },
 *     shouldFetch = { user -> user == null || shouldRefreshData(user) }
 * )
 *
 * result.collect { resource ->
 *     when (resource) {
 *         is Resource.Loading -> showLoadingUI()
 *         is Resource.Success -> showData(resource.data)
 *         is Resource.Error -> showError(resource.error)
 *     }
 * }
 * ```
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    //First step, fetch data from the local cache
    val data = query().first()

    //If shouldFetch returns true,
    val resource = if (shouldFetch(data)) {

        //Dispatch a message to the UI that you're doing some background work
        emit(Resource.Loading(data))

        try {

            //make a networking call
            val resultType = fetch()

            //save it to the database
            saveFetchResult(resultType)

            //Now fetch data again from the database and Dispatch it to the UI
            query().map { Resource.Success(it) }

        } catch (throwable: Throwable) {

            //Dispatch any error emitted to the UI, plus data emmited from the Database
            query().map { Resource.Error(throwable, it) }

        }

        //If should fetch returned false
    } else {
        //Make a query to the database and Dispatch it to the UI.
        query().map { Resource.Success(it) }
    }

    //Emit the resource variable
    emitAll(resource)
}