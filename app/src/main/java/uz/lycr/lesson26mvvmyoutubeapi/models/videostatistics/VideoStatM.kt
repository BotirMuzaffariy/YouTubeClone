package uz.lycr.lesson26mvvmyoutubeapi.models.videostatistics

data class VideoStatM(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)