package main.kotlin.repositories

import main.kotlin.data.Ticket

object TicketsRepository {

    private val tickets = mutableListOf<Ticket>()

    init {
        tickets.add(
            Ticket(
                1L,
                4L,
                1,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                2L,
                3L,
                4,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                3L,
                1L,
                2,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                4L,
                5L,
                6,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                5L,
                2L,
                3,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                6L,
                1L,
                7,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                7L,
                3L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                8L,
                1L,
                1,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                9L,
                7L,
                2,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                10L,
                7L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                11L,
                5L,
                7,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                12L,
                2L,
                2,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                13L,
                4L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                14L,
                6L,
                2,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                15L,
                2L,
                7,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                16L,
                5L,
                4,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                17L,
                3L,
                1,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                18L,
                1L,
                3,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                19L,
                3L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                20L,
                1L,
                5,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                21L,
                5L,
                6,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                22L,
                4L,
                2,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                23L,
                6L,
                1,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                24L,
                3L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                25L,
                2L,
                3,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                26L,
                7L,
                8,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                27L,
                7L,
                1,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                28L,
                1L,
                4,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                29L,
                2L,
                2,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                30L,
                5L,
                3,
                "Platea"
            )
        )
    }

    fun limpiarInstancia() {
        tickets.clear()
    }

    fun registrarNuevoTicket(nuevoTicket: Ticket, listaDeEventosRegistrados: MutableList<Long>) : Boolean{
        return this.validarIdTicket(nuevoTicket)
                && this.validarEventoAsociado(nuevoTicket, listaDeEventosRegistrados)
                && this.validarUbicacion(nuevoTicket)
                && this.validarCantidad(nuevoTicket)
                && !this.esDuplicado(nuevoTicket)
                && tickets.add(nuevoTicket)
    }

    fun obtenerListaDeIDsDeTickets(): MutableList<Long> {
        val listaDeIDsDeTicketsRegistrados = mutableListOf<Long>()
        for (ticket in this.tickets){
            listaDeIDsDeTicketsRegistrados.add(ticket.id)
        }
        return listaDeIDsDeTicketsRegistrados
    }

    private fun validarEventoAsociado(nuevoTicket: Ticket, listaDeIDsEventosRegistrados: MutableList<Long>): Boolean {
        for (id in listaDeIDsEventosRegistrados){
            if (nuevoTicket.eventId == id){
                return true
            }
        }
        return false
    }

    private fun validarCantidad(nuevoTicket: Ticket): Boolean {
        return nuevoTicket.quantity >= 1
    }

    private fun validarUbicacion(nuevoTicket: Ticket): Boolean {
        return nuevoTicket.section == "Platea" || nuevoTicket.section == "Campo"
    }

    private fun esDuplicado(nuevoTicket: Ticket): Boolean {
        for (t in this.tickets){
            if (t == nuevoTicket || t.id == nuevoTicket.id){
                return true
            }
        }
        return false
    }

    private fun validarIdTicket(nuevoTicket: Ticket) : Boolean{
        return nuevoTicket.id >= 1L
    }

    fun reiniciarInstancia() {
        tickets.add(
            Ticket(
                1L,
                4L,
                1,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                2L,
                3L,
                4,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                3L,
                1L,
                2,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                4L,
                5L,
                6,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                5L,
                2L,
                3,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                6L,
                1L,
                7,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                7L,
                3L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                8L,
                1L,
                1,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                9L,
                7L,
                2,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                10L,
                7L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                11L,
                5L,
                7,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                12L,
                2L,
                2,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                13L,
                4L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                14L,
                6L,
                2,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                15L,
                2L,
                7,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                16L,
                5L,
                4,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                17L,
                3L,
                1,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                18L,
                1L,
                3,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                19L,
                3L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                20L,
                1L,
                5,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                21L,
                5L,
                6,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                22L,
                4L,
                2,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                23L,
                6L,
                1,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                24L,
                3L,
                4,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                25L,
                2L,
                3,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                26L,
                7L,
                8,
                "Platea"
            )
        )

        tickets.add(
            Ticket(
                27L,
                7L,
                1,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                28L,
                1L,
                4,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                29L,
                2L,
                2,
                "Campo"
            )
        )

        tickets.add(
            Ticket(
                30L,
                5L,
                3,
                "Platea"
            )
        )
    }

    fun obtenerTicketPorId(ticketId: Long): Ticket? {
        for (ticket in tickets) {
            if (ticket.id == ticketId) {
                return ticket
            }
        }
        return null
    }
}