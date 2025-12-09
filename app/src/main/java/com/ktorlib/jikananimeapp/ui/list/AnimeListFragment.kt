package com.ktorlib.jikananimeapp.ui.list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktorlib.jikananimeapp.R
import com.ktorlib.jikananimeapp.data.local.AnimeDatabase
import com.ktorlib.jikananimeapp.data.remote.RetrofitClient
import com.ktorlib.jikananimeapp.data.repository.AnimeRepository
import com.ktorlib.jikananimeapp.ui.common.UiState
import com.ktorlib.jikananimeapp.ui.detail.AnimeDetailFragment
import com.ktorlib.jikananimeapp.util.Constants.KEY_ANIME_ID
import com.ktorlib.jikananimeapp.util.Constants.KEY_ANIME_POSTER
import com.ktorlib.jikananimeapp.util.NetworkUtil
import kotlinx.coroutines.launch


class AnimeListFragment : Fragment(R.layout.fragment_anime_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dao = AnimeDatabase.getInstance(requireContext()).animeDao()
        val repo = AnimeRepository(RetrofitClient.api, dao)
        val vm = AnimeListViewModel(repo)

        val recycler = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler)
        val adapter = AnimeAdapter { anime ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.container,
                    AnimeDetailFragment().apply {
                        arguments = Bundle().apply {
                            putInt(KEY_ANIME_ID, anime.id)
                            putString(KEY_ANIME_POSTER, anime.poster)
                        }
                    })
                .addToBackStack(null)
                .commit()
        }

        vm.refresh()

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            vm.anime.collect {
                Log.d("RV_CHECK", "List size: ${it.size}")
                adapter.submitList(it)
            }
        }

        val offlineBanner = view.findViewById<TextView>(R.id.offlineBanner)

        lifecycleScope.launch {
            vm.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        offlineBanner.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        offlineBanner.visibility = View.VISIBLE
                        offlineBanner.text = state.message
                    }
                    is UiState.Success -> {
                        offlineBanner.visibility = if (NetworkUtil.isOnline(requireContext())) View.GONE else View.VISIBLE
                    }
                }
            }
        }

    }
}