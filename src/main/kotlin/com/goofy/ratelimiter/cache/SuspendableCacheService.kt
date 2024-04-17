package com.goofy.ratelimiter.cache

import kotlinx.coroutines.reactive.awaitFirstOrNull
import mu.KotlinLogging
import org.springframework.data.domain.Range
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import kotlin.coroutines.cancellation.CancellationException

@Service
class SuspendableCacheService(
    private val reactiveStringRedisTemplate: ReactiveStringRedisTemplate,
) : CacheService {
    private val logger = KotlinLogging.logger { }
    private val zSetOps = reactiveStringRedisTemplate.opsForZSet()
    override suspend fun <VALUE_TYPE : Any> rateLimit(
        cache: Cache<VALUE_TYPE>,
        range: Range<Double>,
        score: Int
        limitScore: Long
    ): VALUE_TYPE {
        val score: String = runCatching {
            zSetOps.add(cache.key, 1)
            zSetOps.rangeByScore(cache.key, range).awaitFirstOrNull()
        }.onFailure { e ->
            when (e) {
                is CancellationException -> logger.debug { "Redis Read job cancelled." }
                else -> logger.error(e) { "fail to read data from redis. key : ${cache.key}" }
            }
        }.getOrNull() ?: throw RuntimeException("Fail to Rate Limit : key ${cache.key}")

        if (limitScore < score as VALUE_TYPE) {

        }

        // TODO 일단 여기까지만~
    }
}
