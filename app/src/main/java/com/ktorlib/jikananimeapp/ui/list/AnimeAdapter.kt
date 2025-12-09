package com.ktorlib.jikananimeapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ktorlib.jikananimeapp.R
import com.ktorlib.jikananimeapp.data.local.AnimeEntity
import com.ktorlib.jikananimeapp.util.Constants.DEFAULT_EPISODES
import com.ktorlib.jikananimeapp.util.Constants.IMAGE_PLACEHOLDER
import com.ktorlib.jikananimeapp.util.Constants.NO_DATA

class AnimeAdapter(
    private val onClick: (AnimeEntity) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    private val items = mutableListOf<AnimeEntity>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_anime, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<AnimeEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged() // 🔥 REQUIRED
    }

    inner class AnimeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView =
            itemView.findViewById(R.id.poster)

        private val title: TextView =
            itemView.findViewById(R.id.title)
        private val meta: TextView =
            itemView.findViewById(R.id.meta)

        fun bind(item: AnimeEntity) {

            title.text = item.title
            meta.text =
                "⭐ ${item.score ?: NO_DATA} | Ep ${item.episodes ?: DEFAULT_EPISODES}"

            // ✅ Load image from Room URL
            Glide.with(itemView.context)
                .load(item.poster)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(IMAGE_PLACEHOLDER)
                .into(poster)

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}