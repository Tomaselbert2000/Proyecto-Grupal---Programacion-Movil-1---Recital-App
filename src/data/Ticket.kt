package main.kotlin.data

data class Ticket(
    val id: Long,
    val eventId: Long,
    val quantity: Int,
    val section: String,
)
