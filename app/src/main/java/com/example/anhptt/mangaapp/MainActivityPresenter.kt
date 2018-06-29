package com.example.anhptt.mangaapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.example.anhptt.mangaapp.manga.Manga
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*

class MainActivityPresenter constructor(private var mView: MainActivityContract.View) : MainActivityContract.Presenter {

    private var isFinalPage = false
    private var page = 1


    override fun getMangaList(url: String, page: Int) {
        this.page = page
        val loadDataFromUrl = LoadMangaFromURL()
        loadDataFromUrl.execute(String.format(url, page))
    }


    @SuppressLint("StaticFieldLeak")
    inner class LoadMangaFromURL : AsyncTask<String, Void, MutableList<Manga>>() {

        val listManga: MutableList<Manga> = mutableListOf()

        override fun onPreExecute() {
            super.onPreExecute()
            mView.showLoadingDialog()
        }

        override fun doInBackground(vararg params: String?): MutableList<Manga> {
            val url = params[0]
            val document = Jsoup.connect(url).get()
            val homeList: Elements = document.select(".ulListruyen li")
            for (i in 0 until homeList.size) {
                val element = homeList[i]
                val manga = Manga()
                manga.title = element.getElementsByTag("a").attr("title")
                manga.pictureUrl = element.getElementsByTag("img").attr("src")
                manga.urlDetail = element.getElementsByTag("a").attr("href")
                listManga.add(manga)
            }
            return listManga
        }

        override fun onPostExecute(result: MutableList<Manga>?) {
            super.onPostExecute(result)
            isFinalPage = result?.size == 0
            mView.showMangaList(result, page, isFinalPage)
            mView.hideLoadingDialog()
        }
    }
}