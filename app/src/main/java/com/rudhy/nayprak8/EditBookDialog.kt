package com.rudhy.nayprak8

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.rudhy.nayprak8.databinding.DialogAddBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditBookDialog(
    private val context: Context,
    private val booksRef: DatabaseReference,
    private val book: Book
) {

    fun show() {
        val dialogBinding = DialogAddBookBinding.inflate(LayoutInflater.from(context))

        // Isi data awal ke edit text
        dialogBinding.editTextTitleBook.setText(book.title)
        dialogBinding.editTextAuthorBook.setText(book.author)
        dialogBinding.editTextRelease.setText(book.releaseDate)

        // Date Picker
        dialogBinding.editTextRelease.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, day ->
                    val date = Calendar.getInstance()
                    date.set(year, month, day)
                    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    dialogBinding.editTextRelease.setText(format.format(date.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Dialog
        MaterialAlertDialogBuilder(context)
            .setTitle("Edit Buku")
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val newTitle = dialogBinding.editTextTitleBook.text.toString().trim()
                val newAuthor = dialogBinding.editTextAuthorBook.text.toString().trim()
                val newRelease = dialogBinding.editTextRelease.text.toString().trim()

                if (newTitle.isEmpty() || newAuthor.isEmpty() || newRelease.isEmpty()) {
                    Toast.makeText(context, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val updatedBook = Book(book.id, newTitle, newAuthor, newRelease, book.isDone)

                booksRef.child(book.id!!).setValue(updatedBook)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
