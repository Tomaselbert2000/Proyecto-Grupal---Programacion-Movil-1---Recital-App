package com.example.myapplication.data.subclass

import com.example.myapplication.data.superclass.PaymentMethod
import java.time.DayOfWeek
import java.time.LocalDateTime

class Mastercard(override val id: Long, override val name: String = "Mastercard", override var fee: Double = 0.03) :
    PaymentMethod.MetodoDePago(
        id, name,
        fee
    ) {
    override fun calculateFee(montoBase: Double, fechaHoraCompra: LocalDateTime): Double {
        val dia = fechaHoraCompra.dayOfWeek
        fee = if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) 0.03 else 0.0075
        return montoBase * fee
    }
}