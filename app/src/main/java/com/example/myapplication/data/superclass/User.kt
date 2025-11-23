package com.example.myapplication.data.superclass

data class User(
    val name: String,
    val surname: String,
    val personalID: Long,
    val address: String,
    var phoneNumber: String,
    var nickname: String,
    var password: String,
    var email: String,
    val createdDate: String,
    val clientNumberAssigned: Long
) {
    var money: Double =
        0.0 // dado que el usuario puede comprar y cargar saldo, este valor debe tener la posibilidad de modificarse
    var usuarioBloqueado: Boolean =
        false // este valor por defecto es false, si el usuario acumula 3 intentos fallidos de sesion se bloquea
    var estadoDeSesion: Boolean =
        false // por default un usuario al registrarse en el sistema debe iniciar sesion, su estado inicial siempre es false
    var cantidadIniciosDeSesionFallidos: Int =
        0 // dado que este valor debe poder incrementarse, lo declaramos como var

    fun addFunds(amount: Double): Boolean {
        if (amount in 1000.0..1000000.0) {
            this.money += amount
            return true
        }
        return false
    }

    fun descontarSaldo(calcularTotalPorTicket: Double) {
        this.money -= calcularTotalPorTicket
    }

    fun updateNickname(newNickname: String) {
        this.nickname = newNickname
    }

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun updateEmail(newEmail: String) {
        this.email = newEmail
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        this.phoneNumber = newPhoneNumber
    }
}