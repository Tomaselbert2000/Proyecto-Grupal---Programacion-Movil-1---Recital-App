package data.superclass

import java.time.LocalDateTime

abstract class PaymentMethod {
    abstract class MetodoDePago (
        open val id: Long,
        open val name: String,
        open var fee : Double = 0.0
    ) {
        abstract fun calcularMontoComision(montoBase: Double, fechaHoraCompra: LocalDateTime = LocalDateTime.now()): Double
    }
}