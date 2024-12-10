package com.example.music_app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.music_app.R
import com.example.music_app.core.database.AppDatabase
import com.example.music_app.data.repository.MusicRepository
import com.example.music_app.databinding.ActivityMainBinding
import com.example.music_app.viewmodel.PlaylistViewModel
import com.example.music_app.viewmodel.QuizViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var musicRepository: MusicRepository

    lateinit var quizViewModel: QuizViewModel
    lateinit var playlistViewModel: PlaylistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        //Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(view)

        //Initialize app database and repository
        val database = AppDatabase.getDatabase(applicationContext)
        musicRepository = MusicRepository(database)

        //Initialize Viewmodels
        quizViewModel = QuizViewModel(musicRepository)
        playlistViewModel = PlaylistViewModel(musicRepository)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

}