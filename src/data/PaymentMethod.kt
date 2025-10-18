package main.kotlin.data

data class PaymentMethod (
    val id : Long,
    val name : String,
    // dentro del constructor del metodo de pago podemos especificar el valor que queremos como comision
    var fee : Double
)