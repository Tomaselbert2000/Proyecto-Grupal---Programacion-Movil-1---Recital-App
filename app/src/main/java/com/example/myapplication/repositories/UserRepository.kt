package repositories

import data.superclass.User

object UserRepository {

    private val users = mutableListOf<User>()

    init {
        users.add(User(1504L, "MARTIN_ALBANESI", "abc4321", "Martin", "Albanesi", 350000.0, "2024-05-13"))
        users.add(User(2802L, "Fran25", "contraseña123", "Franco German", "Mazafra", 200000.0, "2021-01-20"))
        users.add(User(1510L, "jonaURAN", "@12345", "Jonatan", "Uran", 125000.0, "2018-04-15"))
    }

    fun login(nickname: String?, password: String?): User? {
        val usuario = this.users.find { it.nickname == nickname }
        if (usuario != null) {
            if (usuario.password == password) {
                usuario.estadoDeSesion = true
                return usuario
            } else {
                usuario.cantidadIniciosDeSesionFallidos++
                if (usuario.cantidadIniciosDeSesionFallidos >= 3) {
                    usuario.usuarioBloqueado = true
                }
            }
        }
        return null
    }

    fun registrarNuevoUsuario(usuarioNuevo: User): Boolean {
        return this.validarId(usuarioNuevo) &&
                !this.estaDuplicado(usuarioNuevo) &&
                this.saldoValido(usuarioNuevo) &&
                users.add(usuarioNuevo)
    }

    private fun validarId(usuarioNuevo: User): Boolean {
        return usuarioNuevo.id >= 1L
    }

    private fun saldoValido(usuarioNuevo: User): Boolean {
        return usuarioNuevo.money >= 0.0
    }

    private fun estaDuplicado(usuario: User): Boolean {
        for (u in users) {
            if (u == usuario || u.id == usuario.id || u.nickname == usuario.nickname) {
                return true
            }
        }
        return false
    }

    fun obtenerRegistrosPorFechaDeAlta(fechaDeAlta: String): MutableList<User> {
        val listaDeUsuarios = mutableListOf<User>()
        for (user in users) {
            if (user.createdDate == fechaDeAlta) {
                listaDeUsuarios.add(user)
            }
        }
        return listaDeUsuarios
    }

    fun buscarUsuarioPorID(idBuscado: Long): User? {
        for (u in users) {
            if (u.id == idBuscado) {
                return u
            }
        }
        return null
    }

    fun buscarUsuarioPorNickname(nicknameParaBuscar: String): User? {
        for (user in users) {
            if (user.nickname == nicknameParaBuscar) {
                return user
            }
        }
        return null
    }

    fun obtenerListaUsuariosFiltradosPorSaldo(saldoMinimo: Double, saldoMaximo: Double): MutableList<User> {
        val listaDeUsuariosFiltradosPorSaldo = mutableListOf<User>()
        for (user in users) {
            if (user.money in saldoMinimo..saldoMaximo) {
                listaDeUsuariosFiltradosPorSaldo.add(user)
            }
        }
        return listaDeUsuariosFiltradosPorSaldo
    }

    fun obtenerListaDeIDsDeUsuarios(): MutableList<Long> {
        val listaDeIDsRegistrados = mutableListOf<Long>()
        for (user in this.users) {
            listaDeIDsRegistrados.add(user.id)
        }
        return listaDeIDsRegistrados
    }

    // dado que el repositorio de usuarios es de tipo Object, no podemos tener mas que una sola instancia
    // lo cual lleva a posibles errores o inconsistencias al momento de correr sus respectivos tests
    // con este metodo reiniciamos la instancia desde dentro para poder testear correctamente

    fun limpiarInstancia() {
        users.clear()
        /*users.add(User(1504L, "MARTIN_ALBANESI", "abc4321", "Martin", "Albanesi", 350000.0, "2024-05-13"))
        users.add(User(2802L, "Fran25", "contraseña123", "Franco German", "Mazafra", 200000.0, "2021-01-20"))
        users.add(User(1510L, "jonaURAN", "@12345", "Jonatan", "Uran", 125000.0, "2018-04-15"))*/
    }

    fun obtenerSaldoDeUsuario(userIdQueBuscamos: Long): Double {
        for (usuario in users) {
            if (usuario.id == userIdQueBuscamos) {
                return usuario.obtenerSaldo()
            }
        }
        return 0.0
    }

    fun reiniciarInstancia() {
        users.add(User(1504L, "MARTIN_ALBANESI", "abc4321", "Martin", "Albanesi", 350000.0, "2024-05-13"))
        users.add(User(2802L, "Fran25", "contraseña123", "Franco German", "Mazafra", 200000.0, "2021-01-20"))
        users.add(User(1510L, "jonaURAN", "@12345", "Jonatan", "Uran", 125000.0, "2018-04-15"))
    }

    fun obtenerListaDeNicknames(): MutableList<String> {
        val listaNicknamesNoDisponibles = mutableListOf<String>()
        for (usr in this.users) {
            listaNicknamesNoDisponibles.add(usr.nickname)
        }
        return listaNicknamesNoDisponibles
    }

}
