package interfaz

import customExceptions.BlankSelectionException
import customExceptions.BlankUserDataException
import customExceptions.InvalidInputDataException
import customExceptions.InvalidSelectionException
import main.kotlin.data.Event
import main.kotlin.data.User
import main.kotlin.repositories.EventRepository
import main.kotlin.repositories.PaymentMethodRepository
import main.kotlin.repositories.TicketCollectionRepository
import main.kotlin.repositories.TicketsRepository
import main.kotlin.repositories.UserRepository
import java.time.LocalDate

fun main(){

    val repoUsuarios = UserRepository
    println("\n")
    println("""
        +=== Sistema de gestion ===+
        |==========================|
        |1. Iniciar sesion         |
        |2. Crear nuevo usuario    |
        |3. Salir del programa     |
        +==========================+
    """.trimIndent())
    var opcionMenuSeleccionada : Int?
    do {
        try {
           println("Ingrese un valor: ")
            opcionMenuSeleccionada = readln().toInt()
            if(opcionMenuSeleccionada !in 1..3){
                println(".=== El valor ingresado no corresponde a una opcion del menu, intente nuevamente ===.")
            }
        }catch (_ : NumberFormatException){
            println(".=== Solo se aceptan valores numericos, intente nuevamente ===.")
            opcionMenuSeleccionada = null
        }
    }while (opcionMenuSeleccionada !in 1..3 || opcionMenuSeleccionada == null)

    when(opcionMenuSeleccionada){
        1 -> {
            iniciarSesion(repoUsuarios)
        }
        2 -> {
            crearNuevoUsuario(repoUsuarios)
        }
        3 -> {
            println(".=== Programa finalizado por el usuario ===.")
        }
    }
}

fun crearNuevoUsuario(repoUsuarios: UserRepository){
    println("""
        .=== Alta de nuevo usuario en el sistema ===.
        =============================================
    """.trimIndent())
    var nombre : String
    var apellido : String
    var nickname : String
    var password : String
    var newUser : User? = null
    var excepcionLanzada = false

    do {
        try {
            println("""
            .=== Ingresar nombre (incluyendo 2do nombre si corresponde) ===.
        """.trimIndent())
            nombre = readln()
            println("""
            .=== Ingresar apellido (incluyendo 2do apellido si corresponde) ===.
        """.trimIndent())
            apellido = readln()
            println("""
            .=== Ingresar nickname de usuario ===.
        """.trimIndent())
            nickname = readln()
            do {
                println("""
            .=== Ingresar contraseña ===.
            =============================
            Requisitos minimos:
            1 letra mayuscula
            1 caracter especial
            1 caracter numerico
            8 caracteres de longitud
            =============================
        """.trimIndent())
                password = readln()
                if(!passwordValida(password)){
                    println(".=== La contraseña ingresada no cumple los requisitos minimos de seguridad. Intente nuevamente ===.")
                }else{
                    newUser = User(1L, nickname, password, nombre, apellido, 0.0, LocalDate.now().toString())
                }
            }while(!passwordValida(password))

            if (nombre.isBlank() || apellido.isBlank() || nickname.isBlank() || password.isBlank()){
                excepcionLanzada = true
                throw BlankUserDataException()
            }
            else if(nombre.any { it.isDigit() } || apellido.any { it.isDigit() } || nombre.any { it.toInt() in 33..38 } || apellido.any { it.toInt() in 33..38 }){
                excepcionLanzada = true
                throw InvalidInputDataException()
            }
        }catch (e: Exception){
            println(e.message)
        }
    }while (newUser == null)

    if(repoUsuarios.registrarNuevoUsuario(newUser) && !excepcionLanzada){
        println("""
            .=== Usuario creado exitosamente ===.
            =====================================
                Volviendo al menu principal...
            =====================================
        """.trimIndent())
        main()
    }else{
        println("""
              .=== Error al crear el usuario. Intente nuevamente ===.
            ===========================================================
            ¿Desea reintentar la operacion? Ingresar S/N para continuar
            ===========================================================
        """.trimIndent())
        var confirmacion : String

        try {
            do {
                confirmacion = readln()
                if (confirmacion != "S" && confirmacion != "N") {
                    println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
                }else if(confirmacion.isBlank()){
                    throw BlankUserDataException()
                }else if (confirmacion.any{it.isDigit()}){
                    throw InvalidSelectionException()
                }
            }while (confirmacion != "S" && confirmacion != "N")
        }catch (e: Exception){
            println(e.message)
        }
    }
}

