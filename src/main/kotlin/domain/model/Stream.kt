package domain.model

data class Stream(
    val id: String,
    val name: String,
    val type: StreamType,
    val startTime: Long
)