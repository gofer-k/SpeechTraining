package com.gofer.speechtraining.ui

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache

class SpeechTrainingApplication: Application(), ImageLoaderFactory {
  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(this)
          .memoryCache {
            MemoryCache.Builder(this)
              .maxSizePercent(0.20)
              .build()
          }
          .diskCache {
            DiskCache.Builder()
              .directory(cacheDir.resolve("image_cache"))
              .maxSizeBytes(5 * 1024 * 1024)
              .build()
          }
//          .logger(DebugLogger())
          .respectCacheHeaders(false)
          .build()
  }
}