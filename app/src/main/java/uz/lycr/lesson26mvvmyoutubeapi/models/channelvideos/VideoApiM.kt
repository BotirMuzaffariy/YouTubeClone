package uz.lycr.lesson26mvvmyoutubeapi.models.channelvideos

data class VideoApiM(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String = "",
    val pageInfo: PageInfo,
    val prevPageToken: String = "",
    val regionCode: String
)