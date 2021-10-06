package uz.lycr.lesson26mvvmyoutubeapi.models.channelfullinfo

data class ChannelFullInfoM(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)