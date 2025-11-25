package com.example.myapplication.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.superclass.Ticket
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
object TicketsRepository {

    private val tickets = mutableListOf<Ticket>()

    init {
        tickets.add(
            Ticket(
                1L,
                4L,
                1,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 6, 12, 8, 30)
            )
        )

        tickets.add(
            Ticket(
                2L,
                3L,
                4,
                "Campo",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 9, 18, 12,30)
            )
        )

        tickets.add(
            Ticket(
                3L,
                1L,
                2,
                "Campo",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025,11,19,8,30)
            )
        )

        tickets.add(
            Ticket(
                4L,
                5L,
                6,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 12, 24, 19, 30)

            )
        )

        tickets.add(
            Ticket(
                5L,
                2L,
                3,
                "Platea",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025,11,19,8,30)
            )
        )

        tickets.add(
            Ticket(
                6L,
                1L,
                7,
                "Campo",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 9, 12, 12,50)
            )
        )

        tickets.add(
            Ticket(
                7L,
                3L,
                4,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 2, 25, 12,0)
            )
        )

        tickets.add(
            Ticket(
                8L,
                1L,
                1,
                "Campo",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 12, 24, 19, 30)
            )
        )

        tickets.add(
            Ticket(
                9L,
                7L,
                2,
                "Campo",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 6, 12, 15,50)
            )
        )

        tickets.add(
            Ticket(
                10L,
                7L,
                4,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 9, 12, 12,50)
            )
        )

        tickets.add(
            Ticket(
                11L,
                5L,
                7,
                "Campo",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 11, 4, 10,30)
            )
        )

        tickets.add(
            Ticket(
                12L,
                2L,
                2,
                "Platea",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 9, 17, 17,0)
            )
        )

        tickets.add(
            Ticket(
                13L,
                4L,
                4,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 4, 12, 12,30)
            )
        )

        tickets.add(
            Ticket(
                14L,
                6L,
                2,
                "Platea",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 12, 24, 19, 30)
            )
        )

        tickets.add(
            Ticket(
                15L,
                2L,
                7,
                "Campo",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 12, 10, 12,50)
            )
        )

        tickets.add(
            Ticket(
                16L,
                5L,
                4,
                "Campo",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 8, 27, 14,20)
            )
        )

        tickets.add(
            Ticket(
                17L,
                3L,
                1,
                "Platea",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 11, 9, 16,30)
            )
        )

        tickets.add(
            Ticket(
                18L,
                1L,
                3,
                "Platea",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 9, 18, 12,30)
            )
        )

        tickets.add(
            Ticket(
                19L,
                3L,
                4,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 5, 22, 18, 30)
            )
        )

        tickets.add(
            Ticket(
                20L,
                1L,
                5,
                "Campo",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 12, 24, 19, 30)
            )
        )

        tickets.add(
            Ticket(
                21L,
                5L,
                6,
                "Platea",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 3, 28, 12,50)
            )
        )

        tickets.add(
            Ticket(
                22L,
                4L,
                2,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 9, 12, 12,50)
            )
        )

        tickets.add(
            Ticket(
                23L,
                6L,
                1,
                "Campo",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 6, 5, 12,30)
            )
        )

        tickets.add(
            Ticket(
                24L,
                3L,
                4,
                "Platea",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 9, 18, 12,30)
            )
        )

        tickets.add(
            Ticket(
                25L,
                2L,
                3,
                "Platea",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 9, 12, 12,50)
            )
        )

        tickets.add(
            Ticket(
                26L,
                7L,
                8,
                "Platea",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 12, 24, 19, 30)
            )
        )

        tickets.add(
            Ticket(
                27L,
                7L,
                1,
                "Campo",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 7, 22, 9,45)
            )
        )

        tickets.add(
            Ticket(
                28L,
                1L,
                4,
                "Campo",
                idMedioDePagoUsado = 1L,
                ticketDateTime = LocalDateTime.of(2025, 8, 12, 12,50)
            )
        )

        tickets.add(
            Ticket(
                29L,
                2L,
                2,
                "Campo",
                idMedioDePagoUsado = 2L,
                ticketDateTime = LocalDateTime.of(2025, 9, 18, 12,30)
            )
        )

        tickets.add(
            Ticket(
                30L,
                5L,
                3,
                "Platea",
                idMedioDePagoUsado = 3L,
                ticketDateTime = LocalDateTime.of(2025, 9, 12, 12,50)
            )
        )
    }

    fun registrarNuevoTicket(nuevoTicket: Ticket, listaDeEventosRegistrados: MutableList<Long>): Boolean {
        return this.validarIdTicket(nuevoTicket)
                && this.validarEventoAsociado(nuevoTicket, listaDeEventosRegistrados)
                && this.validarUbicacion(nuevoTicket)
                && this.validarCantidad(nuevoTicket)
                && !this.esDuplicado(nuevoTicket)
                && tickets.add(nuevoTicket)
    }

    fun obtenerListaDeIDsDeTickets(): MutableList<Long> {
        val listaDeIDsDeTicketsRegistrados = mutableListOf<Long>()
        for (ticket in this.tickets) {
            listaDeIDsDeTicketsRegistrados.add(ticket.id)
        }
        return listaDeIDsDeTicketsRegistrados
    }

    private fun validarEventoAsociado(nuevoTicket: Ticket, listaDeIDsEventosRegistrados: MutableList<Long>): Boolean {
        for (id in listaDeIDsEventosRegistrados) {
            if (nuevoTicket.eventId == id) {
                return true
            }
        }
        return false
    }

    private fun validarCantidad(nuevoTicket: Ticket): Boolean {
        return nuevoTicket.quantity >= 1
    }

    private fun validarUbicacion(nuevoTicket: Ticket): Boolean {
        return nuevoTicket.section == "Platea" || nuevoTicket.section == "Campo" || nuevoTicket.section == "Palco"
    }

    private fun esDuplicado(nuevoTicket: Ticket): Boolean {
        for (t in this.tickets) {
            if (t == nuevoTicket || t.id == nuevoTicket.id) {
                return true
            }
        }
        return false
    }

    private fun validarIdTicket(nuevoTicket: Ticket): Boolean {
        return nuevoTicket.id >= 1L
    }

    fun getTicketById(ticketId: Long): Ticket? {
        for (ticket in tickets) {
            if (ticket.id == ticketId) {
                return ticket
            }
        }
        return null
    }

    fun obtenerListaDeTickets(): MutableList<Ticket> {
        return this.tickets
    }

    fun calculateTotalByList(listOfTicketWithTheUserId: MutableList<Ticket>): Double {
        var result = 0.0
        for(ticket in listOfTicketWithTheUserId){
            val subTotalPerTicket = ticket.calculateTicketSubtotal()
            val paymentMethodUsed = PaymentMethodRepository.searchPaymentMethodById(ticket.idMedioDePagoUsado)
            val feeApplied = paymentMethodUsed?.calculateFee(subTotalPerTicket, LocalDateTime.now())
            if(paymentMethodUsed != null){
                result += (subTotalPerTicket + feeApplied!!)
            }
        }
        return result
    }

    fun getListOfTakenTicketsIDs(): MutableList<Long> {
        val listOfTakenIDs = mutableListOf<Long>()
        for(ticket in this.tickets){
            listOfTakenIDs.add(ticket.id)
        }
        return listOfTakenIDs
    }
}