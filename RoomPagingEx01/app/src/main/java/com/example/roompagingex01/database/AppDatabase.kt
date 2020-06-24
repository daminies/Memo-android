package com.example.roompagingex01.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/* AppDatabse 선언 - RoomDatabase() 상속
   - entities 어노테이션으로 데이터베이스에 포함된 엔티티 목록 선언
   - 데이터베이스의 구조가 바뀌면 Version 변경
*/
@Database(entities = arrayOf(NoteEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    /* Database access하는데 사용하는 Dao를 가져온다. */
    abstract fun noteDao(): NoteDao

    /* database를 전역으로 사용하기 위해 companion object(final static)으로 선언 */
    companion object {
        // database 변수 선언
        private var database: AppDatabase? = null

        //database 이름 상수 선언
        private const val ROOM_DB = "note.db"

        /* Database 객체를 생성하여 반환하는 함수 */
        fun getDatabase(context: Context): AppDatabase {
            if (database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, ROOM_DB
                ).build()
            }
            /* 안전한 강제 캐스팅 */
            return database!!
        }
    }
}
