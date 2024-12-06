package com.example.music_app.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music_app.databinding.SearchGridItemBinding
import com.example.music_app.ui.search.data.Album
import com.example.music_app.ui.search.data.Track

class GridRVAdapter(
    private var itemList: List<Any>, private val onItemClick: (Any) -> Unit
) : RecyclerView.Adapter<GridRVAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(private val binding: SearchGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any) {
            when (item) {
                is Track -> {
                    binding.imageAlbumCover.setImageResource(item.album_image)
                    binding.textAlbumOrTrackTitle.text = item.track_title
                    binding.textArtistName.text = item.artist_name
                    itemView.setOnClickListener { onItemClick(item) }
                }

                is Album -> {
                    binding.imageAlbumCover.setImageResource(item.album_image)
                    binding.textAlbumOrTrackTitle.text = item.album_title
                    binding.textArtistName.text = item.artist_name
                    itemView.setOnClickListener { onItemClick(item) }
                }
            }
        }
    }

    fun updateData(newData: List<Any>) {
        itemList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = SearchGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}
