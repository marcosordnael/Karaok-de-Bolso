package com.marcosdev.karaokdebolso

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.marcosdev.karaokdebolso.adapter.SongAdapter
import com.marcosdev.karaokdebolso.databinding.FragmentSongListBinding
import com.marcosdev.karaokdebolso.model.Song
import androidx.fragment.app.viewModels
import com.marcosdev.karaokdebolso.viewmodel.SongViewModel

class SongListFragment : Fragment() {
    private var _binding: FragmentSongListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SongViewModel by viewModels {
        SongViewModel.Factory(requireActivity().application)
    }
    private lateinit var adapter: SongAdapter
    private var allSongs: List<Song> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerSongs.layoutManager = LinearLayoutManager(requireContext())
        adapter = SongAdapter(
            emptyList(),
            onItemClick = { song ->
                if (!song.link.isNullOrBlank()) {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(song.link)
                    startActivity(intent)
                } else {
                    /* TODO: ação ao clicar no item sem link */
                }
            },
            onLinkClick = { link ->
                if (!link.isNullOrBlank()) {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(link)
                    startActivity(intent)
                }
            }
        )
        binding.recyclerSongs.adapter = adapter

        adapter.setOnItemLongClickListener { song ->
            val options = arrayOf("Editar", "Excluir")
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Opções")
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> {
                            val bundle = Bundle()
                            bundle.putParcelable("song", song)
                            findNavController().navigate(R.id.action_songListFragment_to_addSongFragment, bundle)
                        }
                        1 -> viewModel.delete(song)
                    }
                }
                .show()
        }

        viewModel.allSongs.observe(viewLifecycleOwner) { songs ->
            allSongs = songs
            adapter.updateSongs(songs)
            updateEmptyState(songs.isEmpty())
        }


        updateEmptyState(isEmpty = true)
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerSongs.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun filterSongs(query: String?) {
        val filtered = if (query.isNullOrBlank()) {
            allSongs
        } else {
            allSongs.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.artist.contains(query, ignoreCase = true) ||
                it.link.contains(query, ignoreCase = true)
            }
        }
        adapter.updateSongs(filtered)
        updateEmptyState(filtered.isEmpty())
    }

    fun filterSongsFromActivity(query: String?) {
        filterSongs(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}