package com.example.notesmvvm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesmvvm.Adapter.NotesAdapter
import com.example.notesmvvm.Database.NoteDatabase
import com.example.notesmvvm.Model.Note
import com.example.notesmvvm.Model.NoteViewModel
import com.example.notesmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListner, PopupMenu.OnMenuItemClickListener  {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var adapter: NotesAdapter
    lateinit var noteModel: Note
    lateinit var noteViewModel: NoteViewModel
    lateinit var selected_note: Note

    private val updatenote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode== Activity.RESULT_OK){
            val note= result.data?.getSerializableExtra("note") as Note
            if (note != null){
                noteViewModel.updateNote(note)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize UI
        initUI()

        noteViewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        noteViewModel.allNotes.observe(this){ list ->

            list?.let{
                adapter.updateList(list)
            }
        }

        database = NoteDatabase.getDatabase(this)

    }

    private fun initUI() {

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager= StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter= NotesAdapter(this,this)
        binding.recyclerview.adapter = adapter

        val getContent= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

            if (result.resultCode== Activity.RESULT_OK){

                val note= result.data?.getSerializableExtra("note") as Note

                if (note != null){
                    noteViewModel.insertNote(note)
                }
            }

        }

        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    adapter.filterlist(newText)
                }
                return true
            }

        })





    }

    override fun onNoteClick(note: Note) {

        val intent = Intent(this, AddNote::class.java)
        intent.putExtra("current_note", note)

        updatenote.launch(intent)
    }

    override fun onLongClick(note: Note, cardView: CardView) {

        selected_note= note
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup= PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId== R.id.delete_note){
            noteViewModel.deleteNote(selected_note)
            return true
        }
        return false
    }
}