package data.superclass

data class User(
    val id: Long,
    var nickname: String,
    var password: String,
    val name: String,
    val surname: String,
    var money: Double, // dado que el usuario puede comprar y cargar saldo, este valor debe tener la posibilidad de modificarse
    val createdDate: String
) {
    var usuarioBloqueado: Boolean =
        false // este valor por defecto es false, si el usuario acumula 3 intentos fallidos de sesion se bloquea
    var estadoDeSesion: Boolean =
        false // por default un usuario al registrarse en el sistema debe iniciar sesion, su estado inicial siempre es false
    var cantidadIniciosDeSesionFallidos: Int =
        0 // dado que este valor debe poder incrementarse, lo declaramos como var

    fun obtenerCantidadIniciosSesionFallidos(): Int {
        return this.cantidadIniciosDeSesionFallidos
    }

    fun obtenerEstadoDeSesion(): Boolean {
        return this.estadoDeSesion
    }

    fun obtenerEstadoDeBloqueoDeUsuario(): Boolean {
        return this.usuarioBloqueado
    }

    fun obtenerSaldo(): Double {
        return this.money
    }

    fun cargarSaldo(saldoACargar: Double): Boolean {
        if (saldoACargar in 1000.0..1000000.0) {
            this.money += saldoACargar
            return true
        }
        return false
    }

    fun descontarSaldo(calcularTotalPorTicket: Double) {
        this.money -= calcularTotalPorTicket
    }

    fun actualizarNickname(nuevoNickname: String) {
        this.nickname = nuevoNickname
    }

    fun actualizarPassword(newPassword: String) {
        this.password = newPassword
    }
}