package com.marcosdev.karaokdebolso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marcosdev.karaokdebolso.databinding.FragmentAddSongBinding
import com.marcosdev.karaokdebolso.model.Song
import com.marcosdev.karaokdebolso.viewmodel.SongViewModel

class AddSongFragment : Fragment() {
    private var _binding: FragmentAddSongBinding? = null
    private val binding get() = _binding!!
    private var songToEdit: Song? = null

    private val viewModel: SongViewModel by viewModels {
        SongViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera Song do arguments, se houver (edição)
        songToEdit = arguments?.getParcelable<Song>("song")
        songToEdit?.let { song ->
            binding.etTitle.setText(song.title)
            binding.etArtist.setText(song.artist)
            binding.etLink.setText(song.link)
            binding.btnSave.text = "Atualizar"
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val artist = binding.etArtist.text.toString().trim()
            val link = binding.etLink.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Informe o nome da música", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (songToEdit != null) {
                // Atualiza música existente
                val updatedSong = songToEdit!!.copy(title = title, artist = artist, link = link)
                viewModel.update(updatedSong)
            } else {
                // Cria nova música
                val newSong = Song(title = title, artist = artist, link = link)
                viewModel.insert(newSong)
            }

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
