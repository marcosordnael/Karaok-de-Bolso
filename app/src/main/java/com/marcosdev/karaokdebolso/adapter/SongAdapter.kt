package com.marcosdev.karaokdebolso.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcosdev.karaokdebolso.databinding.ItemSongBinding
import com.marcosdev.karaokdebolso.model.Song

class SongAdapter(
    private var songs: List<Song>,
    private val onItemClick: (Song) -> Unit,
    private val onLinkClick: (String) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var onItemLongClick: ((Song) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (Song) -> Unit) {
        onItemLongClick = listener
    }

    inner class SongViewHolder(val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.tvTitle.text = song.title

            // Se artista for vazio ou null, esconde o TextView
            if (song.artist.isNullOrBlank()) {
                binding.tvArtist.visibility = View.GONE
            } else {
                binding.tvArtist.visibility = View.VISIBLE
                binding.tvArtist.text = song.artist
            }

            // Se link for vazio ou null, esconde o TextView
            if (song.link.isNullOrBlank()) {
                binding.tvLink.visibility = View.GONE
            } else {
                binding.tvLink.visibility = View.VISIBLE
                binding.tvLink.text = song.link
                binding.tvLink.setOnClickListener {
                    onLinkClick(song.link!!)
                }
            }

            // Clique no card abre detalhes (mantém para compatibilidade)
            binding.root.setOnClickListener {
                onItemClick(song)
            }
            binding.root.setOnLongClickListener {
                onItemLongClick?.invoke(song)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    fun updateSongs(newSongs: List<Song>) {
        this.songs = newSongs
        notifyDataSetChanged()
    }
}