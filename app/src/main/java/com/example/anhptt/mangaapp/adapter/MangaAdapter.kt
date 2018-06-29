package com.example.anhptt.mangaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.anhptt.mangaapp.R
import com.example.anhptt.mangaapp.manga.Manga

class MangaAdapter constructor(private var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_LOADING = 0
        const val VIEW_ITEM = 1
    }


    private var mMangaList: MutableList<Manga> = mutableListOf()
    private lateinit var actionCallback: ActionCallback

    fun setItems(items: MutableList<Manga>?) {
        if (mMangaList.size > 0) {
            if (mMangaList[mMangaList.size - 1].title == null) {
                mMangaList.removeAt(mMangaList.size - 1)
                notifyItemRemoved(mMangaList.size)
            }
        }
        mMangaList.addAll(items!!)
        notifyItemInserted(mMangaList.size)
    }


    fun setFooter(items: Manga?) {
        mMangaList.add(mMangaList.size, items!!)
        notifyItemInserted(mMangaList.size)
    }

    private fun getActionCallback(): ActionCallback {
        return actionCallback
    }

    fun setActionCallback(actionCallback: ActionCallback) {
        this.actionCallback = actionCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_manga_view, parent, false)
            vh = ItemViewHolder(view)
            return vh
        } else if (viewType == VIEW_LOADING) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_loading_view, parent, false)
            vh = LoadingViewHolder(view)
            return vh
        }
        return null!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (mMangaList[position].title != null) VIEW_ITEM else VIEW_LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.txtTitle?.text = mMangaList[position].title
            Glide.with(context).load(mMangaList[position].pictureUrl).into(holder.ivPicture!!)
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    getActionCallback().OnClick(position, mMangaList[position])
                }
            })
        } else if (holder is LoadingViewHolder) {
            holder.btnLoadMore?.visibility = View.VISIBLE
            holder.progressBar?.visibility = View.GONE
            holder.btnLoadMore?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    holder.progressBar?.visibility = View.VISIBLE
                    holder.btnLoadMore?.visibility = View.GONE
                    getActionCallback().OnLoadMore()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return mMangaList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class LoadingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var layoutLoading = itemView?.findViewById<LinearLayout>(R.id.item_loading)
        var progressBar = itemView?.findViewById<ProgressBar>(R.id.progressBar)
        var btnLoadMore = itemView?.findViewById<Button>(R.id.btnLoadMore)
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var ivPicture = itemView?.findViewById<ImageView>(R.id.ivPicture)
        var txtTitle = itemView?.findViewById<TextView>(R.id.txtTitle)
    }

    interface ActionCallback {
        fun OnClick(position: Int, manga: Manga)

        fun OnLoadMore()
    }
}