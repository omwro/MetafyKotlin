package nl.omererdem.metafy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.adamratzman.spotify.*

class MainActivity : AppCompatActivity() {

    private val CLIENT_ID: String = "c9630b46437740928f9bb3b2e42c0f3d"
    private val REDIRECTED_URI: String = "metafy://authenticationcallback"
    private val CODE_VERIFIER = "thisisaveryrandomalphanumericcodeverifierandisgreaterthan43characters"
    private val codeChallenge = getSpotifyPkceCodeChallenge(CODE_VERIFIER)

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
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        navController = findNavController(R.id.nav_host_fragment)
    }

    private fun getAuthUri(): Uri {
        val uri: Uri = Uri.parse(
            getPkceAuthorizationUrl(
                SpotifyScope.PLAYLIST_READ_PRIVATE,
                SpotifyScope.PLAYLIST_MODIFY_PRIVATE,
                SpotifyScope.PLAYLIST_MODIFY_PUBLIC,
                SpotifyScope.PLAYLIST_READ_COLLABORATIVE,
                SpotifyScope.USER_LIBRARY_READ,
                SpotifyScope.USER_LIBRARY_MODIFY,
                clientId = CLIENT_ID,
                redirectUri = REDIRECTED_URI,
                codeChallenge = codeChallenge
            )
        )
        Log.i("OMER AUTH URI", uri.toString())
        return uri
    }

    fun openSpotifyAuthenticator() {
        startActivity(Intent(Intent.ACTION_VIEW, getAuthUri()))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val token =  Uri.parse(intent?.dataString).getQueryParameter("code")
        if (token != null) {
            getTracks(token)
            navController.navigate(R.id.action_loginFragment_to_libraryFragment)
        }
    }

    private fun getTracks(token: String) {
        val api = spotifyClientPkceApi(
            CLIENT_ID,
            REDIRECTED_URI,
            token,
            CODE_VERIFIER
        ).build()
        Log.e("API", api.library.getSavedTracks().getAllItems().complete().toString())
    }
}