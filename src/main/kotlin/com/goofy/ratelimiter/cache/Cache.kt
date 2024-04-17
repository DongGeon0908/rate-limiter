package com.goofy.ratelimiter.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.time.Duration

class Cache<VALUE_TYPE>(
    val key: String,
    val type: TypeReference<VALUE_TYPE>,
    val duration: Duration,
) {
    companion object Factory {
        fun rateLimiter(key: String, duration: Duration): Cache<Long> {
            return Cache(
                key = key,
                type = jacksonTypeRef(),
                duration = duration
            )
        }
    }
}
