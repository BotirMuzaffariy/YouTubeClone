package uz.lycr.lesson26mvvmyoutubeapi.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.lycr.lesson26mvvmyoutubeapi.R
import uz.lycr.lesson26mvvmyoutubeapi.databinding.ItemVideosBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.channelfullinfo.ChannelFullInfoM
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.Item
import uz.lycr.lesson26mvvmyoutubeapi.models.videostatistics.VideoStatM
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiClient
import uz.lycr.lesson26mvvmyoutubeapi.utils.Functions

class VideosAdapter(var listener: VideoOnClickListener) :
    ListAdapter<Item, VideosAdapter.HomeVh>(HomeDiffUtil()) {

    inner class HomeVh(private var itemBinding: ItemVideosBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(item: Item) {
            val videoInfoLd = MutableLiveData<VideoStatM>()
            val channelInfoLd = MutableLiveData<ChannelFullInfoM>()

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        videoInfoLd.value =
                            ApiClient.apiService.getVideoInfo(videoId = item.id.videoId)
                        channelInfoLd.value =
                            ApiClient.apiService.getChannelInfo(channelId = item.snippet.channelId)
                    }
                } catch (e: Exception) {
                    Log.d("HomeAdapter", "onBind: ${e.message}")
                }
            }

            var viewCount: String
            var channelImgUrl: String

            val time = Functions.sortDate(item.snippet.publishTime)
            val channelName = item.snippet.channelTitle

            videoInfoLd.observeForever {
                viewCount = Functions.sortViewCount(it.items[0].statistics.viewCount)
                itemBinding.tvMoreInfo.text = "$channelName · $viewCount views · $time"
            }

            channelInfoLd.observeForever {
                channelImgUrl = it.items[0].snippet.thumbnails.medium.url
                if (channelImgUrl != "")
                    Picasso.get().load(channelImgUrl).placeholder(R.drawable.placeholder)
                        .error(R.drawable.no_image).into(itemBinding.ivChannelImg)
            }

            itemBinding.tvVideoName.text = item.snippet.title

            Picasso.get().load(item.snippet.thumbnails.high.url)
                .placeholder(R.drawable.video_placeholder).error(R.drawable.no_video)
                .into(itemBinding.ivVideoImg)

            itemBinding.ivMore.setOnClickListener { listener.onMoreBtnClick(it) }
            itemBinding.root.setOnClickListener { listener.onItemClick(item) }
        }
    }

    interface VideoOnClickListener {
        fun onMoreBtnClick(view: View)
        fun onItemClick(item: Item)
    }

    class HomeDiffUtil : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVh {
        return HomeVh(ItemVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeVh, position: Int) {
        holder.onBind(getItem(position))
    }

}