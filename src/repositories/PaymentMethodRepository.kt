package main.kotlin.repositories

import main.kotlin.data.PaymentMethod
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

object PaymentMethodRepository {

    private val listaMediosDePago = mutableListOf<PaymentMethod>()

    init {
        listaMediosDePago.add(PaymentMethod(1L, "Mercado Pago", 0.02))
        listaMediosDePago.add(PaymentMethod(2L, "Visa", 0.01))
        listaMediosDePago.add(PaymentMethod(3L, "MasterCard", 0.03))
    }

    private fun obtenerFee(pm: PaymentMethod, date: LocalDate, time: LocalTime): Double {
        return when (pm.name) {
            "Mercado Pago" -> 0.02
            "Visa" -> {
                if (time in LocalTime.of(15, 0)..LocalTime.of(22, 30)) {
                    0.01
                } else {
                    0.03
                }
            }
            "MasterCard" -> {
                if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) {
                    0.03
                } else {
                    0.0075
                }
            }
            else -> 0.0
        }
    }

    fun registrarNuevoMedioDePago(nuevoMedioDePago: PaymentMethod): Boolean {
        val precondiciones = !this.estaDuplicado(nuevoMedioDePago) // aca juntamos dentro de una sola variable todas las condiciones para crear un nuevo medio de pago
                && !this.idRepetido(nuevoMedioDePago)
                && !this.nombreRepetido(nuevoMedioDePago)
                && this.idValido(nuevoMedioDePago)
                && this.valorDeComisionValido(nuevoMedioDePago)
        if (precondiciones){ // preguntamos si se cumplen todas, es decir, que toda esa suma logica da true
            this.obtenerFee(nuevoMedioDePago, LocalDate.now(), LocalTime.now()) // le preguntamos a la funcion cual es la comision segun el nombre del nuevo objeto, la fecha y la hora
            this.listaMediosDePago.add(nuevoMedioDePago)
            return true
        }
        return false
    }

    private fun valorDeComisionValido(nuevoMedioDePago: PaymentMethod): Boolean {
        return nuevoMedioDePago.fee >= 0.0
    }

    private fun idValido(nuevoMedioDePago: PaymentMethod): Boolean {
        return nuevoMedioDePago.id >= 1L
    }

    private fun nombreRepetido(nuevoMedioDePago: PaymentMethod): Boolean {
        for(pm in listaMediosDePago) {
            if(pm.name == nuevoMedioDePago.name) {
                return true
            }
        }
        return false
    }

    private fun idRepetido(nuevoMedioDePago: PaymentMethod): Boolean {
        for (pm in listaMediosDePago) {
            if (nuevoMedioDePago.id == pm.id) {
                return true
            }
        }
        return false
    }

    private fun estaDuplicado(nuevoMedioDePago: PaymentMethod): Boolean {
        for (item in this.listaMediosDePago) {
            if(item == nuevoMedioDePago){
                return true
            }
        }
        return false
    }

    fun obtenerMedioDePagoPorId(paymentId: Long): PaymentMethod? {
        for(pm in listaMediosDePago) {
            if(pm.id == paymentId){
                return pm
            }
        }
        return null
    }

    fun reiniciarInstancia() {
        listaMediosDePago.add(PaymentMethod(1L, "Mercado Pago", 0.02))
        listaMediosDePago.add(PaymentMethod(2L, "Visa", 0.01))
        listaMediosDePago.add(PaymentMethod(3L, "MasterCard", 0.03))
    }

    fun limpiarInstancia() {
        listaMediosDePago.clear()
    }

    fun obtenerListaDeIDs(): MutableList<Long> {
        val listaDeIds = mutableListOf<Long>()
        for (item in listaMediosDePago) {
            listaDeIds.add(item.id)
        }
        return listaDeIds
    }
}