package nl.omererdem.metafy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import nl.omererdem.metafy.spotify.SpotifyAuthenticator
import nl.omererdem.metafy.spotify.SpotifyService

var spotifyService: SpotifyService? = null

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

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
        fabToggler()
    }

    // Hide the FAB if the add fragment is the destination fragment
    private fun fabToggler() {
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener {
                _, destination, _ ->
            if (destination.id in arrayListOf(R.id.libraryFragment)) {
                fab.show()
                fab.setOnClickListener {

                }
            } else {
                fab.hide()
            }
        }
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