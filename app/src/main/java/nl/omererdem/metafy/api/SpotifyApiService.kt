package nl.omererdem.metafy.api

import retrofit2.http.GET

interface SpotifyApiService {
    @GET("/authorize?response_type=code&client_id=c9630b46437740928f9bb3b2e42c0f3d&redirect_uri=nl.omererdem.metafy://callback&scope=playlist-read-private")
    suspend fun getAuthorized()
}