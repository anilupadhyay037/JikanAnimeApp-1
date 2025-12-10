package com.ktorlib.jikananimeapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.ktorlib.jikananimeapp.databinding.FragmentAnimeDetailBinding
import com.ktorlib.jikananimeapp.databinding.FragmentAnimeListBinding
import com.ktorlib.jikananimeapp.util.Constants.DEFAULT_EPISODES
import com.ktorlib.jikananimeapp.util.Constants.KEY_ANIME_ID
import com.ktorlib.jikananimeapp.util.Constants.KEY_ANIME_POSTER
import com.ktorlib.jikananimeapp.util.Constants.NO_DATA
import com.ktorlib.jikananimeapp.util.NetworkUtil


class AnimeDetailFragment : Fragment(R.layout.fragment_anime_detail) {

    private var player: ExoPlayer? = null
    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAnimeDetailBinding.bind(view)

        val animeId = requireArguments().getInt(KEY_ANIME_ID)
        val posterUrl = requireArguments().getString(KEY_ANIME_POSTER)

        val repo = AnimeRepository(
            RetrofitClient.api,
            AnimeDatabase.getInstance(requireContext()).animeDao()
        )

        val vm = AnimeDetailViewModel(repo)

        if (NetworkUtil.isOnline(requireContext())) {
            vm.load(animeId).observe(viewLifecycleOwner) { data ->

                binding.title.text = data.title
                binding.info.text =
                    "⭐ ${data.score ?: NO_DATA} | Episodes: ${data.episodes ?: DEFAULT_EPISODES}"
                binding.synopsis.text = data.synopsis ?: resources.getString(R.string.no_synopsis_available)
                //  Show poster first
                Glide.with(requireContext())
                    .load(posterUrl)
                    .into(binding.posterImage)

                //  Handle trailer safely
                val trailerUrl = data.trailer?.url

                if (!trailerUrl.isNullOrEmpty()) {
                    player = ExoPlayer.Builder(requireContext()).build()
                    val playerView = PlayerView(requireContext())
                    playerView.player = player
                    binding.videoContainer as ViewGroup
                    binding.videoContainer.addView(playerView)

                    player?.setMediaItem(MediaItem.fromUri(Uri.parse(trailerUrl)))
                    player?.prepare()
                } else {
                    binding.posterImage.visibility = View.VISIBLE
                }

                data.genres.forEach { genre ->
                    val chip = Chip(requireContext())
                    chip.text = genre.name
                    chip.isClickable = false
                    chip.isCheckable = false
                    binding.genreGroup.addView(chip)
                }
            }
        } else {
            Toast.makeText(requireContext(),requireContext().getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
    }
}