fun passwordValida(password: String): Boolean {
    var contadorMayusculas = 0
    var contadorEspeciales = 0
    var contadorNumeros = 0

    for(letter in password){
        if(letter.isDigit()){
            contadorNumeros++
        }else if((letter).code in 33..38){
            contadorEspeciales++
        }else if (letter.isUpperCase()){
            contadorMayusculas++
        }
    }

    return contadorMayusculas >= 1 && contadorEspeciales >= 1 && contadorNumeros >= 1 && password.length >= 8
}

fun iniciarSesion(repoUsuarios: UserRepository){
    var nickname: String
    var password: String
    var loggedUser : User? = null

    do {
        try {
            println(".=== Ingresar nickname ===.")
            nickname = readln()
            println(".=== Ingresar contraseña ===.")
            password = readln()

            if (nickname.isBlank() || password.isBlank()){
                throw BlankUserDataException()
            }

            loggedUser = repoUsuarios.login(nickname, password)

            if(loggedUser == null){
                println("""
                            .=== Usuario o contraseña incorrectos ===.
                    ===========================================================
                    ¿Desea reintentar la operacion? Ingresar S/N para continuar
                    ===========================================================
                """.trimIndent())
                try {
                    val confirmacion = readln()
                    do {
                        if (confirmacion.isBlank()){
                            throw BlankUserDataException()
                        }else if(confirmacion.any{it.isDigit()}){
                            throw InvalidSelectionException()
                        }
                    }while(confirmacion != "S" && confirmacion != "N" || confirmacion.isBlank() || confirmacion.any{it.isDigit()})

                    if(confirmacion == "S"){
                        iniciarSesion(repoUsuarios)
                    }else{
                        main()
                    }
                }catch (e : Exception){
                    println(e.message)
                }
            }else{
                return menuPrincipalSistema(loggedUser)
            }
        }catch (e: Exception){
            println(e.message)
        }

    }while (loggedUser == null)
}

fun menuPrincipalSistema(loggedUser: User) {
    val repoUsuarios = UserRepository
    val repoEventos = EventRepository
    val repoTickets = TicketsRepository
    val repoTicketCollection = TicketCollectionRepository
    val repoMediosPago = PaymentMethodRepository

    println("""
            .=== Menu principal ===.
        Bienvenido ${loggedUser.nickname}
        =================================
        1. Ver eventos disponibles.
        2. Comprar tickets.
        3. Cargar saldo.
        4. Ver historial de compras.
        5. Ver saldo actual de usuario.
        6. Cerrar sesion.
        7. Salir del programa.
        =================================
        Ingresar un valor para continuar
        =================================
    """.trimIndent())
    var opcionSeleccionada : Int? = null

    do {
        try {
            opcionSeleccionada = readln().toInt()
            if (opcionSeleccionada !in 1..7){
                println("El valor ingresado no corresponde a una opcion del menu. Intente nuevamente.")
                opcionSeleccionada = null
            }
        }catch (_: NumberFormatException){
            println(".=== El valor ingresado no corresponde a un tipo de dato valido, intente nuevamente ===.")
        }
    }while (opcionSeleccionada == null || opcionSeleccionada !in 1..7)

    when (opcionSeleccionada){
        1 -> {
            mostrarEventos(loggedUser, repoEventos)
        }
        2 -> {
            comprarTickets(loggedUser, repoUsuarios, repoEventos, repoTickets, repoTicketCollection, repoMediosPago)
        }
        3 -> {
            cargarSaldo(loggedUser, repoUsuarios)
        }
        4 ->{
            mostrarHistorialDeComprasDeUsuario(loggedUser, repoTicketCollection, repoTickets, repoEventos, repoMediosPago)
        }
        5 -> {
            verSaldoActualUsuario(loggedUser)
        }
        6 -> {
            cerrarSesion(loggedUser)
        }
        7 -> {
            println(".=== Programa finalizado por el usuario ===.")
        }
    }
}


fun mostrarEventos(loggedUser: User, repoEventos: EventRepository) {
    val listaDeEventos : MutableList<Event> = repoEventos.obtenerListaDeEventos()
    println(".=== Información de eventos ===.")
    for (event in listaDeEventos){
        println("""
            =========================================================================================================================================================================
            Fecha: ${event.date}
            Hora: ${event.time}
            Lugar: ${event.location}
            Artista invitado: ${event.artist}
            URL Imagen: ${event.image}
            =========================================================================================================================================================================
        """.trimIndent())
    }
    println(".=== Presione cualquier tecla para volver al menu anterior ===.")
    readln()
    menuPrincipalSistema(loggedUser)
}

