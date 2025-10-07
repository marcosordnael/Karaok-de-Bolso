package com.marcosdev.karaokdebolso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.marcosdev.karaokdebolso.databinding.AddSongDialogBinding

class AddSongDialogFragment(private val onSongAdded: (title: String, artist: String, link: String) -> Unit) : DialogFragment() {
    private var _binding: AddSongDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = AddSongDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val artist = binding.etArtist.text.toString().trim()
            val link = binding.etLink.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Informe o nome da música", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            onSongAdded(title, artist, link)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        fun newInstance(onSongAdded: (title: String, artist: String, link: String) -> Unit): AddSongDialogFragment {
            return AddSongDialogFragment(onSongAdded)
        }
    }
}
