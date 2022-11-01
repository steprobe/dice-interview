package com.steprobe.diceinterview.features.artistsearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.databinding.FragmentArtistSearchBinding
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

        subscribeToArtists()

        binding.artistList.adapter = ArtistsAdapter()

        // Debouncing so as not to saturate API and get kicked out
        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchField.textFlow().debounce(400).collect {
                Log.e("STEO", "Searching " + it)
                viewModel.searchArtists(it)
            }
        }
    }

    private fun subscribeToArtists() {
        viewModel.artists.observe(viewLifecycleOwner) { artists ->
            if (artists is DataState.Success) {
                (binding.artistList.adapter as ArtistsAdapter).submitList(artists.data)
            } else if (artists is DataState.Error) {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
