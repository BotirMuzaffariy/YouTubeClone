package uz.lycr.lesson26mvvmyoutubeapi.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import uz.lycr.lesson26mvvmyoutubeapi.R
import uz.lycr.lesson26mvvmyoutubeapi.activities.VideoActivity
import uz.lycr.lesson26mvvmyoutubeapi.adapters.ExploreTopAdapter
import uz.lycr.lesson26mvvmyoutubeapi.adapters.VideosAdapter
import uz.lycr.lesson26mvvmyoutubeapi.databinding.FragmentExploreBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.Item
import uz.lycr.lesson26mvvmyoutubeapi.models.exploretop.ExploreTopM
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiClient
import uz.lycr.lesson26mvvmyoutubeapi.utils.Status
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.SearchViewModel
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.ViewModelFactory

class ExploreFragment : Fragment() {

    private lateinit var adapter: VideosAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var list: ArrayList<ExploreTopM>
    private lateinit var binding: FragmentExploreBinding
    private lateinit var listener: VideosAdapter.VideoOnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        loadList()
        binding.rvExploreTop.adapter = ExploreTopAdapter(list)

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiClient.apiService)
        )[SearchViewModel::class.java]

        setBottomRv()

        return binding.root
    }

    private fun loadList() {
        list = ArrayList()

        list.add(ExploreTopM("Trending", R.drawable.ic_tranding, R.drawable.trending))
        list.add(ExploreTopM("Music", R.drawable.ic_musical_note, R.drawable.music))
        list.add(ExploreTopM("Gaming", R.drawable.ic_gaming, R.drawable.gaming))
        list.add(ExploreTopM("News", R.drawable.ic_news, R.drawable.news))
        list.add(ExploreTopM("Fashion & Beauty", R.drawable.ic_fashion, R.drawable.fashion))
        list.add(ExploreTopM("Learning", R.drawable.ic_light, R.drawable.learning))
        list.add(ExploreTopM("Live", R.drawable.ic_live, R.drawable.live))
        list.add(ExploreTopM("Sports", R.drawable.ic_sport, R.drawable.sport))
    }

    private fun setBottomRv() {
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

        viewModel.getSearchLiveData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                    .show()
                Status.SUCCESS -> {
                    val list = it.data?.items
                    for (item in list ?: emptyList()) item.snippet.title.replace("&#39;", "`", true)

                    adapter.submitList(list)
                }
            }
        })

        adapter = VideosAdapter(listener)
        binding.rvBottom.adapter = adapter

    }

}