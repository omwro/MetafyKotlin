package nl.omererdem.metafy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import nl.omererdem.metafy.model.TagViewModel
import nl.omererdem.metafy.spotify.SpotifyAuthenticator
import nl.omererdem.metafy.spotify.SpotifyService

var spotifyService: SpotifyService? = null
lateinit var navController: NavController

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        onInit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onInit() {
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigationHandler()
        viewElementToggler()
    }

    private fun viewElementToggler() {
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            // Visibility for the FAB
            if (destination.id in arrayListOf(R.id.libraryFragment)) {
                fab.show()
                fab.setOnClickListener {
                    navController.navigate(R.id.playlistFragment)
                }
            } else {
                fab.hide()
            }

            // Visibility for the bottom navigation bar
            if (destination.id in arrayListOf(R.id.loginFragment)) {
                bottomNavigation.visibility = View.GONE
            } else {
                bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    private fun bottomNavigationHandler() {
        val navigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_library -> {
                        navController.navigate(R.id.libraryFragment)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_search -> {
                        navController.navigate(R.id.searchFragment)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_preference -> {
                        navController.navigate(R.id.preferenceFragment)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    fun openSpotifyAuthenticator() {
        SpotifyAuthenticator().authenticate(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val token = SpotifyAuthenticator().getToken(intent)
        if (token != null) {
            spotifyService = SpotifyService(token)
            navController.navigate(R.id.action_loginFragment_to_libraryFragment)
        }
    }
}