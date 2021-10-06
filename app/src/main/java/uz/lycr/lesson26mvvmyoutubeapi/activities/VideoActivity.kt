package uz.lycr.lesson26mvvmyoutubeapi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.lycr.lesson26mvvmyoutubeapi.R
import uz.lycr.lesson26mvvmyoutubeapi.adapters.VideosAdapter
import uz.lycr.lesson26mvvmyoutubeapi.databinding.ActivityVideoBinding
import uz.lycr.lesson26mvvmyoutubeapi.models.channelfullinfo.ChannelFullInfoM
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.Item
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.VideoApiM
import uz.lycr.lesson26mvvmyoutubeapi.models.videostatistics.VideoStatM
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiClient
import uz.lycr.lesson26mvvmyoutubeapi.repository.YouTubeRepository
import uz.lycr.lesson26mvvmyoutubeapi.utils.Constants
import uz.lycr.lesson26mvvmyoutubeapi.utils.Functions
import uz.lycr.lesson26mvvmyoutubeapi.utils.Status
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.SearchViewModel
import uz.lycr.lesson26mvvmyoutubeapi.viewmodels.ViewModelFactory
import java.lang.Exception
import java.lang.StringBuilder

class VideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private var isLike = false
    private var isDislike = false

    private lateinit var item: Item
    private lateinit var adapter: VideosAdapter
    private lateinit var binding: ActivityVideoBinding
    private lateinit var listener: VideosAdapter.VideoOnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        item = intent.getSerializableExtra("item") as Item
        window.statusBarColor = resources.getColor(R.color.black)

        binding.playerView.initialize(Constants.KEY, this)

        val videoInfoLd = MutableLiveData<VideoStatM>()
        val channelInfoLd = MutableLiveData<ChannelFullInfoM>()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                coroutineScope {
                    try {
                        videoInfoLd.value =
                            ApiClient.apiService.getVideoInfo(videoId = item.id.videoId)
                    } catch (e: Exception) {
                        Toast.makeText(this@VideoActivity, e.message, Toast.LENGTH_SHORT).show()
                    }

                    channelInfoLd.value =
                        ApiClient.apiService.getChannelInfo(channelId = item.snippet.channelId)
                }
            } catch (e: Exception) {
                Toast.makeText(this@VideoActivity, e.message, Toast.LENGTH_SHORT).show()
                Log.d("VideoActivity", "GlobalScope: ${e.message}")
            }
        }

        binding.tvVideoName.text = item.snippet.title
        binding.tvDescription.text = item.snippet.description
        binding.tvChannelName.text = item.snippet.channelTitle

        videoInfoLd.observeForever {
            val item1 = it.items[0]
            val time = Functions.sortDate(item.snippet.publishTime)
            val views = Functions.sortViewCount(item1.statistics.viewCount)
            val likes = Functions.sortViewCount(item1.statistics.likeCount)
            val dislikes = Functions.sortViewCount(item1.statistics.dislikeCount)
            val comments = Functions.sortViewCount(item1.statistics.commentCount)

            if (item1.snippet.tags.isNullOrEmpty()) {
                binding.tvTags.visibility = View.GONE
            } else {
                val tags = StringBuilder()

                for (tag in item1.snippet.tags) {
                    tags.append("#$tag").append(" ")
                }

                binding.tvTags.text = tags.toString()
                binding.tvTags2.text = tags.toString()
            }

            binding.tvLikesCount.text = likes
            binding.tvDislikesCount.text = dislikes
            binding.tvCommentsCount.text = comments
            binding.tvMoreInfo.text = "$views views · $time"
        }

        channelInfoLd.observeForever {
            val subs = Functions.sortViewCount(it.items[0].statistics.subscriberCount)
            binding.tvChannelSubscribers.text = "$subs subscribers"

            Picasso.get().load(it.items[0].snippet.thumbnails.high.url)
                .placeholder(R.drawable.placeholder).error(R.drawable.no_image)
                .into(binding.ivChannelImg)
        }

        binding.clMain.setOnClickListener {
            if (binding.llComments.isVisible) {
                binding.llComments.visibility = View.GONE

                binding.ivIconDown.animate()
                    .setDuration(300)
                    .setInterpolator(OvershootInterpolator())
                    .rotation(0f)
                    .start()
            } else {
                binding.llComments.visibility = View.VISIBLE

                binding.ivIconDown.animate()
                    .setDuration(300)
                    .setInterpolator(OvershootInterpolator())
                    .rotation(180f)
                    .start()
            }
        }

        binding.cvLike.setOnClickListener {
            var likeCount = -1
            var dislikeCount = -1

            if (binding.tvLikesCount.text.isDigitsOnly()) likeCount =
                binding.tvLikesCount.text.toString().toInt()
            if (binding.tvLikesCount.text.isDigitsOnly()) dislikeCount =
                binding.tvDislikesCount.text.toString().toInt()

            if (isLike) {
                isLike = false
                if (likeCount != -1) likeCount--
                binding.ivLike.setImageResource(R.drawable.ic_like_hand)
            } else {
                if (isDislike) {
                    isDislike = false
                    if (dislikeCount != -1) dislikeCount--
                    binding.ivDislike.setImageResource(R.drawable.ic_like_hand)
                }

                isLike = true
                if (likeCount != -1) likeCount++
                binding.ivLike.setImageResource(R.drawable.ic_like_filled)
            }

            if (likeCount != -1) binding.tvLikesCount.text = likeCount.toString()
            if (dislikeCount != -1) binding.tvDislikesCount.text = dislikeCount.toString()
        }

        binding.cvDislike.setOnClickListener {
            var likeCount = -1
            var dislikeCount = -1

            if (binding.tvLikesCount.text.isDigitsOnly()) likeCount =
                binding.tvLikesCount.text.toString().toInt()
            if (binding.tvLikesCount.text.isDigitsOnly()) dislikeCount =
                binding.tvDislikesCount.text.toString().toInt()

            if (isDislike) {
                isDislike = false
                if (dislikeCount != -1) dislikeCount--
                binding.ivDislike.setImageResource(R.drawable.ic_like_hand)
            } else {
                if (isLike) {
                    isLike = false
                    if (likeCount != -1) likeCount--
                    binding.ivLike.setImageResource(R.drawable.ic_like_hand)
                }

                if (dislikeCount != -1) dislikeCount++
                isDislike = true
                binding.ivDislike.setImageResource(R.drawable.ic_like_filled)
            }

            if (likeCount != -1) binding.tvLikesCount.text = likeCount.toString()
            if (dislikeCount != -1) binding.tvDislikesCount.text = dislikeCount.toString()
        }

        binding.tvSubscribe.setOnClickListener {
            if (binding.tvSubscribe.text == "SUBSCRIBE") {
                binding.tvSubscribe.text = "SUBSCRIBED"
                binding.tvSubscribe.setTextColor(resources.getColor(R.color.icon_color))
            } else {
                binding.tvSubscribe.text = "SUBSCRIBE"
                binding.tvSubscribe.setTextColor(resources.getColor(R.color.red))
            }
        }

        setRv()

    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (p2) {
            p1?.cueVideo(item.id.videoId)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {

    }

    private fun setRv() {
        val liveData = MutableLiveData<VideoApiM>()
        val repository = YouTubeRepository(ApiClient.apiService)
        listener = object : VideosAdapter.VideoOnClickListener {
            override fun onMoreBtnClick(view: View) {
                val popupMenu = PopupMenu(this@VideoActivity, view)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.show()
            }

            override fun onItemClick(item: Item) {
                val intent = Intent(this@VideoActivity, VideoActivity::class.java)
                intent.putExtra("item", item)
                startActivity(intent)
                finish()
            }

        }

        GlobalScope.launch(Dispatchers.Main) {
            repository.getSearchApiData(order = "date")
                .catch {
                    Toast.makeText(this@VideoActivity, it.message, Toast.LENGTH_SHORT).show()
                }.collect {
                    liveData.postValue(it)
                }
        }

        liveData.observeForever{
            adapter.submitList(it.items)
        }

        adapter = VideosAdapter(listener)
        binding.rvVideo.adapter = adapter
    }

}