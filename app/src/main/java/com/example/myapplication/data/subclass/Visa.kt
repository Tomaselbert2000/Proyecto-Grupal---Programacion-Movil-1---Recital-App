package com.example.myapplication.data.subclass

import com.example.myapplication.data.superclass.PaymentMethod
import java.time.LocalDateTime
import java.time.LocalTime

class Visa(override val id: Long, override val name: String = "Visa", override var fee: Double = 0.01) :
    PaymentMethod.MetodoDePago(id, name) {
    override fun calculateFee(montoBase: Double, fechaHoraCompra: LocalDateTime): Double {
        val hora = fechaHoraCompra.toLocalTime()
        val inicio = LocalTime.of(15, 0)
        val fin = LocalTime.of(22, 30)
        fee = if (hora.isAfter(inicio) && hora.isBefore(fin)) 0.01 else 0.03
        return montoBase * fee
    }
}