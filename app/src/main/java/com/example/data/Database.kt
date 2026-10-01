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

@Entity(tableName = "study_blocks")
data class StudyBlock(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val description: String = "",
    val color: Long,
    val startTime: Long = 0L,
    val duration: Int = 25,
    val repeatRule: String? = null,
    val reminderEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sessions")
data class TimerSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameSnapshot: String = "",
    val colorSnapshot: Long? = null,
    val studyBlockId: Long? = null,
    val sessionType: String = "Focus",
    val startTime: Long = 0L,
    val duration: Int = 0,
    val endTime: Long = startTime + duration * 60000L
) {
    @Ignore
    constructor(
        taskName: String,
        isBreak: Boolean,
        durationMinutes: Int,
        startTime: Long,
        endTime: Long,
        taskColor: Long? = null,
        studyBlockId: Long? = null
    ) : this(
        id = 0,
        nameSnapshot = taskName,
        colorSnapshot = taskColor,
        studyBlockId = studyBlockId,
        sessionType = if (isBreak) "Break" else "Focus",
        startTime = startTime,
        duration = durationMinutes,
        endTime = endTime
    )

    val taskName: String get() = nameSnapshot.ifBlank { if (isBreak) "Break" else "Focus Session" }
    val taskColor: Long? get() = colorSnapshot
    val isBreak: Boolean get() = sessionType.equals("Break", ignoreCase = true)
    val durationMinutes: Int get() = duration
}

@Dao
interface StudyBlockDao {
    @Query("SELECT * FROM study_blocks ORDER BY createdAt DESC")
    fun getAllStudyBlocks(): Flow<List<StudyBlock>>

    @Query("SELECT * FROM study_blocks")
    suspend fun getAllStudyBlocksList(): List<StudyBlock>

    @Query("SELECT * FROM study_blocks WHERE id = :id")
    suspend fun getStudyBlockById(id: Long): StudyBlock?

    @Query("SELECT * FROM study_blocks WHERE id = :id")
    fun observeStudyBlockById(id: Long): Flow<StudyBlock?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyBlock(studyBlock: StudyBlock): Long

    @Update
    suspend fun updateStudyBlock(studyBlock: StudyBlock)

    @Delete
    suspend fun deleteStudyBlock(studyBlock: StudyBlock)

    @Query("DELETE FROM study_blocks WHERE id = :id")
    suspend fun deleteStudyBlockById(id: Long)
}

class StudyBlockRepository(
    private val studyBlockDao: StudyBlockDao,
    private val sessionDao: SessionDao
) {
    val allStudyBlocks: Flow<List<StudyBlock>> = studyBlockDao.getAllStudyBlocks()

    suspend fun getById(id: Long): StudyBlock? = studyBlockDao.getStudyBlockById(id)

    fun observeById(id: Long): Flow<StudyBlock?> = studyBlockDao.observeStudyBlockById(id)

    suspend fun create(studyBlock: StudyBlock): Long = studyBlockDao.insertStudyBlock(studyBlock)

    suspend fun update(studyBlock: StudyBlock) = studyBlockDao.updateStudyBlock(studyBlock)

    suspend fun delete(studyBlock: StudyBlock) = studyBlockDao.deleteStudyBlock(studyBlock)

    suspend fun deleteById(id: Long) = studyBlockDao.deleteStudyBlockById(id)

    suspend fun createQuickFocusSession(
        name: String = "Quick Focus",
        durationMinutes: Int = 25,
        color: Long? = null
    ): TimerSession {
        val now = System.currentTimeMillis()
        val session = TimerSession(
            nameSnapshot = name,
            colorSnapshot = color,
            studyBlockId = null,
            sessionType = "Focus",
            startTime = now,
            duration = durationMinutes,
            endTime = now + durationMinutes * 60000L
        )
        sessionDao.insertSession(session)
        return session
    }

    suspend fun startSessionFromStudyBlock(
        studyBlock: StudyBlock,
        sessionType: String = "Focus"
    ): TimerSession {
        val now = System.currentTimeMillis()
        val session = TimerSession(
            nameSnapshot = studyBlock.name,
            colorSnapshot = studyBlock.color,
            studyBlockId = studyBlock.id,
            sessionType = sessionType,
            startTime = now,
            duration = studyBlock.duration,
            endTime = now + studyBlock.duration * 60000L
        )
        sessionDao.insertSession(session)
        return session
    }
}

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

    @Query("DELETE FROM sessions WHERE nameSnapshot = :taskName")
    suspend fun deleteSessionsByTask(taskName: String)

    @Query("SELECT * FROM sessions WHERE studyBlockId = :studyBlockId ORDER BY startTime DESC")
    fun getSessionsForStudyBlock(studyBlockId: Long): Flow<List<TimerSession>>

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

@Database(entities = [StudyBlock::class, TaskItem::class, TimerSession::class, AlarmItem::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyBlockDao(): StudyBlockDao
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

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE sessions ADD COLUMN taskColor INTEGER DEFAULT NULL")
        }
    }

    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            try {
                database.execSQL("ALTER TABLE sessions ADD COLUMN nameSnapshot TEXT NOT NULL DEFAULT ''")
            } catch (e: Exception) {}
            try {
                database.execSQL("ALTER TABLE sessions ADD COLUMN colorSnapshot INTEGER DEFAULT NULL")
            } catch (e: Exception) {}
            try {
                database.execSQL("ALTER TABLE sessions ADD COLUMN studyBlockId INTEGER DEFAULT NULL")
            } catch (e: Exception) {}
            try {
                database.execSQL("ALTER TABLE sessions ADD COLUMN sessionType TEXT NOT NULL DEFAULT 'Focus'")
            } catch (e: Exception) {}
            try {
                database.execSQL("ALTER TABLE sessions ADD COLUMN duration INTEGER NOT NULL DEFAULT 0")
            } catch (e: Exception) {}
            try {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `study_blocks` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`name` TEXT NOT NULL, " +
                        "`description` TEXT NOT NULL DEFAULT '', " +
                        "`color` INTEGER NOT NULL, " +
                        "`startTime` INTEGER NOT NULL DEFAULT 0, " +
                        "`duration` INTEGER NOT NULL DEFAULT 25, " +
                        "`repeatRule` TEXT, " +
                        "`reminderEnabled` INTEGER NOT NULL DEFAULT 0, " +
                        "`createdAt` INTEGER NOT NULL DEFAULT 0" +
                    ")"
                )
            } catch (e: Exception) {}
        }
    }

    fun getDatabase(context: android.content.Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pomelo_db_v3_stable"
            )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_5_6)
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
            INSTANCE = instance
            instance
        }
    }
}
