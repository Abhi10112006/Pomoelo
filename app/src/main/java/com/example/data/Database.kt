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
    val taskName: String,
    val isBreak: Boolean,
    val durationMinutes: Int,
    val startTime: Long,
    val endTime: Long
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY timestamp DESC")
    fun getAllTasks(): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem)

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

@Entity(tableName = "alarms")
data class AlarmItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val label: String,
    val isEnabled: Boolean = true,
    val daysOfWeek: String = "Daily", // e.g. "Daily" or "2,3,4,5,6" (Monday-Friday) 
    val squatTarget: Int = 10
)

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms ORDER BY hour ASC, minute ASC")
    fun getAllAlarms(): Flow<List<AlarmItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmItem): Long

    @Update
    suspend fun updateAlarm(alarm: AlarmItem)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmItem)

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Int): AlarmItem?
}

@Database(entities = [TaskItem::class, TimerSession::class, AlarmItem::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
    abstract fun alarmDao(): AlarmDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `taskName` TEXT NOT NULL, `isBreak` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL)"
            )
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `alarms` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `hour` INTEGER NOT NULL, `minute` INTEGER NOT NULL, `label` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, `daysOfWeek` TEXT NOT NULL, `squatTarget` INTEGER NOT NULL)"
            )
        }
    }

    fun getDatabase(context: android.content.Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pomelo_db"
            )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
            INSTANCE = instance
            instance
        }
    }
}
