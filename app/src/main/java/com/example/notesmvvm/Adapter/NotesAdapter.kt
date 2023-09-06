package com.example.notesmvvm.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesmvvm.Model.Note
import com.example.notesmvvm.R
import kotlin.random.Random

class NotesAdapter(context: Context, val listner: NotesClickListner) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    val fullList = ArrayList<Note>()
    val NoteList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false);
        return NoteViewHolder(itemView)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NoteList[position]
        holder.title.text= currentNote.title
        holder.title.isSelected= true
        holder.note.text= currentNote.note
        holder.date.text= currentNote.date
        holder.date.isSelected= true

        holder.card.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.card.setOnClickListener {
            listner.onNoteClick(NoteList[holder.adapterPosition])

        }
        holder.card.setOnLongClickListener {
            listner.onLongClick(NoteList[holder.adapterPosition], holder.card)
            true
        }


    }

    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterlist(search: String){
        NoteList.clear()

        for (item in fullList){

            if (item.title?.lowercase()?.contains(search.lowercase())== true ||
                    item.note?.lowercase()?.contains(search.lowercase())== true){

                NoteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun randomColor(): Int{
        val list = ArrayList<Int>()

        list.add(R.color.light_teal)
        list.add(R.color.pink)
        list.add(R.color.blue)
        list.add(R.color.orange)
        list.add(R.color.yellow)
        list.add(R.color.orange)
        list.add(R.color.purple)

        val seed= System.currentTimeMillis().toInt()
        val random = Random(seed).nextInt(list.size)

        return list[random]
    }

    override fun getItemCount(): Int {

        return NoteList.size
     }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note = itemView.findViewById<TextView>(R.id.tv_notes)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
        val card = itemView.findViewById<CardView>(R.id.cardview)
    }
    interface NotesClickListner{
        fun onNoteClick(note: Note)
        fun onLongClick(note: Note, cardView: CardView)
    }

}