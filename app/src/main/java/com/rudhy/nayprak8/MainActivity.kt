package com.rudhy.nayprak8

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.rudhy.nayprak8.databinding.ActivityMainBinding
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var booksRef: DatabaseReference
    private lateinit var adapter: BookAdapter
    private val booksList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        booksRef = FirebaseDatabase.getInstance().getReference("books")

        setupRecycler()
        loadData()
        setupAddButton()
    }

    private fun setupRecycler() {
        adapter = BookAdapter(this, booksList, booksRef)
        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = adapter
    }

    private fun loadData() {
        booksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                booksList.clear()
                for (data in snapshot.children) {
                    val book = data.getValue(Book::class.java)
                    book?.let { booksList.add(it) }
                }
                adapter.notifyDataSetChanged()

                // ⭐ Show/Hide Empty View
                if (booksList.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvBooks.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvBooks.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupAddButton() {
        binding.fabAddBooks.setOnClickListener {
            AddBookDialog(this, booksRef).show()
        }
    }
}