fun comprarTickets(
    loggedUser: User,
    repoUsuarios: UserRepository,
    repoEventos: EventRepository,
    repoTickets: TicketsRepository,
    repoTicketCollection: TicketCollectionRepository,
    repoMediosPago: PaymentMethodRepository
) {
}

fun cargarSaldo(loggedUser: User, repoUsuarios: UserRepository) {
    try {
        println("""
        .=== Carga de saldo de usuario ===.
        ===================================
              Ingresar saldo a cargar:
        ===================================
    """.trimIndent())
        val saldoACargar : Double = readln().toDouble()

        if (saldoACargar.toString().any {it.isLetter()}){
            throw Exception(".=== El campo de saldo no puede contener caracteres no numericos ")
        }
        if(loggedUser.cargarSaldo(saldoACargar)){
            println("""
                .=== Saldo agregado en cuenta de manera exitosa ===.   
                ===================================================
            """.trimIndent())
        }else{
            println("""
                .=== El saldo minimo para realizar un ingreso es $1000 ===.
                ¿Desea reintentar la operacion? Ingresar S/N para continuar
                ===========================================================
            """.trimIndent())
            try {
                val confirmacion = readln()
                do {
                    if(confirmacion.isBlank()){
                        throw BlankSelectionException()
                    }
                    else if(confirmacion.any{it.isDigit()}){
                        throw InvalidSelectionException()
                    }
                }while (confirmacion != "S" && confirmacion != "N")

                if (confirmacion == "S"){
                    cargarSaldo(loggedUser, repoUsuarios)
                }else{
                    menuPrincipalSistema(loggedUser)
                }

            }catch (e: Exception){
                println(e.message)
            }
        }
    }catch (e : Exception){
        println(e.message)
    }
    println(".=== Presione cualquier tecla para volver al menu anterior ===.")
    readln()
    menuPrincipalSistema(loggedUser)
}

fun mostrarHistorialDeComprasDeUsuario(
    loggedUser: User,
    repoTicketCollection: TicketCollectionRepository,
    repoTickets: TicketsRepository,
    repoEventos: EventRepository,
    repoMediosPago: PaymentMethodRepository
){
    val userId = loggedUser.id
    val listaCompras = repoTicketCollection.buscarComprasPorId(userId)

    for(compra in listaCompras){
        val medioDePagoUsadoEnLaCompra = repoMediosPago.obtenerMedioDePagoPorId(compra.paymentId)
        println("""
            .=== Numero de compra: ${compra.id} ===.
            Medio de pago utilizado: ${medioDePagoUsadoEnLaCompra?.name}
        """.trimIndent())
        if(compra.ticketCollection.isNotEmpty()){
            for (ticketId in compra.ticketCollection){
                val ticketParaMostrar = repoTickets.obtenerTicketPorId(ticketId)
                val eventoAsociado = repoEventos.obtenerEventoPorId(ticketParaMostrar?.eventId)
                println("""
                    ==============================================
                    Número de ticket comprado: $ticketId
                    ==============================================
                                    Datos del evento
                    ==============================================
                    Fecha: ${eventoAsociado?.date}
                    Hora: ${eventoAsociado?.time}         
                    Artista: ${eventoAsociado?.artist}
                    Sección del estadio: ${ticketParaMostrar?.section}
                    ==============================================
                    Valor total del ticket: $${ticketParaMostrar?.precio}
                    ==============================================
                """.trimIndent())
                println("\n")
            }
        }else {
            println("""
                .=== No se registran compras de tickets hasta el momento ===.
                =============================================================
                        Presionar Enter para volver al menu anterior
                =============================================================
            """.trimIndent())
            readln()
            menuPrincipalSistema(loggedUser)
        }
    }
    println(".=== Presione cualquier tecla para volver al menu anterior ===.")
    readln()
    menuPrincipalSistema(loggedUser)
}

fun verSaldoActualUsuario(loggedUser: User) {
    println("""
        .=== Saldo actual de usuario: $${loggedUser.money}
        ===========================================
        Presione Enter para volver al menu anterior
        ===========================================
    """.trimIndent())
    readln()
    menuPrincipalSistema(loggedUser)
}

fun cerrarSesion(loggedUser: User) {
    loggedUser.estadoDeSesion = false
    println(".=== Sesion finalizada por el usuario ===.")
    main()
}