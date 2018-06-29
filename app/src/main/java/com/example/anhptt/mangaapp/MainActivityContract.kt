package com.example.anhptt.mangaapp

import com.example.anhptt.mangaapp.manga.Manga

interface MainActivityContract {

    interface View {
        fun showLoadingDialog()
        fun hideLoadingDialog()
        fun showMangaList(result: MutableList<Manga>?, page: Int?, isFinalPage: Boolean?)
    }

    interface Presenter {
        fun getMangaList(url: String, page: Int)
    }

}