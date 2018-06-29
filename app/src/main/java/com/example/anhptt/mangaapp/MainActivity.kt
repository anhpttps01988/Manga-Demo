package com.example.anhptt.mangaapp

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.Toast
import com.example.anhptt.mangaapp.adapter.MangaAdapter
import com.example.anhptt.mangaapp.manga.Manga
import com.example.anhptt.mangaapp.service.ServiceUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v7.widget.LinearLayoutManager
import android.util.Log


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainActivityContract.View {

    private lateinit var mPresenter: MainActivityContract.Presenter
    private var mVisibleItemCount = 0
    private var mFirstVisibleItem = intArrayOf()
    private var mTotalItemCount = 0
    private var isLoading = false
    private var page = 1
    private var adapter: MangaAdapter? = null
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var mListManga: MutableList<Manga> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = MainActivityPresenter(this)
        mPresenter.getMangaList(ServiceUtil.URL_MAIN, page)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        setupAdapterManga()
        loadMore()
    }

    private fun setupAdapterManga() {
        adapter = MangaAdapter(this)
        adapter!!.setActionCallback(object : MangaAdapter.ActionCallback {
            override fun OnClick(position: Int, manga: Manga) {
                Toast.makeText(this@MainActivity, manga.urlDetail, Toast.LENGTH_SHORT).show()
            }

            override fun OnLoadMore() {
                mPresenter.getMangaList(ServiceUtil.URL_MAIN, page)
            }
        })
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        listManga.setHasFixedSize(true)
        listManga.layoutManager = layoutManager
        listManga.isNestedScrollingEnabled = true
        listManga.adapter = adapter
    }

    private fun loadMore() {
        listManga.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mTotalItemCount = layoutManager!!.itemCount
                mVisibleItemCount = layoutManager!!.childCount
                mFirstVisibleItem = layoutManager!!.findFirstVisibleItemPositions(null)
                if (!isLoading && mTotalItemCount <= (mFirstVisibleItem[0] + mVisibleItemCount)) {
                    mListManga.add(mListManga.size, Manga())
                    adapter!!.setFooter(Manga())
                    page += 1
                    Log.d("LoadMore", "Paging = $page")
                    isLoading = true
                }
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun showLoadingDialog() {
    }

    override fun hideLoadingDialog() {
    }

    override fun showMangaList(result: MutableList<Manga>?, page: Int?, isFinalPage: Boolean?) {
        this.mListManga = result!!
        this.isLoading = isFinalPage!!
        this.page = page!!
        if (result.size > 0) {
            this.adapter!!.setItems(this.mListManga)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
