package com.example.roompagingex01.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/* @Entity Annotation으로 테이블 정의
   - Entity 어노테이션을통해 테이블에 들어갈 row 를 정의
   - tableName은 생략가능.(생략할경우 data class의 이름이 tablename)
 */
@Entity(tableName = "NoteEntity")
data class  NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var noteIdx: Int? = null,
    val noteContent: String
)

/* Primary Key를 직접 설정하는경우
   - @PrimaryKey
     var ExName: String */

/* 복합 Primary Key를 사용하는경우
  @Entity(primaryKeys = arrayOf("firstName", "lastName")) */

/* Primary Key를 자동 생성하는 경우
  - PrimaryKey Annotation으로 Primary Key 정의.
  - 이때 반드시 Long 혹은 Int 타입 */