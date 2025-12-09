package com.rudhy.nayprak8


data class Book(
    var id: String? = null,                  // ➜ tambahkan ID
    var title: String? = null,
    var author: String? = null,              // ➜ author juga diperlukan di adapter kamu
    var releaseDate: String? = null,
    var isDone: Boolean? = false             // ➜ untuk checkbox
)



