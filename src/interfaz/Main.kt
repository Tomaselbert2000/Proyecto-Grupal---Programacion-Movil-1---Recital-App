package interfaz

import customExceptions.BlankUserDataException
import main.kotlin.data.Event
import main.kotlin.data.User
import main.kotlin.repositories.EventRepository
import main.kotlin.repositories.PaymentMethodRepository
import main.kotlin.repositories.TicketCollectionRepository
import main.kotlin.repositories.TicketsRepository
import main.kotlin.repositories.UserRepository

fun main(){

    val repoUsuarios = UserRepository
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
        println("""
            .=== Ingresar contraseña (se recomienda 8+ caracteres) ===.
        """.trimIndent())
        password = readln()

        if (nombre.isBlank() || apellido.isBlank() || nickname.isBlank() || password.isBlank()){
            throw BlankUserDataException()
        }
        if(nombre.any { it.isDigit() } || apellido.any { it.isDigit() }){
            throw Exception(".=== Los campos de nombre o apellido no pueden contener numeros o caracteres especiales. Intente nuevamente ===.")
        }
        val newUser = User(1L, nickname, password, nombre, apellido, 0.0, "2025-10-17")
        if(repoUsuarios.registrarNuevoUsuario(newUser)){
            println(".=== Usuario creado exitosamente. Ingresar con credenciales para acceder al sistema ===.")
            iniciarSesion(repoUsuarios)
        }
    }catch (e: Exception){
        println(e.message)
        crearNuevoUsuario(repoUsuarios)
    }
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

            loggedUser = repoUsuarios.login(nickname, password)

            if (nickname.isBlank() || password.isBlank()){
                throw BlankUserDataException()
            }

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
                            throw Exception(".=== El campo de confirmacion no puede quedar en blanco ===.")
                        }else if(confirmacion.any{it.isDigit()}){
                            throw Exception(".=== El campo de confirmacion no puede ser un numero ===.")
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
                        throw Exception(".=== El campo de confirmacion no puede quedar en blanco ===.")
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
                """.trimIndent())
            }
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