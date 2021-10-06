package uz.lycr.lesson26mvvmyoutubeapi.viewmodels

import androidx.lifecycle.ViewModel
import java.lang.IllegalArgumentException
import androidx.lifecycle.ViewModelProvider
import uz.lycr.lesson26mvvmyoutubeapi.networking.ApiService

class ViewModelFactory(var apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelViewModel::class.java)) {
            return ChannelViewModel(apiService) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(apiService) as T
        }
        throw IllegalArgumentException("Error view model")
    }
}