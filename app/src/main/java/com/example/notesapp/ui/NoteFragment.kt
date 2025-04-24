package com.example.notesapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.MainActivity
import com.example.notesapp.data.adapter.NoteAdapter
import com.example.notesapp.data.factory.GenericViewModelFactory
import com.example.notesapp.data.local.Note
import com.example.notesapp.databinding.FragmentNoteBinding
import com.example.notesapp.utils.OnItemClickListener
import com.example.notesapp.utils.setupHideKeyboardOnTouch
import com.google.android.material.snackbar.Snackbar

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NoteAdapter

    private lateinit var viewModel: NoteViewModel

    private var allNotes = listOf<Note>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val factory = GenericViewModelFactory(
            mapOf(NoteViewModel::class.java to { NoteViewModel(requireActivity().application) })
        )

        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHideKeyboardOnTouch(binding.root,binding.editTextSearch)

        (requireActivity() as MainActivity).setToolbarTitle("MY STORY")
        (requireActivity() as MainActivity).showBackButton(false)
        setHasOptionsMenu(true)

        adapter = NoteAdapter(noteItem)
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotes.adapter = adapter

        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            allNotes = notes
            adapter.submitList(notes)
            updateUI(notes)
        }

        initActionAddNote()

    }

    private fun updateUI(notes: List<Note>) {
        if (notes.isEmpty()) {
            binding.recyclerViewNotes.visibility = View.GONE
            binding.layoutNoData.visibility = View.VISIBLE
        } else {
            binding.recyclerViewNotes.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE
        }
    }

    private fun initActionAddNote() {
        binding.btnAddNote.setOnClickListener {
            // OR, for adding new note (pass null)
            val action = NoteFragmentDirections.actionNoteFragmentToAddNoteFragment(null)
            findNavController().navigate(action)
        }
    }

    private val noteItem = object : OnItemClickListener<Note> {
        override fun onItemClick(item: Note) {
            val action = NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment(item)
            findNavController().navigate(action)
        }

        override fun onItemLongClick(item: Note) {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.delete(item)
                    Snackbar.make(binding.root,"Note deleted",Snackbar.LENGTH_LONG)
                        .setAction("Undo"){
                            viewModel.insert(item)
                        }.show()
                }
                .setNeutralButton("Update"){_,_ ->
                    val action = NoteFragmentDirections.actionNoteFragmentToAddNoteFragment(item)
                    findNavController().navigate(action)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                filterNotes(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun filterNotes(query: String) {
        val filteredList = allNotes.filter {
            it.title.contains(query, ignoreCase = true)
        }
        adapter.submitList(filteredList)
        Log.i("ATG","ALL : $allNotes")
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}