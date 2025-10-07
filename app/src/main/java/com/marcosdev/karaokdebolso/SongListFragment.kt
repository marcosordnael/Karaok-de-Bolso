package com.marcosdev.karaokdebolso

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcosdev.karaokdebolso.adapter.SongAdapter
import com.marcosdev.karaokdebolso.databinding.FragmentSongListBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marcosdev.karaokdebolso.model.Song
import com.marcosdev.karaokdebolso.AddSongDialogFragment

class SongListFragment : Fragment() {
    private var _binding: FragmentSongListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SongAdapter
    private val songs = mutableListOf<Song>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ajusta padding para status bar e navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.songListRoot) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        binding.recyclerSongs.layoutManager = LinearLayoutManager(requireContext())
        adapter = SongAdapter(
            songs,
            onItemClick = { /* TODO: ação ao clicar no item */ },
            onLinkClick = { /* TODO: ação ao clicar no link */ }
        )
        binding.recyclerSongs.adapter = adapter

        binding.fabAddSong.setOnClickListener {
            AddSongDialogFragment.newInstance { title, artist, link ->
                val song = Song(title = title, artist = artist, link = link)
                songs.add(song)
                adapter.notifyItemInserted(songs.size - 1)
                binding.emptyState.visibility = View.GONE
            }.show(parentFragmentManager, "AddSongDialog")
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // filtrar via ViewModel / Adapter
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Atualizar filtragem em tempo real
                return true
            }
        })

        updateEmptyState(isEmpty = true) // TODO: mudar conforme dados
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerSongs.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}