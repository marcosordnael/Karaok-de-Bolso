package com.marcosdev.karaokdebolso

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.marcosdev.karaokdebolso.adapter.SongAdapter
import com.marcosdev.karaokdebolso.databinding.FragmentSongListBinding
import com.marcosdev.karaokdebolso.model.Song
import androidx.fragment.app.viewModels
import com.marcosdev.karaokdebolso.viewmodel.SongViewModel
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.MenuProvider
import androidx.appcompat.widget.SearchView

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

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBarsInsets.bottom
            )
            insets
        }


        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as? SearchView
                searchView?.queryHint = getString(R.string.search_hint)
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        filterSongsFromActivity(query)
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterSongsFromActivity(newText)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)

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