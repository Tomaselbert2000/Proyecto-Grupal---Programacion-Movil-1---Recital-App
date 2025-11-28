package com.example.myapplication.repositories

import com.example.myapplication.data.subclass.Mastercard
import com.example.myapplication.data.subclass.MercadoPago
import com.example.myapplication.data.superclass.PaymentMethod
import com.example.myapplication.data.subclass.Visa

object PaymentMethodRepository {

    val registeredPaymentMethods = mutableListOf<PaymentMethod.MetodoDePago>()

    init {
        registeredPaymentMethods.add(MercadoPago(1L))
        registeredPaymentMethods.add(Visa(2L))
        registeredPaymentMethods.add(Mastercard(3L))
    }

    fun searchPaymentMethodById(id: Long): PaymentMethod.MetodoDePago? {
        for (paymentMethod in registeredPaymentMethods) {
            if (paymentMethod.id == id) {
                return paymentMethod
            }
        }
        return null
    }

    fun searchPaymentMethodByName(name: String): PaymentMethod.MetodoDePago? {
        for(pm in this.registeredPaymentMethods){
            if(pm.name == name){
                return pm
            }
        }
        return null
    }

}

