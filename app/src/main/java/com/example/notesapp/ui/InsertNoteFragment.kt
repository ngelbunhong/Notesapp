package com.example.notesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesapp.R
import com.example.notesapp.data.local.Note
import com.example.notesapp.databinding.FragmentAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InsertNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private val args: InsertNoteFragmentArgs by navArgs()
    private var currentNote: Note? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        initArgsUpdateNote()
        initCancelNote()
        initSaveNote()

    }

    private fun initArgsUpdateNote() {
        currentNote = args.note
        currentNote?.let {
            binding.editTextTitle.setText(it.title)
            binding.editTextContent.setText(it.content)
            binding.buttonSaveNote.text = getString(R.string.update_text)
        }
    }

    private fun initSaveNote() {
        binding.buttonSaveNote.setOnClickListener {
            val formatter = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
            val currentDate = formatter.format(Date())
            val title = binding.editTextTitle.text.toString().trim()
            val content = binding.editTextContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(
                    id = currentNote?.id ?: 0,
                    title = title,
                    content = content,
                    date = currentDate
                )
                if (currentNote != null) {
                    noteViewModel.update(note)
                    Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()

                } else {
                    noteViewModel.insert(note)
                    Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Please fill out both fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun initCancelNote() {
        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}