package com.example.myapplication.data.subclass

import com.example.myapplication.data.superclass.PaymentMethod
import java.time.LocalDateTime

class MercadoPago(override val id: Long, override val name: String = "Mercado Pago", override var fee: Double = 0.02) :
    PaymentMethod.MetodoDePago(
        id, name,
        fee
    ) {
    override fun calculateFee(montoBase: Double, fechaHoraCompra: LocalDateTime): Double {
        return montoBase * fee
    }
}