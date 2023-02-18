package com.perco.interview.core.repo.local.cache

class CachePolicy<T>(
    ttlMs: Long,
    softTtlMs: Long = Long.MAX_VALUE,
    val ttlResolver: (T) -> Long = { ttlMs },
    val softTtlResolver: (T) -> Long = { softTtlMs }
)