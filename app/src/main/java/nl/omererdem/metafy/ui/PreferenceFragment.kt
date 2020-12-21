package nl.omererdem.metafy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.fragment_preference.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagViewModel
import nl.omererdem.metafy.utils.TagAdapter

class PreferenceFragment : Fragment() {
    val tags: ArrayList<Tag> = arrayListOf()
    val tagAdapter = TagAdapter(tags, true)
    val tagViewModel: TagViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getTags()
    }

    private fun initView() {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.alignItems = AlignItems.CENTER
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        rvTagsPreference.layoutManager = flexboxLayoutManager
        rvTagsPreference.adapter = tagAdapter
        btnAddTag.setOnClickListener {
            saveTag()
        }
        deleteTag().attachToRecyclerView(rvTagsPreference)
    }

    private fun getTags() {
        tagViewModel.tags.observe(viewLifecycleOwner, {
            savedTags ->
            tags.clear()
            tags.addAll(savedTags)
            tags.sortBy { it?.name }
            tagAdapter.notifyDataSetChanged()
            Log.e("SAVED TAGS", savedTags.toString())
        })
    }

    private fun saveTag() {
        if (etAddInput.text?.isNotBlank() == true) {
            tagViewModel.insertTag(Tag(etAddInput.text.toString()))
            etAddInput.setText("")
            getTags()
        }
    }

    private fun deleteTag(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                tags[position]?.let { tagViewModel.deleteTag(it) }
            }
        }
        return ItemTouchHelper(callback)
    }
}