package com.subin.cleanbookstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.subin.cleanbookstore.data.local.dao.BookmarkDao
import com.subin.cleanbookstore.data.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}