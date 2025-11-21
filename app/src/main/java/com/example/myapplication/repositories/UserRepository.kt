package com.example.myapplication.repositories

import com.example.myapplication.data.superclass.User

object UserRepository {

    private val users = mutableListOf<User>()

    init {
        users.add(
            User(
                "Martin",
                "Albanesi",
                1504L,
                "San Justo 123",
                "11223344",
                "MARTIN_ALBANESI",
                "abc4321",
                "martin@gmail",
                "2024-06-12"
            )
        )
        users.add(
            User(
                "Franco German",
                "Mazafra",
                2802L,
                "Varela 456",
                "99887766",
                "Fran25",
                "contraseña123",
                "fran@gmail.com",
                "2021-01-20"
            )
        )
        users.add(
            User(
                "Jonatan",
                "Uran",
                1510L,
                "Otero 999",
                "1234567",
                "jonaURAN",
                "@12345",
                "jona@gmail.com",
                "2018-04-15)",
            )
        )
        this.searchUserByIdAndAddFunds(1504L, 350000.0)
        this.searchUserByIdAndAddFunds(2802L, 234500.0)
        this.searchUserByIdAndAddFunds(1510L, 57800.0)
    }

    fun login(emailOrNicknameInputString: String?, password: String?): User? {
        for (usr in users) {
            if (usr.email == emailOrNicknameInputString || usr.nickname == emailOrNicknameInputString) {
                if (usr.password == password) {
                    usr.estadoDeSesion = true
                    return usr
                } else {
                    usr.cantidadIniciosDeSesionFallidos++
                    if (usr.cantidadIniciosDeSesionFallidos >= 3) {
                        usr.usuarioBloqueado = true
                    }
                }
            }
        }
        return null
    }

    fun registerNewUser(newUser: User): Boolean {
        return users.add(newUser)
    }

    fun userEmailAddressIsNotTaken(email: String): Boolean {
        for (usr in users) {
            if (usr.email == email) {
                return false
            }
        }
        return true
    }

    fun userNicknameIsNotTaken(nicknameToValidate: String): Boolean {
        for (usr in users) {
            if (usr.nickname == nicknameToValidate) {
                return false
            }
        }
        return true
    }

    fun phoneNumberIsNotTaken(phoneNumberToValidate: String): Boolean {
        for(usr in users){
            if(usr.phoneNumber == phoneNumberToValidate){
                return false
            }
        }
        return true
    }

    fun userIdGreaterThanZero(newUserId: Long): Boolean {
        return newUserId >= 1L
    }

    fun userIdIsDuplicated(newUserId: Long): Boolean {
        for (u in users) {
            if (u.id == newUserId) {
                return true
            }
        }
        return false
    }

    fun searchUserByIdAndAddFunds(userIdToSearch:Long, amountToAdd : Double){
        for(usr in users){
            if(usr.id == userIdToSearch){
                usr.addFunds(amountToAdd)
            }
        }
    }

    fun filterUsersByCreationDate(creationDate: String): MutableList<User> {
        val usersList = mutableListOf<User>()
        for (user in users) {
            if (user.createdDate == creationDate) {
                usersList.add(user)
            }
        }
        return usersList
    }

    fun getUserById(userIdToSearch: Long?): User? {
        for (u in users) {
            if (u.id == userIdToSearch) {
                return u
            }
        }
        return null
    }

    fun getUserByNickname(nicknameToSearch: String): User? {
        for (user in users) {
            if (user.nickname == nicknameToSearch) {
                return user
            }
        }
        return null
    }

    fun filterUsersByCurrentFundsRange(
        minimumFundsLimit: Double,
        maxFundsLimit: Double
    ): MutableList<User> {
        val usersFilteredByFunds = mutableListOf<User>()
        for (user in users) {
            if (user.money in minimumFundsLimit..maxFundsLimit) {
                usersFilteredByFunds.add(user)
            }
        }
        return usersFilteredByFunds
    }

    fun getListOfAllRegisteredUserIDs(): MutableList<Long> {
        val listOfIdsAsLong = mutableListOf<Long>()
        for (user in this.users) {
            listOfIdsAsLong.add(user.id)
        }
        return listOfIdsAsLong
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

    fun getCurrentFundsOfUserById(userIdToSearch: Long): Double {
        for (user in users) {
            if (user.id == userIdToSearch) {
                return user.obtenerSaldo()
            }
        }
        return 0.0
    }

    /*fun reiniciarInstancia() {
        users.add(
            User(
                1504L,
                "MARTIN_ALBANESI",
                "abc4321",
                "Martin",
                "Albanesi",
                350000.0,
                "2024-05-13"
            )
        )
        users.add(
            User(
                2802L,
                "Fran25",
                "contraseña123",
                "Franco German",
                "Mazafra",
                200000.0,
                "2021-01-20"
            )
        )
        users.add(User(1510L, "jonaURAN", "@12345", "Jonatan", "Uran", 125000.0, "2018-04-15"))
    }*/

    fun obtenerListaDeNicknames(): MutableList<String> {
        val listaNicknamesNoDisponibles = mutableListOf<String>()
        for (usr in this.users) {
            listaNicknamesNoDisponibles.add(usr.nickname)
        }
        return listaNicknamesNoDisponibles
    }

}
