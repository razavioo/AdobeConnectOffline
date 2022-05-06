package domain.model

data class Stream(
    val id: String,
    val name: String,
    val type: StreamType,
    val startTime: Long
) : Comparable<Stream> {
    var stopTime: Long = -1

    fun getDuration() = stopTime - startTime

    override fun compareTo(other: Stream): Int {
        return (this.startTime - other.startTime).toInt()
    }
}