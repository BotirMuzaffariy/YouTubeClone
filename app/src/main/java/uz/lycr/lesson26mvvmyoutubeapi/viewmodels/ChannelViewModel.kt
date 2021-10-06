package uz.lycr.lesson26mvvmyoutubeapi.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos.VideoApiM
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiService
import uz.lycr.lesson26mvvmyoutubeapi.repository.YouTubeRepository
import uz.lycr.lesson26mvvmyoutubeapi.utils.Resource

class ChannelViewModel(apiService: ApiService) : ViewModel() {

    private val channelLiveData = MutableLiveData<Resource<VideoApiM>>()
    private val repository = YouTubeRepository(apiService)

    fun getChannelLiveData(channelId: String): MutableLiveData<Resource<VideoApiM>> {
        viewModelScope.launch(Dispatchers.IO) {
            channelLiveData.postValue(Resource.loading(null))

            repository.getChannelApiData(channelId = channelId)
                .catch {
                    channelLiveData.postValue(Resource.error(it.message ?: "Error", null))
                }.collect {
                    channelLiveData.postValue(Resource.success(it))
                }
        }

        return channelLiveData
    }

}