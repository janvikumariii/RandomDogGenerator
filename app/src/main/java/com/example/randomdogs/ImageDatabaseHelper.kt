package com.example.randomdogs

object ImageDatabaseHelper {

    private val cacheSize = 20
    val imageUrls = mutableListOf<String>()
    val lruCache = androidx.collection.LruCache<String, String>(cacheSize)

    fun addImageUrl(url: String) {
        imageUrls.add(url)
        lruCache.put(url, url)
        if (lruCache.size() > cacheSize) {
            val leastRecentlyUsedUrl = lruCache.snapshot().keys.first()
            lruCache.remove(leastRecentlyUsedUrl)
            imageUrls.remove(leastRecentlyUsedUrl)
        }
    }

    fun getAllImageUrls(): List<String> {
        return lruCache.snapshot().keys.toList().asReversed()
    }
    fun clearUrls() {
        imageUrls.clear()
        lruCache.evictAll()

    }
}



