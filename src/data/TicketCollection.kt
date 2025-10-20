package data

data class TicketCollection(
    val id: Long,
    val userId: Long,
    val paymentId: Long,
    val ticketCollection: MutableList<Long>,
)
