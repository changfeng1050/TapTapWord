package com.example.changfeng.taptapword

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.changfeng.taptapword.adapter.RecyclerViewAdapter
import com.example.changfeng.taptapword.listener.WordItemArchivedListener
import com.example.changfeng.taptapword.listener.WordItemClickListener
import com.example.changfeng.taptapword.listener.WordItemUnArchivedListener
import com.example.changfeng.taptapword.util.Attributes
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

/**
 * Created by changfeng on 2015/4/17.
 */
open class WordsFragment : Fragment(){

    var words: List<Word>? = null

    val recyclerView: RecyclerView get() = find(R.id.word_recycler_view)
    val noRecentWordsCardView: CardView get() = find(R.id.no_recent_words_card_view)
    val noArchivedWordsCardView: CardView get() = find(R.id.no_archived_words_card_view)

    var adapter: RecyclerViewAdapter? = null

    var archived = false


    override fun onResume() {
        super.onResume()
        setRecyclerAdapter()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater!!.inflate(R.layout.fragment_word_recycler_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)

    }

    fun setRecyclerAdapter() {
        words = WordManger.get(activity).getWords(archived)
        adapter = RecyclerViewAdapter(activity, words, archived)
        adapter?.mode = Attributes.Mode.Single

        adapter?.setOnItemClickListener(object : WordItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val name = words!![position].name
                                    name?.let {
                                        context.startActivity(context.intentFor<WordActivity>(WordActivity.EXTRA_WORD_NAME  to name))
                                    }
            }
        })

        adapter?.setOnItemArchiveListener(object : WordItemArchivedListener{
            override fun onItemArchived(view: View, position: Int) {
                update()
                toast(R.string.message_archive_success)
            }
        })

        adapter?.setOnItemUnarchivedListener(object : WordItemUnArchivedListener{
            override fun onItemUnArchived(view: View, position: Int) {
                update()
                toast(R.string.message_archive_success)
            }
        })

        recyclerView.adapter = adapter
        update()
    }

    private fun update() {
        words = WordManger.get(activity).getWords(archived)
        if (words!!.isEmpty()) {
            if (archived) {
                    noArchivedWordsCardView.visibility = View.VISIBLE
            } else {
                    noRecentWordsCardView.visibility = View.VISIBLE
            }
        } else {
            if (archived) {
                    noArchivedWordsCardView.visibility = View.GONE
            } else {
                    noRecentWordsCardView.visibility = View.GONE
            }
        }
    }

    companion object {

        private val TAG = "WordsFragment"

        val ARG_ARCHIVED = "archived"
    }
}
