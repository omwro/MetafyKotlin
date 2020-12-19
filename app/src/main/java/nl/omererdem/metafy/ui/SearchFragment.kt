package nl.omererdem.metafy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.runBlocking
import nl.omererdem.metafy.R
import nl.omererdem.metafy.spotifyService
import nl.omererdem.metafy.utils.SpotifySongAdapter2

class SearchFragment : Fragment() {
    private var songsFound: ArrayList<Track> = arrayListOf()
    private val adapter = SpotifySongAdapter2(songsFound)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        rvSearch.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvSearch.adapter = adapter
        etSearch.addTextChangedListener {
            runBlocking {
                val result: List<Track>? = spotifyService?.getSearch(etSearch.text.toString())
                if (result != null) {
                    songsFound.clear()
                    songsFound.addAll(result)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}