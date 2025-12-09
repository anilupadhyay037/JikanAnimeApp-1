package com.ktorlib.jikananimeapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ktorlib.jikananimeapp.R
import com.ktorlib.jikananimeapp.data.local.AnimeDatabase
import com.ktorlib.jikananimeapp.data.remote.RetrofitClient
import com.ktorlib.jikananimeapp.data.repository.AnimeRepository
import com.ktorlib.jikananimeapp.util.Constants.DEFAULT_EPISODES
import com.ktorlib.jikananimeapp.util.Constants.KEY_ANIME_ID
import com.ktorlib.jikananimeapp.util.Constants.KEY_ANIME_POSTER
import com.ktorlib.jikananimeapp.util.Constants.NO_DATA


class AnimeDetailFragment : Fragment(R.layout.fragment_anime_detail) {

    private var player: ExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val animeId = requireArguments().getInt(KEY_ANIME_ID)
        val posterUrl = requireArguments().getString(KEY_ANIME_POSTER)

        val repo = AnimeRepository(
            RetrofitClient.api,
            AnimeDatabase.getInstance(requireContext()).animeDao()
        )

        val vm = AnimeDetailViewModel(repo)

        val posterImage = view.findViewById<ImageView>(R.id.posterImage)
        val title = view.findViewById<TextView>(R.id.title)
        val info = view.findViewById<TextView>(R.id.info)
        val synopsis = view.findViewById<TextView>(R.id.synopsis)
        val container = view.findViewById<View>(R.id.videoContainer)
        val genreGroup = view.findViewById<ChipGroup>(R.id.genreGroup)


        vm.load(animeId).observe(viewLifecycleOwner) { data ->

            title.text = data.title
            info.text = "⭐ ${data.score ?: NO_DATA} | Episodes: ${data.episodes ?: DEFAULT_EPISODES}"
            synopsis.text = data.synopsis ?: resources.getString(R.string.no_synopsis_available)
            // ✅ Show poster first
            Glide.with(requireContext())
                .load(posterUrl)
                .into(posterImage)

            // ✅ Handle trailer safely
            val trailerUrl = data.trailer?.url

            if (!trailerUrl.isNullOrEmpty()) {
                player = ExoPlayer.Builder(requireContext()).build()
                val playerView = PlayerView(requireContext())
                playerView.player = player
                container as ViewGroup
                container.addView(playerView)

                player?.setMediaItem(MediaItem.fromUri(Uri.parse(trailerUrl)))
                player?.prepare()
            } else {
                posterImage.visibility = View.VISIBLE
            }

            data.genres.forEach { genre ->
                val chip = Chip(requireContext())
                chip.text = genre.name
                chip.isClickable = false
                chip.isCheckable = false
                genreGroup.addView(chip)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
    }
}