package com.example.music_app.ui.details.track

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object MusicController {
    private var exoPlayer: ExoPlayer? = null

    private fun initializePlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context).build()
    }

    fun setMediaItem(context: Context, previewUrl: String) {
        if (exoPlayer == null) {
            initializePlayer(context)
        }

        val mediaItem = MediaItem.fromUri(previewUrl)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
        }
    }

    fun playPreview() {
        exoPlayer?.apply {
            if (isPlaying) {
                seekTo(currentPosition)
            } else {
                play()
            }
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun stop() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0L
    }

    fun getDuration(): Long {
        return exoPlayer?.duration ?: 0L
    }

    fun setVolume(volume: Float) {
        exoPlayer?.volume = volume
    }

}