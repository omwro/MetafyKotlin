package nl.omererdem.metafy.spotify

import android.content.Intent
import android.net.Uri
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.getPkceAuthorizationUrl
import com.adamratzman.spotify.getSpotifyPkceCodeChallenge
import nl.omererdem.metafy.MainActivity

class SpotifyAuthenticator {
    val CLIENT_ID: String = "c9630b46437740928f9bb3b2e42c0f3d"
    val REDIRECTED_URI: String = "metafy://authenticationcallback"
    val CODE_VERIFIER = "thisisaveryrandomalphanumericcodeverifierandisgreaterthan43characters"
    private val codeChallenge = getSpotifyPkceCodeChallenge(CODE_VERIFIER)

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
        return uri
    }

    fun authenticate(mainActivity: MainActivity) {
        mainActivity.startActivity(Intent(Intent.ACTION_VIEW, this.getAuthUri()))
    }

    fun getToken(intent: Intent?): String? {
        return Uri.parse(intent?.dataString).getQueryParameter("code")
    }

}