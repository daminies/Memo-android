package com.example.roompagingex01.database

import android.provider.ContactsContract
import androidx.paging.DataSource
import androidx.room.*

/* DAO(Data Access Object) 정의
   - 데이터베이스에 엑세스하는데 사용되는 메서드(insert, query, update, delete) 선언
*/
@Dao
interface NoteDao {

    /* Insert Annotation으로 Insert를 정의
       - key 중복시 Strategy 설정
       - 파라미터로 객체를 전달하면, 값 매칭은 Room이 인스턴스 변수를 보고 자동 처리
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(vararg notes: NoteEntity)

    /* Query Annotation으로 쿼리를 정의
       - FROM 절로 넘긴 테이블과 매칭되는 모델로 반환값을 선언하면 room이 자동으로 객체를 매핑

       - ROOM의 DataSource.Factory를 이용하여 LiveData<PagedList>를 생성(PageList 인스턴스)
       - DataSource.Factory를 통해 DataSource에서 Data를 가져와서 빌더를 이용해 새로운 PageList를 생성
       - LiveData 설정
    */
    @Query("SELECT * FROM NoteEntity ORDER BY noteContent")
    fun selectNotes(): DataSource.Factory<Int, NoteEntity>

    /* Query Annotation으로 쿼리를 정의
       - 파라미터로 전달할 값을 콜론(:) 뒤에 같은 이름으로 선언(:noteIdx)
       - FROM 절로 넘긴 테이블과 매칭되는 모델로 반환값을 선언하면 room이 자동으로 객체를 매핑
     */
    @Query("SELECT * FROM NoteEntity WHERE noteIdx = :noteIdx")
    fun selectNote(noteIdx: Int): NoteEntity

    /* Update Annotation으로 Update를 정의
      - 파라미터로 객체를 전달하면, 값 매칭은 Room이 인스턴스 변수를 보고 자동 처리
    */
    @Update
    fun updateNote(vararg notes: NoteEntity)

    /*Delete Annotation으로 Delete를 정의
     - 파라미터로 객체를 전달하면, 값 매칭은 Room이 인스턴스 변수를 보고 자동 처리
   */
    @Delete
    fun deleteNots(vararg note: NoteEntity)
}