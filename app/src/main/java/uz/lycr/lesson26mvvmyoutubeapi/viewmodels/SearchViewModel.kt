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

class SearchViewModel(apiService: ApiService) : ViewModel() {

    private val searchLiveData = MutableLiveData<Resource<VideoApiM>>()
    private val repository = YouTubeRepository(apiService)

    fun getSearchLiveData(
        query: String = "",
        order: String = "date"
    ): MutableLiveData<Resource<VideoApiM>> {
        viewModelScope.launch(Dispatchers.IO) {
            searchLiveData.postValue(Resource.loading(null))

            repository.getSearchApiData(query, order)
                .catch {
                    searchLiveData.postValue(Resource.error(it.message ?: "Error", null))
                }.collect {
                    searchLiveData.postValue(Resource.success(it))
                }
        }

        return searchLiveData
    }

}