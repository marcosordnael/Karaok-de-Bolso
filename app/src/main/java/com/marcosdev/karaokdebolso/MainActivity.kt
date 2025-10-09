package com.marcosdev.karaokdebolso

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.marcosdev.karaokdebolso.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        // Clique do FloatingActionButton agora está na MainActivity
        binding.fabAddSong.setOnClickListener {
            // Garante que está no SongListFragment antes de navegar
            val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()
            if (currentFragment is com.marcosdev.karaokdebolso.SongListFragment) {
                navController.navigate(R.id.action_songListFragment_to_addSongFragment)
            }
        }

        // Deixa a navigation bar preta
        window.navigationBarColor = resources.getColor(R.color.black, theme)
        // Deixa a status bar fixa com a cor do header
        window.statusBarColor = resources.getColor(R.color.roxo_escuro, theme)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = "Buscar música ou artista"
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sendQueryToFragment(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                sendQueryToFragment(newText)
                return true
            }
        })
        return true
    }

    private fun sendQueryToFragment(query: String?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val fragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
        if (fragment is com.marcosdev.karaokdebolso.SongListFragment) {
            fragment.filterSongsFromActivity(query)
        }
    }
}
