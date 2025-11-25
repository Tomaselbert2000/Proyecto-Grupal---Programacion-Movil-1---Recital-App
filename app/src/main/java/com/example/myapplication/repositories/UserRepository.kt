package com.example.myapplication.repositories

import com.example.myapplication.data.superclass.User

object UserRepository {

    private val users = mutableListOf<User>()
    var currentUserLogged : User ? = null

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
                "2024-06-12",
                19258L
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
                "2021-01-20",
                87256L
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
                9999L
            )
        )

        users.add(
            User(
                "Tomas",
                "Elbert",
                1000L,
                "Calle falsa 123",
                "11335577",
                "tomas",
                "abc1234",
                "tomas@gmail",
                "2025-11-19",
                14380L
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
                    currentUserLogged = usr
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
        for (usr in users) {
            if (usr.phoneNumber == phoneNumberToValidate) {
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
            if (u.personalID == newUserId) {
                return true
            }
        }
        return false
    }

    fun searchUserByIdAndAddFunds(userIdToSearch: Long, amountToAdd: Double) {
        for (usr in users) {
            if (usr.personalID == userIdToSearch) {
                usr.addFunds(amountToAdd)
            }
        }
    }

    fun getUserById(userIdToSearch: Long?): User? {
        for (u in users) {
            if (u.personalID == userIdToSearch) {
                return u
            }
        }
        return null
    }

    fun addFundsToUser(userId: Long, amountToAddAsDouble: Double) {
        for (usr in users) {
            if (usr.personalID == userId) {
                usr.addFunds(amountToAddAsDouble)
            }
        }
    }

    fun getListOfTakenClientNumbers(): MutableList<Long> {
        val takenClientNumbers = mutableListOf<Long>()
        for (user in users) {
            takenClientNumbers.add(user.clientNumberAssigned)
        }
        return takenClientNumbers
    }

    fun discountPurchase(userId: Long, purchaseTotalAsDouble: Double) {
        for(usr in users){
            if(usr.personalID == userId){
                usr.descontarSaldo(purchaseTotalAsDouble)
            }
        }
    }
}
