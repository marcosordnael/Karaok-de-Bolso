package com.marcosdev.karaokdebolso

import android.os.Bundle
import android.os.Build
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marcosdev.karaokdebolso.databinding.ActivityMainBinding
import com.marcosdev.karaokdebolso.databinding.FragmentSongListBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajusta padding para status bar e navigation bar
        window.statusBarColor = ContextCompat.getColor(this, R.color.roxo_escuro)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SongListFragment())
                .commit()
        }
    }
}