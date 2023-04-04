package social.network.domain

class TimeLine(val messages: List<Message>) {
    fun sortByRecentFirst(): List<Message> = messages.sortedByDescending { it.ts }
}

