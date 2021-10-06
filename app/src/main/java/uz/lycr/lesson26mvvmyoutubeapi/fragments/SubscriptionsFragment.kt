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
import uz.lycr.lesson26mvvmyoutubeapi.activities.SubscriptionChannelAdapter
import uz.lycr.lesson26mvvmyoutubeapi.activities.VideoActivity
import uz.lycr.lesson26mvvmyoutubeapi.adapters.VideosAdapter
import uz.lycr.lesson26mvvmyoutubeapi.databinding.FragmentSubscriptionsBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.Item
import uz.lycr.lesson26mvvmyoutubeapi.models.subscriptionchannel.SubscriptionChannelM
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiClient
import uz.lycr.lesson26mvvmyoutubeapi.utils.Status
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.ChannelViewModel
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.SearchViewModel
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.ViewModelFactory

class SubscriptionsFragment : Fragment() {

    private lateinit var adapter: VideosAdapter
    private lateinit var viewModel: ChannelViewModel
    private lateinit var binding: FragmentSubscriptionsBinding
    private lateinit var listener: VideosAdapter.VideoOnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiClient.apiService)
        )[ChannelViewModel::class.java]

        setTopRv()

        setBottomRv()

        return binding.root
    }

    private fun setTopRv() {
        val list = ArrayList<SubscriptionChannelM>()
        list.add(SubscriptionChannelM("PDP Academy", "https://yt3.ggpht.com/pl-pa9hLg5NS2sXUlKsvpDwoinfjlKzYb8cM0zqGVxUUBDeRbGegGZbC8QRcPj62yiFzYN70Lg=s800-c-k-c0x00ffffff-no-rj", "UCLRXXDGv3L_gGxUHFY8D45g", true))
        list.add(SubscriptionChannelM("Sanjar Suvonov", "https://yt3.ggpht.com/ytc/AAUvwnh6Mb5nfmWshSsxe70T51oojYcncPqsXMYjQrMKxA=s800-c-k-c0x00ffffff-no-rj", "UCb6dc_1RkwR7G3anuO6N8Kg", false))

        binding.rvChannels.adapter = SubscriptionChannelAdapter(
            list,
            object : SubscriptionChannelAdapter.SubscriptionClickListener {
                override fun onItemClick(channelId: String) {

                }
            })
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

        viewModel.getChannelLiveData("UCLRXXDGv3L_gGxUHFY8D45g").observe(viewLifecycleOwner, {
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
        binding.rvBottom.adapter = adapter
    }

}