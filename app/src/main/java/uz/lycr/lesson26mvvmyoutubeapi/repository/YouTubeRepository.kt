package uz.lycr.lesson26mvvmyoutubeapi.repository

import kotlinx.coroutines.flow.flow
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiService

class YouTubeRepository(var apiService: ApiService) {

    suspend fun getSearchApiData(query: String = "", order: String = "date") =
        flow { emit(apiService.search(query = query, order = order)) }

    suspend fun getChannelApiData(channelId: String) =
        flow { emit(apiService.getChannelVideos(channelId = channelId)) }

//    suspend fun getVideoInfo(videoId: String) =
//        flow { emit(apiService.getVideoInfo(videoId = videoId)) }
//
//    suspend fun getChannelInfo(channelId: String) =
//        flow { emit(apiService.getChannelInfo(channelId = channelId)) }

}