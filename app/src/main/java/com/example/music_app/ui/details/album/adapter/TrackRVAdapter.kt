package com.example.music_app.ui.details.album.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music_app.databinding.TrackItemBinding
import com.example.music_app.ui.search.data.Track

class TrackRVAdapter(
    private var trackList: List<Track>,
    private val onTrackClick: (Track) -> Unit,
) : RecyclerView.Adapter<TrackRVAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(private val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.trackTitle.text = track.track_title
            binding.trackArtist.text = track.artist_name
            binding.trackCoverImage.setImageResource(track.album_image)

            itemView.setOnClickListener {
                onTrackClick(track)
            }
        }
    }

    fun updateData(newData: List<Track>) {
        trackList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int = trackList.size
}
