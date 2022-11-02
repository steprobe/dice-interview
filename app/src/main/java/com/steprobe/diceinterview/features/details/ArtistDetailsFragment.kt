package com.steprobe.diceinterview.features.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.steprobe.diceinterview.DataState
import com.steprobe.diceinterview.MainActivity
import com.steprobe.diceinterview.R
import com.steprobe.diceinterview.databinding.FragmentArtistDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailsFragment : Fragment() {

    private val viewModel: ArtistDetailsViewModel by viewModels()

    private var _binding: FragmentArtistDetailsBinding? = null
    private val binding get() = _binding!!

    private val artistId: String
        get() = arguments?.getString(ARG_ARTIST_ID)!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArtistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.albumList.adapter = AlbumAdapter()

        subscribeToDetails()
        subscribeToAlbums()

        viewModel.loadDetails(artistId)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarTitle(
            arguments?.getString(ARG_ARTIST_TITLE) ?: ""
        )
    }

    private fun subscribeToAlbums() {
        viewModel.albums.observe(viewLifecycleOwner) {
            (binding.albumList.adapter as AlbumAdapter).submitList(it)
        }
    }

    private fun subscribeToDetails() {
        viewModel.details.observe(viewLifecycleOwner) { details ->
            when (details) {
                is DataState.Success -> onDetailsLoaded(details.data)
                is DataState.Error -> onDetailsLoadError()
                is DataState.Loading -> TODO()
            }
        }
    }

    private fun onDetailsLoaded(data: ArtistDetailsDisplayModel) {

        binding.artistLocation.text = data.placeOfOrigin
        binding.artistLifetime.text = if (data.disbanded)
            requireContext().getString(R.string.duration_disbanded, data.begin, data.end) else
            requireContext().getString(R.string.duration_ongoing, data.begin)
    }

    private fun onDetailsLoadError() {
        Snackbar.make(requireView(), R.string.details_failed, Snackbar.LENGTH_LONG)
            .setAction(R.string.details_failed_retry) {
                viewModel.loadDetails(artistId)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_ARTIST_ID = "arg artist_id"
        const val ARG_ARTIST_TITLE = "arg_artist_title"
    }
}
