package com.example.notesappviewmodel

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.notesappviewmodel.databinding.ActivityMainBinding
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesappviewmodel.adapters.NotesAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerAdapter: NotesAdapter

    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        viewModel.fetchNotes().observe(this, {
            notes -> recyclerAdapter.updateNotes(notes)
        })

        binding.floatingActionButton.setOnClickListener { showAddNoteDialog() }

        updateRV()
    }

    private fun updateRV() {
        recyclerAdapter = NotesAdapter(this)
        binding.notesRV.adapter = recyclerAdapter
        binding.notesRV.layoutManager = GridLayoutManager(this, 2)
    }


    private fun showAddNoteDialog() {
        val dialog = Dialog(this, R.style.Theme_AppCompat_DayNight)
        dialog.setContentView(R.layout.add_note_dialog)
        dialog.setCanceledOnTouchOutside(true)

        val addBtnD = dialog.findViewById<Button>(R.id.addBtnD)
        val addNoteET = dialog.findViewById<EditText>(R.id.addNoteET)

        addBtnD.setOnClickListener {
            if (addNoteET.text.isNotEmpty()) {
                viewModel.addNote(addNoteET.text.toString())
                addNoteET.text.clear()
                dialog.dismiss()

            } else {
                Toast.makeText(
                    applicationContext,
                    "Type something to add a note",
                    Toast.LENGTH_LONG
                ).show()
                // this Toast is not showing
            }
        }
        dialog.show()
    }


    fun showEditDeleteDialog(id: Int, noteText: String) {
        val dialog = Dialog(
            this,
            R.style.Theme_AppCompat_DayNight)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_delete_dialog)

        val deleteBtn = dialog.findViewById<Button>(R.id.deleteBtn)
        val editBtn = dialog.findViewById<Button>(R.id.editBtn)
        val editDeleteET = dialog.findViewById<EditText>(R.id.editDeleteET)
        editDeleteET.setText(noteText)

        editBtn.setOnClickListener {
            viewModel.editNote(id, editDeleteET.text.toString())
            dialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            displayDeleteConformationDialog(id)
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun displayDeleteConformationDialog(id: Int) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                viewModel.deleteNote(id)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Delete Confirmation")
        alert.show()
    }
}