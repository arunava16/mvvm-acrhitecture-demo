package com.arunava.example.tmdbdemo.service.repository

import io.reactivex.Observable
import io.reactivex.Single

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResultType, RequestType> {

    fun getResource(): Observable<Resource<ResultType>> {
        return Observable.create { emitter ->

            // Emit loading signal with null data initially
            emitter.onNext(Resource.Loading())

            // Load data from db
            val dbData = loadFromDb()

            // Check if data need to be fetched from network
            if (shouldFetch(dbData)) {

                // Emit loading with db data
                emitter.onNext(Resource.Loading(dbData))

                // And then fetch new data from network
                networkFetch().subscribe(
                    { requestType ->

                        saveFetchResult(requestType)
                        emitter.onNext(Resource.Success(loadFromDb()))
                    },
                    {
                        onFetchFailed()
                        emitter.onNext(Resource.Error(it.message.toString(), dbData))
                    })
            } else {
                // Emit db data with success signal
                emitter.onNext(Resource.Success(dbData))
            }
        }
    }

    /**
     * For fetching the data from the network
     */
    protected abstract fun networkFetch(): Single<RequestType>

    /**
     * For saving the network fetched data into the local db
     */
    protected abstract fun saveFetchResult(data: RequestType)

    /**
     * For getting the data from the local db
     */
    protected abstract fun loadFromDb(): ResultType?

    /**
     * Returns if data needs to be fetched from network. True by default
     */
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    /**
     * For extra action that needs to be taken in case network fetch is failed
     */
    protected open fun onFetchFailed() {}
}