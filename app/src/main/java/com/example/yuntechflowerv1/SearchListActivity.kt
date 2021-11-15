package com.example.yuntechflowerv1

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yuntechflowerv1.adapter.MyAdapter
import com.example.yuntechflowerv1.flowers.FlowerData
import com.example.yuntechflowerv1.flowers.FlowerItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.flower_search_item.*
import kotlinx.android.synthetic.main.search_list.*
import java.util.*
import kotlin.collections.ArrayList

class SearchListActivity : AppCompatActivity() {
    var items: MutableList<FlowerItem> = ArrayList()
    var searchList: MutableList<FlowerItem> = ArrayList()
    private var sectionItemData: MutableList<FlowerItem> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        title = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_list)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            textView,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.isIconifiedByDefault = false
        recyclerView.layoutManager = WrapContentLinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        //recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val photosList = FlowerData.allFlower
        items.addAll(photosList)
        searchList.addAll(photosList)

        resetSection()
        recyclerView.adapter = MyAdapter(this, sectionItemData)

        buildToolbar()

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val imm =
                    this@SearchListActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchView.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    searchList.clear()
                    val searchText = newText.lowercase(Locale.getDefault())
                    items.forEach {
                        if (it.nameCh.contains(searchText)
                            || it.nameEn.lowercase(Locale.getDefault()).contains(searchText)
                        ) {
                            searchList.add(it)
                        }
                    }
                    resetSection()
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(items)
                    resetSection()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })
    }

    fun resetSection() {
        val sections: MutableList<FlowerItem> = mutableListOf()

        for (value in searchList) {
            sections.add(value)
        }
        sectionItemData.clear()
        sectionItemData.addAll(sections)
    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
