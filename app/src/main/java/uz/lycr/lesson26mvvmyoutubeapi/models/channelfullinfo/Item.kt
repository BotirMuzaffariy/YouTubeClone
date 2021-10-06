package uz.lycr.lesson26mvvmyoutubeapi.models.channelfullinfo

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet,
    val statistics: Statistics
)