package com.rudhy.nayprak8

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.rudhy.nayprak8.databinding.DialogAddBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddBookDialog(
    private val ctx: Context,
    private val booksRef: DatabaseReference
) : Dialog(ctx) {

    private lateinit var bind: DialogAddBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DialogAddBookBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // Tutup dialog jika batal
        bind.btnCancelDialog.setOnClickListener { dismiss() }

        // Date Picker
        bind.editTextRelease.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                ctx,
                { _, year, month, day ->
                    val date = Calendar.getInstance()
                    date.set(year, month, day)
                    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    bind.editTextRelease.setText(format.format(date.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        bind.btnSave.setOnClickListener {
            val title = bind.editTextTitleBook.text.toString().trim()
            val author = bind.editTextAuthorBook.text.toString().trim()
            val release = bind.editTextRelease.text.toString().trim()

            if (title.isEmpty() || author.isEmpty() || release.isEmpty()) {
                Toast.makeText(ctx, "Tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = booksRef.push().key!!
            val newBook = Book(id, title, author, release, false)

            booksRef.child(id).setValue(newBook)
                .addOnSuccessListener {
                    Toast.makeText(ctx, "Data berhasil ditambah!", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(ctx, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
