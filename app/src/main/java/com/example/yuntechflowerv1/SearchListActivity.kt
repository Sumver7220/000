package com.example.yuntechflowerv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.search_list.*
import kotlinx.android.synthetic.main.search_list.toolbar

class SearchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        title=""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_list)
        val Adapter = MyAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = Adapter
        buildToolbar()
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