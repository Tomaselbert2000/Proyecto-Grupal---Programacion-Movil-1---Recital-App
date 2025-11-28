package com.example.myapplication.data.superclass

import java.time.LocalDateTime

data class Ticket(
    val id: Long,
    val eventId: Long,
    val quantity: Int,
    val section: String,
    val price: Double = 10000.0,
    val idMedioDePagoUsado: Long,
    val ticketDateTime: LocalDateTime
) {
    fun calculateTicketSubtotal(): Double {
        return quantity * price
    }
}
