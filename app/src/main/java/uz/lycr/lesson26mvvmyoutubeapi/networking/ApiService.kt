package uz.lycr.lesson26mvvmyoutubeapi.networking

import retrofit2.http.GET
import retrofit2.http.Query
import uz.lycr.lesson26mvvmyoutubeapi.models.channelfullinfo.ChannelFullInfoM
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.VideoApiM
import uz.lycr.lesson26mvvmyoutubeapi.models.videostatistics.VideoStatM
import uz.lycr.lesson26mvvmyoutubeapi.utils.Constants

interface ApiService {

    @GET("search")
    suspend fun search(
        @Query("key") key: String = Constants.KEY,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResult: Int = 50,
        @Query("order") order: String = "date",
        @Query("type") type: String = "videos",
        @Query("pageToken") pageToken: String = "",
        @Query("q") query: String = ""
    ): VideoApiM

    @GET("search")
    suspend fun getChannelVideos(
        @Query("key") key: String = Constants.KEY,
        @Query("channelId") channelId: String,
        @Query("part") part: String = "snippet,id",
        @Query("order") order: String = "date",
        @Query("maxResults") maxResult: Int = 50,
        @Query("pageToken") pageToken: String = "",
        @Query("type") type: String = "video"
    ): VideoApiM

    @GET("videos")
    suspend fun getVideoInfo(
        @Query("key") key: String = Constants.KEY,
        @Query("id") videoId: String,
        @Query("part") part: String = "statistics,snippet",
    ): VideoStatM

    @GET("channels")
    suspend fun getChannelInfo(
        @Query("key") key: String = Constants.KEY,
        @Query("id") channelId: String,
        @Query("part") part: String = "snippet,statistics"
    ): ChannelFullInfoM

}