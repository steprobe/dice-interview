package com.steprobe.diceinterview.features.artistsearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.R
import com.steprobe.diceinterview.databinding.FragmentArtistSearchBinding
import com.steprobe.diceinterview.features.details.ArtistDetailsFragment
import com.steprobe.diceinterview.textFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@AndroidEntryPoint
class ArtistSearchFragment : Fragment() {

    private val viewModel: ArtistSearchViewModel by viewModels()

    private var _binding: FragmentArtistSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArtistSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.artistList.adapter = ArtistsAdapter { artist ->
            val args = Bundle().apply { putParcelable(ArtistDetailsFragment.ARGS_ARTIST, artist) }
            findNavController().navigate(R.id.action_search_to_details, args)
        }

        subscribeToArtists()

        // Debouncing so as not to saturate API and get kicked out
        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchField.textFlow().debounce(400).collect {
                viewModel.searchArtists(it)
            }
        }
    }

    private fun subscribeToArtists() {
        viewModel.artists.observe(viewLifecycleOwner) { artists ->
            when (artists) {
                is DataState.Success -> onSearchResult(artists.data)
                is DataState.Error -> onSearchFailed()
                DataState.Loading -> TODO()
            }
        }
    }

    private fun onSearchResult(data: List<ArtistDisplayModel>) {
        (binding.artistList.adapter as ArtistsAdapter).submitList(data)
    }

    private fun onSearchFailed() {
        Snackbar.make(requireView(), R.string.search_failed, Snackbar.LENGTH_LONG)
            .setAction(R.string.search_failed_retry) {
                viewModel.searchArtists(binding.searchField.text.toString())
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
