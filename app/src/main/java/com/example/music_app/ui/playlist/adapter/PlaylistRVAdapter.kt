package com.example.music_app.ui.playlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music_app.R
import com.example.music_app.databinding.PlaylistItemBinding
import com.example.music_app.databinding.PlaylistOptionsBottomSheetBinding
import com.example.music_app.ui.playlist.Playlist
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Locale


class PlaylistRVAdapter(
    private var playlists: List<Playlist>,
    private val onPlaylistClick: ((Playlist) -> Unit)? = null,
    private val onPlaylistSelected: ((Playlist) -> Unit)? = null,
    private val onOptionSelected: ((Playlist, String) -> Unit)? = null,
) : RecyclerView.Adapter<PlaylistRVAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(private val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.playlistName.text = playlist.name
            binding.playlistTrackCount.text =
                String.format(Locale.ROOT, "Tracks: %d", playlist.trackCount)

            var selected: Boolean = false
            if (onPlaylistSelected != null) {
                binding.root.setOnClickListener {
                    onPlaylistSelected!!(playlist)
                    if (!selected) {
                        binding.playlistName.setTextColor(binding.root.context.getColor(R.color.spotify_green))
                    } else {
                        binding.playlistName.setTextColor(binding.root.context.getColor(R.color.black))
                    }
                    selected = !selected
                }
            } else if (onPlaylistClick != null) {
                binding.root.setOnClickListener {
                    onPlaylistClick!!(playlist)
                }
            }
            binding.playlistOptions.setOnClickListener {
                showBottomSheet(binding.root.context, playlist)
            }
        }
    }

    private fun showBottomSheet(context: Context, playlist: Playlist) {
        val bottomSheet = BottomSheetDialog(context)
        val binding = PlaylistOptionsBottomSheetBinding.inflate(LayoutInflater.from(context))
        bottomSheet.setContentView(binding.root)

        binding.optionEdit.setOnClickListener {
            bottomSheet.dismiss()
            onOptionSelected?.let { it1 -> it1(playlist, "Edit") }
        }

        binding.optionDelete.setOnClickListener {
            bottomSheet.dismiss()
            onOptionSelected?.let { it1 -> it1(playlist, "Delete") }
        }

        bottomSheet.show()
    }

    fun updateData(newData: List<Playlist>) {
        playlists = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

}
