package repositories

import herencia.Mastercard
import herencia.MercadoPago
import herencia.MetodoPago
import herencia.Visa

object PaymentMethodRepository {

    val listaMetodosDePago = mutableListOf<MetodoPago.MetodoDePago>()

    init {
        listaMetodosDePago.add(MercadoPago(1L))
        listaMetodosDePago.add(Visa(2L))
        listaMetodosDePago.add(Mastercard(3L))
    }

    fun buscarMetodoDePagoPorId(id : Long) : MetodoPago.MetodoDePago? {
        return listaMetodosDePago.find { it.id == id }
    }

    fun registrarMetodoDePago(metodo : MetodoPago.MetodoDePago): Boolean {
        return this.idDuplicado(metodo) && !this.nombreDuplicado(metodo) && this.idValido(metodo) && this.listaMetodosDePago.add(metodo)
    }
}

fun PaymentMethodRepository.idValido(metodo: MetodoPago.MetodoDePago): Boolean {
    return metodo.id > 0
}

fun PaymentMethodRepository.nombreDuplicado(nuevoMetodo: MetodoPago.MetodoDePago): Boolean {
    for(metodo in this.listaMetodosDePago){
        if(metodo.name == nuevoMetodo.name){
            return true
        }
    }
    return false
}

fun PaymentMethodRepository.idDuplicado(nuevoMetodo: MetodoPago.MetodoDePago): Boolean {
    for(metodo in this.listaMetodosDePago){
        if(metodo.id == nuevoMetodo.id){
            return true
        }
    }
    return  false
}
