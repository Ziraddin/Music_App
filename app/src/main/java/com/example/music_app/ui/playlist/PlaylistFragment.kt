package com.example.music_app.ui.playlist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.music_app.databinding.DialogConfirmDeleteBinding
import com.example.music_app.databinding.DialogCreatePlaylistBinding
import com.example.music_app.databinding.DialogUpdatePlaylistBinding
import com.example.music_app.databinding.FragmentPlaylistBinding
import com.example.music_app.ui.main.MainActivity
import com.example.music_app.ui.playlist.adapter.PlaylistRVAdapter
import com.example.music_app.viewmodel.PlaylistState
import com.example.music_app.viewmodel.PlaylistViewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: PlaylistRVAdapter
    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        playlistViewModel = (activity as MainActivity).playlistViewModel
        rvAdapter = PlaylistRVAdapter(
            emptyList(),
            onPlaylistClick = ::onPlaylistClick,
            onOptionSelected = ::onOptionSelected,
        )
        playlistViewModel.getPlaylists()
        setGridRv()
        createPlaylist()
        observePlaylistState()

        return binding.root
    }


    private fun observePlaylistState() {
        playlistViewModel.playlistState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is PlaylistState.Success -> {
                    rvAdapter.updateData(state.result)
                }

                is PlaylistState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        })
    }

    private fun createPlaylist() {
        binding.createPlaylistButton.setOnClickListener {
            showCreatePlaylistDialog()
        }
    }

    //Dialogs
    private fun showCreatePlaylistDialog() {
        val binding = DialogCreatePlaylistBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val editText = binding.editPlaylistName
        val btnCancel = binding.btnCancel
        val btnCreate = binding.btnCreate

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnCreate.setOnClickListener {
            val playlistName = editText.text.toString().trim()

            if (playlistName.isNotEmpty()) {
                val newPlaylist = Playlist(name = playlistName)
                playlistViewModel.addPlaylist(newPlaylist)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Playlist name is required", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialog.show()
    }

    private fun confirmDelete(playlist: Playlist) {
        val dialog = Dialog(requireContext())
        val binding = DialogConfirmDeleteBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val confirmButton = binding.btnDelete
        val cancelButton = binding.btnCancel

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        confirmButton.setOnClickListener {
            playlistViewModel.removePlaylist(playlist)
            Toast.makeText(requireContext(), "Playlist deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showUpdateDialog(playlist: Playlist) {
        val dialog = Dialog(requireContext())
        val binding = DialogUpdatePlaylistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val playlistNameEditText = binding.editPlaylistName
        val updateButton = binding.btnUpdate
        val cancelButton = binding.btnCancel

        playlistNameEditText.setText(playlist.name)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        updateButton.setOnClickListener {
            val newName = playlistNameEditText.text.toString().trim()
            if (newName.isEmpty()) {
                playlistNameEditText.error = "Name is required"
            } else {
                playlist.name = newName
                playlistViewModel.updatePlaylist(playlist)
                Toast.makeText(requireContext(), "Playlist updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    //Recycler View Methods
    private fun setGridRv() {
        binding.recyclerViewPlaylists.adapter = rvAdapter
    }

    private fun onPlaylistClick(playlist: Playlist) {
        val navController = findNavController()
        val action =
            PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailsFragment(playlist)
        navController.navigate(action)
    }

    private fun onOptionSelected(playlist: Playlist, action: String) {
        when (action) {
            "Edit" -> {
                showUpdateDialog(playlist)
            }

            "Delete" -> {
                confirmDelete(playlist)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
