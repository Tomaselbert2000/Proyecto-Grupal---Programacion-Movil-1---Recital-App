package herencia

import java.time.LocalDateTime

class MercadoPago(override val id: Long, override val name: String = "Mercado Pago") : MetodoPago.MetodoDePago(id, name) {
    override fun calcularMontoFinal(montoBase: Double, fechaHoraCompra: LocalDateTime): Double {
        val comision = 0.02
        return montoBase * (1 + comision)
    }
}