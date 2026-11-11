package com.example.data

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val categoryName: String,
    val categoryColor: Long, // ARGB format
    val completedPomodoros: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "sessions")
data class TimerSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskName: String = "",
    val isBreak: Boolean = false,
    val durationMinutes: Int = 0,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val taskColor: Long? = null
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY timestamp DESC")
    fun getAllTasks(): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem): Long

    @Query("UPDATE tasks SET completedPomodoros = completedPomodoros + 1 WHERE id = :id")
    suspend fun incrementPomodoroCount(id: Int)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("DELETE FROM tasks WHERE timestamp < :timestamp")
    suspend fun deleteTasksBefore(timestamp: Long)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY endTime DESC")
    fun getAllSessions(): Flow<List<TimerSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TimerSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessions(sessions: List<TimerSession>)

    @Delete
    suspend fun deleteSession(session: TimerSession)

    @Query("DELETE FROM sessions WHERE startTime >= :start AND startTime < :end")
    suspend fun deleteSessionsInRange(start: Long, end: Long)

    @Query("DELETE FROM sessions WHERE taskName = :taskName")
    suspend fun deleteSessionsByTask(taskName: String)

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()
}

@Database(entities = [TaskItem::class, TimerSession::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    private fun migrateToV8(db: SupportSQLiteDatabase) {
        try {
            db.execSQL("DROP TABLE IF EXISTS `study_blocks`")
            db.execSQL("DROP TABLE IF EXISTS `alarms`")

            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `tasks_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `categoryName` TEXT NOT NULL, `categoryColor` INTEGER NOT NULL, `completedPomodoros` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)"
            )
            try {
                db.execSQL(
                    "INSERT INTO `tasks_new` (`id`, `name`, `categoryName`, `categoryColor`, `completedPomodoros`, `timestamp`) SELECT `id`, `name`, `categoryName`, `categoryColor`, `completedPomodoros`, `timestamp` FROM `tasks`"
                )
            } catch (_: Throwable) {}
            db.execSQL("DROP TABLE IF EXISTS `tasks`")
            db.execSQL("ALTER TABLE `tasks_new` RENAME TO `tasks`")

            val sessionCols = mutableSetOf<String>()
            try {
                db.query("PRAGMA table_info(`sessions`)").use { cursor ->
                    val nameIdx = cursor.getColumnIndex("name")
                    while (cursor.moveToNext()) {
                        if (nameIdx >= 0) {
                            sessionCols.add(cursor.getString(nameIdx))
                        }
                    }
                }
            } catch (_: Throwable) {}

            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `sessions_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `taskName` TEXT NOT NULL, `isBreak` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `taskColor` INTEGER)"
            )

            try {
                if (sessionCols.contains("nameSnapshot") && sessionCols.contains("duration")) {
                    db.execSQL(
                        "INSERT INTO `sessions_new` (`id`, `taskName`, `isBreak`, `durationMinutes`, `startTime`, `endTime`, `taskColor`) " +
                        "SELECT `id`, COALESCE(`nameSnapshot`, 'Focus Session'), CASE WHEN `sessionType` = 'BREAK' THEN 1 ELSE 0 END, COALESCE(`duration`, 25), COALESCE(`startTime`, 0), COALESCE(`endTime`, 0), `colorSnapshot` FROM `sessions`"
                    )
                } else if (sessionCols.contains("taskName")) {
                    if (sessionCols.contains("taskColor")) {
                        db.execSQL(
                            "INSERT INTO `sessions_new` (`id`, `taskName`, `isBreak`, `durationMinutes`, `startTime`, `endTime`, `taskColor`) " +
                            "SELECT `id`, `taskName`, `isBreak`, `durationMinutes`, `startTime`, `endTime`, `taskColor` FROM `sessions`"
                        )
                    } else {
                        db.execSQL(
                            "INSERT INTO `sessions_new` (`id`, `taskName`, `isBreak`, `durationMinutes`, `startTime`, `endTime`, `taskColor`) " +
                            "SELECT `id`, `taskName`, `isBreak`, `durationMinutes`, `startTime`, `endTime`, NULL FROM `sessions`"
                        )
                    }
                }
            } catch (_: Throwable) {}

            db.execSQL("DROP TABLE IF EXISTS `sessions`")
            db.execSQL("ALTER TABLE `sessions_new` RENAME TO `sessions`")
        } catch (_: Throwable) {
            db.execSQL("DROP TABLE IF EXISTS `tasks`")
            db.execSQL("DROP TABLE IF EXISTS `sessions`")
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `categoryName` TEXT NOT NULL, `categoryColor` INTEGER NOT NULL, `completedPomodoros` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)"
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `taskName` TEXT NOT NULL, `isBreak` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `taskColor` INTEGER)"
            )
        }
    }

    private fun migration(from: Int, to: Int) = object : Migration(from, to) {
        override fun migrate(db: SupportSQLiteDatabase) {
            migrateToV8(db)
        }
    }

    fun getDatabase(context: android.content.Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pomelo_db_v3_stable"
            )
            .addMigrations(
                migration(1, 8),
                migration(2, 8),
                migration(3, 8),
                migration(4, 8),
                migration(5, 8),
                migration(6, 8),
                migration(7, 8)
            )
            .fallbackToDestructiveMigration(true)
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .build()
            INSTANCE = instance
            instance
        }
    }
}

