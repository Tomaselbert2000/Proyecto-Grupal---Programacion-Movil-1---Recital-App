package data.superclass

data class Ticket(
    val id: Long,
    val eventId: Long,
    val quantity: Int,
    val section: String,
    val precio: Double = 10000.0,
    val idMedioDePagoUsado : Long
) {
    fun calcularTotalPorTicket(): Double {
        return quantity * precio
    }
}
