package nl.omererdem.metafy.utils

import android.util.Log
import kotlinx.coroutines.*
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongViewModel
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagViewModel

class Combination(var list: ArrayList<String>) {
    var filteredSongs: ArrayList<Song>? = null

    suspend fun getPlaylist(tagViewModel: TagViewModel, songViewModel: SongViewModel): List<Song>? =
        withContext(Dispatchers.IO) {
            val savedSongs = songViewModel.getAllSongsOnce()
            filteredSongs = null
            // If the combination has at least 1 tag or more while keeping in mind that after every
            // tag comes a symbol
            if (list.isNotEmpty() && list.size % 2 == 1) {
                var index = 0

                val firstSymbol: String? = getSymbolByName(list[index])
                val firstTag: Tag? = getTagByName(tagViewModel, list[index])
                if (firstSymbol == null && firstTag != null) {
                    filteredSongs = ArrayList(getLocalSongsByTag(savedSongs, firstTag))
                    index++
                }

                while (list.size >= (index + 2)) {
                    val symbol: String? = getSymbolByName(list[index])
                    val tag: Tag? = getTagByName(tagViewModel, list[(index + 1)])
                    if (symbol != null && tag != null) {
                        val tagSongs: ArrayList<Song> =
                            ArrayList(getLocalSongsByTag(savedSongs, tag))
                        when (symbol) {
                            "+" -> filteredSongs =
                                ArrayList(getPlusLocalSongs(filteredSongs!!, tagSongs))
                            "-" -> filteredSongs =
                                ArrayList(
                                    getMinusLocalSongs(filteredSongs!!, tagSongs)
                                )
                            "=" -> filteredSongs =
                                ArrayList(
                                    getEqualLocalSongs(filteredSongs!!, tagSongs)
                                )
                        }
                        index += 2
                    }
                }
            }
            return@withContext filteredSongs
        }

    fun getCombinationString(): String {
        var combination = ""
        for (item in list) {
            combination += item
        }
        return combination
    }

    private fun getTagByName(tagViewModel: TagViewModel, name: String): Tag? {
        return tagViewModel.getTagByName(name)
    }

    private fun getSymbolByName(string: String): String? {
        return if (string == "+" || string == "-" || string == "=") {
            string
        } else null
    }

    private fun getLocalSongsByTag(localSongs: List<Song>, tag: Tag): List<Song> {
        return localSongs.filter { song ->
            song.tags?.any { it.equals(tag) } == true
        }
    }

    private fun getPlusLocalSongs(
        firstTagSongs: ArrayList<Song>,
        secondTagSongs: ArrayList<Song>
    ): List<Song> {
        firstTagSongs.addAll(secondTagSongs)
        return firstTagSongs.distinct()
    }

    private fun getMinusLocalSongs(
        firstTagSongs: ArrayList<Song>,
        secondTagSongs: ArrayList<Song>
    ): List<Song> {
        firstTagSongs.removeAll(secondTagSongs)
        return firstTagSongs
    }

    private fun getEqualLocalSongs(
        firstTagSongs: ArrayList<Song>,
        secondTagSongs: ArrayList<Song>
    ): List<Song> {
        firstTagSongs.addAll(secondTagSongs)
        return firstTagSongs.groupBy { it.id }
            .filter { it.value.size == 1 }
            .flatMap { it.value }
    }
}