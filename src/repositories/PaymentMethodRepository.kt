package repositories

import data.subclass.Mastercard
import data.subclass.MercadoPago
import data.superclass.PaymentMethod
import data.subclass.Visa

object PaymentMethodRepository {

    val listaMetodosDePago = mutableListOf<PaymentMethod.MetodoDePago>()

    init {
        listaMetodosDePago.add(MercadoPago(1L))
        listaMetodosDePago.add(Visa(2L))
        listaMetodosDePago.add(Mastercard(3L))
    }

    fun buscarMetodoDePagoPorId(id : Long) : PaymentMethod.MetodoDePago? {
        return listaMetodosDePago.find { it.id == id }
    }

    fun registrarMetodoDePago(metodo : PaymentMethod.MetodoDePago): Boolean {
        return this.idDuplicado(metodo) && !this.nombreDuplicado(metodo) && this.idValido(metodo) && this.listaMetodosDePago.add(metodo)
    }
}

fun PaymentMethodRepository.idValido(metodo: PaymentMethod.MetodoDePago): Boolean {
    return metodo.id > 0
}

fun PaymentMethodRepository.nombreDuplicado(nuevoMetodo: PaymentMethod.MetodoDePago): Boolean {
    for(metodo in this.listaMetodosDePago){
        if(metodo.name == nuevoMetodo.name){
            return true
        }
    }
    return false
}

fun PaymentMethodRepository.idDuplicado(nuevoMetodo: PaymentMethod.MetodoDePago): Boolean {
    for(metodo in this.listaMetodosDePago){
        if(metodo.id == nuevoMetodo.id){
            return true
        }
    }
    return  false
}
