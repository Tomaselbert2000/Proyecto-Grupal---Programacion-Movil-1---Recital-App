package herencia

import java.time.LocalDateTime

abstract class MetodoPago {
    abstract class MetodoDePago (
        open val id: Long,
        open val name: String
    ) {
        abstract fun calcularMontoFinal(montoBase: Double, fechaHoraCompra: LocalDateTime = LocalDateTime.now()): Double
    }
}