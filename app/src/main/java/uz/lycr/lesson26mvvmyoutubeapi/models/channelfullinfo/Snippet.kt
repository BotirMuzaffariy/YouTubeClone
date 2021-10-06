package uz.lycr.lesson26mvvmyoutubeapi.models.channelfullinfo

data class Snippet(
    val country: String,
    val description: String,
    val localized: Localized,
    val publishedAt: String,
    val thumbnails: Thumbnails,
    val title: String
)