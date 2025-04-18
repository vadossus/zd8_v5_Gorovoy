package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Bus::class, Driver::class, Route::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busDao(): BusDao
    abstract fun driverDao(): DriverDao
    abstract fun routeDao(): RouteDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new tables
        database.execSQL("CREATE TABLE IF NOT EXISTS `drivers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `licenseNumber` TEXT)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `routes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `routeName` TEXT, `busId` INTEGER, FOREIGN KEY(`busId`) REFERENCES `buses`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
    }
}

fun createDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    )
        .addMigrations(MIGRATION_1_2) // Add migration here
        .build()
}