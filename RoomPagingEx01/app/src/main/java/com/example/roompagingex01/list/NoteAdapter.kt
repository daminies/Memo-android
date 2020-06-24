package com.example.roompagingex01.list

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roompagingex01.R
import com.example.roompagingex01.database.NoteDao
import com.example.roompagingex01.database.NoteEntity
import com.example.roompagingex01.dialog.NoteUpdateDialog
import kotlinx.android.synthetic.main.list_item_note.view.*

/* NoteAdapter 선언
  - PagedListAdapter: 페이징을 처리하기 위한 RecyclerView.Adapter
  - PagedListAdapter는 PagedList를 가지고 있고, 다음 데이터가 필요하면 PagedList에 요청하고,
    또한 전달된  new PagedList 기존 old PagedList를 비교(DiffUtil에 기준을 정의해 PagedListAdapter의
    Consturstor에 넘겨주면, 중복 PagedList는 UI에 보여주지 않음)
 */
class NoteAdapter(val noteDao: NoteDao, val deleteCallback: (note: NoteEntity) -> Unit) :
    PagedListAdapter<NoteEntity, NoteAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: NoteAdapter.ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(getItem(position))
    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_note, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    /* ItemViewHolder 클래스 선언
       - itemView를 인자로 받아 mapping
    */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* bindItems() 함수 선언
           - 데이터 바인딩
         */
        fun bindItems(note: NoteEntity?) {
            /* note가 있을 경우에만 바인딩 */
            note?.let {
                /* itemView(list_item_note.xml)의 txt_note_content 뷰에
                   note 테이블의 noteContent 컬럼값을 표시
                 */
                itemView.txt_note_content.text = it.noteContent

                /* "수정" 버튼 클릭시 업데이트 */
                //NoteUpdateDialog
                itemView.btn_update_note.setOnClickListener { _ ->
                    Log.d("TAG", "ItemViewHolder-수정버튼 클릭시 ")
                    //추가/수정 다이얼로그 생성
                    val dialog = NoteUpdateDialog().apply {
                        arguments = Bundle().apply { putInt("NOTE_KEY", it.noteIdx!!) }
                    }

                    //프래그먼트 다이얼로그(NoteUpdateDialog) 출력
                    dialog.show(
                        (itemView.context as AppCompatActivity).supportFragmentManager,
                        null
                    )
                }

                /* "삭제" 버튼 클릭시 삭제 */
                itemView.btn_delete_note.setOnClickListener { _ ->
                    Log.d("TAG", "ItemViewHolder-삭제버튼 클릭시 ")
                    /* 삭제할 Entity를 callback으로 전달
                       - 삭제를 위임
                    *  */
                    deleteCallback(it)
                }
            }
        }
    }

    /* final static으로 데이터를 비교하는  RecyclerView DiffUtil 클래스 구현
       - DiffUtil 클래스는 두 목록간의 차이점을 찾고 업데이트 되어야 할 목록을 반환
         (기존에 PagedList 가 존재하면 그 차이를 비교한 후 리사이클러뷰를 새로운 페이지로 갱신)
       - RecyclerView 어댑터에 대한 업데이트를 알리는데 사용
       - areItemsTheSame() : 두 객체가 같은 항목인지 여부를 결정
       - areContentsTheSame() : 두 항목의 데이터가 같은지 여부를 결정
     */
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldConcert: NoteEntity, newConcert: NoteEntity): Boolean =
                oldConcert.noteIdx == newConcert.noteIdx

            override fun areContentsTheSame(oldConcert: NoteEntity, newConcert: NoteEntity): Boolean =
                oldConcert.noteContent == newConcert.noteContent
        }
    }
}