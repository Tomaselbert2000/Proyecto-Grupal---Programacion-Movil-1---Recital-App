package herencia

import java.time.LocalDateTime

class Visa(override val id: Long, override val name: String = "Visa") : MetodoPago.MetodoDePago(id, name) {
    override fun calcularMontoFinal(montoBase: Double, fechaHoraCompra: LocalDateTime): Double {
        val hora = fechaHoraCompra.toLocalTime()
        val inicio = java.time.LocalTime.of(15, 0)
        val fin = java.time.LocalTime.of(22, 30)
        val comision = if (hora.isAfter(inicio) && hora.isBefore(fin)) 0.01 else 0.03
        return montoBase * (1 + comision)
    }
}