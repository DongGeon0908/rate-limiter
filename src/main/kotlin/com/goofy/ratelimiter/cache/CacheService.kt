package com.goofy.ratelimiter.cache

import org.springframework.data.domain.Range

interface CacheService {
    suspend fun <VALUE_TYPE : Any> rateLimit(
        cache: Cache<VALUE_TYPE>,
        range: Range<Double>,
        limitScore: Long,
    ): VALUE_TYPE

    companion object {
        suspend fun <VALUE_TYPE : Any> CacheService.rateLimit(
            cache: Cache.Factory.() -> Cache<VALUE_TYPE>,
            range: Range<Double>,
            limitScore: Long,
        ) = rateLimit(cache(Cache), range, limitScore)
    }
}
