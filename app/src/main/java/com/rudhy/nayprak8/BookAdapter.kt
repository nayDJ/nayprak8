package com.rudhy.nayprak8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.rudhy.nayprak8.databinding.ItemBookBinding

class BookAdapter(
    private val context: Context,
    private val books: MutableList<Book>,
    private val booksRef: DatabaseReference
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    inner class ViewHolder(private val bind: ItemBookBinding) :
        RecyclerView.ViewHolder(bind.root) {

        fun bindData(book: Book) {
            bind.tvTitle.text = book.title
            bind.tvAuthor.text = "Author : ${book.author}"
            bind.tvRelease.text = book.releaseDate

            bind.checkDone.setOnCheckedChangeListener(null)
            bind.checkDone.isChecked = book.isDone == true
            bind.checkDone.setOnCheckedChangeListener { _, isChecked ->
                book.id?.let {
                    booksRef.child(it).child("isDone").setValue(isChecked)
                }
            }

            bind.btnEdit.setOnClickListener {
                EditBookDialog(context, booksRef, book).show()
            }

            bind.btnDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Hapus Buku")
                    .setMessage("Yakin ingin menghapus?")
                    .setPositiveButton("Ya") { _, _ ->
                        book.id?.let {
                            booksRef.child(it).removeValue()
                        }
                        Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(books[position])
    }
}
