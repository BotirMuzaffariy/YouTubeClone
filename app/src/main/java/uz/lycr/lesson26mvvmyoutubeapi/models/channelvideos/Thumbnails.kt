package uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos

import java.io.Serializable

data class Thumbnails(
    val default: Default,
    val high: High,
    val medium: Medium
):Serializable