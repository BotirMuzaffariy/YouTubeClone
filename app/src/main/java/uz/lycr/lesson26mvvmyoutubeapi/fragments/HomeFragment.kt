package uz.lycr.lesson26mvvmyoutubeapi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson26mvvmyoutubeapi.R
import uz.lycr.lesson26mvvmyoutubeapi.activities.VideoActivity
import uz.lycr.lesson26mvvmyoutubeapi.adapters.VideosAdapter
import uz.lycr.lesson26mvvmyoutubeapi.databinding.FragmentHomeBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.Item
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiClient
import uz.lycr.lesson26mvvmyoutubeapi.utils.Status
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.ViewModelFactory
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.SearchViewModel

class HomeFragment : Fragment() {

    private lateinit var adapter: VideosAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var listener: VideosAdapter.VideoOnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        listener = object : VideosAdapter.VideoOnClickListener {
            override fun onMoreBtnClick(view: View) {
                val popupMenu = PopupMenu(requireContext(), view)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.show()
            }

            override fun onItemClick(item: Item) {
                val intent = Intent(requireActivity(), VideoActivity::class.java)
                intent.putExtra("item", item)
                startActivity(intent)
            }

        }

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiClient.apiService)
        )[SearchViewModel::class.java]

        viewModel.getSearchLiveData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> binding.pbLoad.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.pbLoad.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    val list = it.data?.items
                    for (item in list ?: emptyList()) item.snippet.title.replace("&#39;", "`", true)

                    adapter.submitList(list)
                    binding.pbLoad.visibility = View.INVISIBLE
                }
            }
        })

        adapter = VideosAdapter(listener)
        binding.rvHome.adapter = adapter

        return binding.root
    }

}