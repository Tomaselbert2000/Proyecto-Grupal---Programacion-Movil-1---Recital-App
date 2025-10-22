package herencia

import java.time.DayOfWeek
import java.time.LocalDateTime

class Mastercard(override val id: Long, override val name: String = "Mastercard") : MetodoPago.MetodoDePago(id, name) {
    override fun calcularMontoFinal(montoBase: Double, fechaHoraCompra: LocalDateTime): Double {
        val dia = fechaHoraCompra.dayOfWeek
        val comision = if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) 0.03 else 0.0075
        return montoBase * (1 + comision)
    }
}