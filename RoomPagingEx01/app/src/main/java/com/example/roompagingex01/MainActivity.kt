package com.example.roompagingex01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roompagingex01.database.AppDatabase
import com.example.roompagingex01.database.NoteEntity
import com.example.roompagingex01.dialog.NoteCreateDialog
import com.example.roompagingex01.list.NoteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    //noteDao
    val noteDao by lazy { AppDatabase.getDatabase(this).noteDao() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* noteAdapter 생성 및 deleteCallback(it)을 받아 처리할 콜백 작성
           - NoteAdpater에서 삭제 버튼을 클릭하면 삭제를 자신을 만든 MainAcitivity에
             deleteCallback(it)을 통해 위임함
           - 따라서 deleteCallback(it)을 받아 노트를 삭제할 코드를 람다식으로 작성
         */
        val noteAdapter = NoteAdapter(noteDao) { note ->
            Log.d("TAG", "MainActivity-delete")
            /* 노트 Delete */
            launch(coroutineContext + Dispatchers.IO) {
                noteDao.deleteNots(note)
            }
        }

        /* RecycleView Initialize*/
        recycle_note.adapter = noteAdapter
        recycle_note.layoutManager = LinearLayoutManager(this)


        /* LivePagedListBuilder(data-Source, page-size)로 PageList 생성
           - 반환값이 LiveData로 Observable 패턴과 같이 데이터의 변동사항이 있을때 계속 데이터를 갱신
           - LivePagedListBuilder에 Page 속성과 DataSource를 정의하고 빌드하면,
             LiveData<PagedList<Item>로 pageList를 반환
        */
        val getDataFromDB: LiveData<PagedList<NoteEntity>> = LivePagedListBuilder(noteDao.selectNotes(), 20).build()

        /* observe() 메소드로 Observer 등록하면, 데이터 변경시 자동 호출됨
           - LiveDta로부터 변경된 PageList가 넘어오면 값을 Adapter에 전달
           - param owner : Observer를 제어하는 LifecycleOwner -
           - param Observer : LiveDta 로부터 이벤트를 수신(데이터가 변경되면 호출됨)
        */
        getDataFromDB.observe(this, Observer {
            Log.d("TAG", "MainActivity-observe()")
            /* 변경된 pageList를 PagedAdapter에 전달
               - PagedList 를 submit하면 PagedListAdapter 의 AsyncPagedListDiffer 는
                 기존에 PagedList가 존재하면 그 차이를 비교한 후 리사이클러뷰를 새로운 페이지로 갱신
           */
            noteAdapter.submitList(it)
        })

        /* "+"(fab_add_note) 버튼을 클릭하면 NoteCreateDialog 출력  */
        fab_add_note.setOnClickListener {
            Log.d("TAG", "MainActivity-NoteCreateDialog()")
            NoteCreateDialog().show(supportFragmentManager, null)
        }
    }//end of onCreate
}