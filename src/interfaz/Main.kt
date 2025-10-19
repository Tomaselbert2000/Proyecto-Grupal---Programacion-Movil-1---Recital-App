package interfaz

import customExceptions.BlankUserDataException
import customExceptions.InvalidInputDataException
import customExceptions.InvalidSelectionException
import data.Event
import data.User
import main.kotlin.repositories.*
import repositories.EventRepository
import repositories.TicketsRepository
import java.time.LocalDate
import kotlin.system.exitProcess

fun main(){

    val repoUsuarios = UserRepository
    println("\n")
    println("""
        .=== Sistema de gestion ===.
        +==========================+
        | 1. Iniciar sesion        |
        | 2. Crear nuevo usuario   |
        | 3. Salir del programa    |
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
            exitProcess(0)
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
            println(".=== Ingresar nombre (incluyendo 2do nombre si corresponde) ===.")
            nombre = readln()

            println(".=== Ingresar apellido (incluyendo 2do apellido si corresponde) ===.")
            apellido = readln()

            println(".=== Ingresar nickname de usuario ===.")
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
            else if(nombre.any { it.isDigit() } || apellido.any { it.isDigit() } || nombre.any { it.code in 33..38 } || apellido.any { it.code in 33..38 }){
                excepcionLanzada = true
                throw InvalidInputDataException()
            }
        }catch (e: Exception){
            println(e.message)
        }
    }while (newUser == null)

    if(!excepcionLanzada){
        repoUsuarios.registrarNuevoUsuario(newUser)
        println("""
            .=== Usuario creado exitosamente ===.
            =====================================
                Volviendo al menu principal...
            =====================================
        """.trimIndent())
        main()
    }else{
        println("""
                        .=== Error al crear el usuario ===.
            ===========================================================
            ¿Desea reintentar la operacion? Ingresar S/N para continuar
            ===========================================================
        """.trimIndent())

        val reiniciarOperacion = solicitarConfirmacionDeUsuario()

        if(reiniciarOperacion){
            crearNuevoUsuario(repoUsuarios)
        }else{
            main()
        }
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

                val reiniciarOperacion = solicitarConfirmacionDeUsuario()

                if (reiniciarOperacion){
                    iniciarSesion(repoUsuarios)
                }else{
                    main()
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
        ===================================
        1. Ver eventos disponibles.
        2. Comprar tickets.
        3. Cargar saldo.
        4. Ver historial de compras.
        5. Ver saldo actual de usuario.
        6. Ver metodos de pago disponibles
        7. Cerrar sesion.
        8. Salir del programa.
        ==================================
         Ingresar un valor para continuar
        ==================================
    """.trimIndent())
    var opcionSeleccionada : Int? = null

    do {
        try {
            opcionSeleccionada = readln().toInt()
            if (opcionSeleccionada !in 1..8){
                println("El valor ingresado no corresponde a una opcion del menu. Intente nuevamente.")
                opcionSeleccionada = null
            }
        }catch (_: NumberFormatException){
            println(".=== El valor ingresado no corresponde a un tipo de dato valido, intente nuevamente ===.")
        }
    }while (opcionSeleccionada == null || opcionSeleccionada !in 1..8)

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
        6 ->{
            mostrarMetodosDePagoDisponibles(loggedUser, repoMediosPago)
        }
        7 -> {
            cerrarSesion(loggedUser)
        }
        8 -> {
            println(".=== Programa finalizado por el usuario ===.")
            exitProcess(0)
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
    println("""
        ===========================================
        Presione Enter para volver al menu anterior
        ===========================================
    """.trimIndent())
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
    println("""
        .=== Pestaña de compra de tickets ===.
        ======================================
        Saldo actual de usuario: $${loggedUser.money}
        ======================================
    """.trimIndent())
    println("""
        .=== Seleccione una opcion para iniciar la compra ===.
        1. Ver lista de eventos programados.
        2. Ver lista de artistas.
        ======================================================
    """.trimIndent())
    try {
        var opcionSeleccionada : Int
        do {
            opcionSeleccionada = readln().toInt()
            if (opcionSeleccionada.toString().isBlank()){
                throw Exception(".=== El campo de seleccion no puede quedar en blanco. Intente nuevamente ===.")
            }else if(opcionSeleccionada.toString().any{it.isLetter()} || opcionSeleccionada.toString().any{it.code in 33..38}){
                throw Exception(".=== El campo de seleccion no puede contener letras o caracteres especiales. Intente nuevamente ===.")
            }else if(opcionSeleccionada !in 1..2){
                println(".=== El valor ingresado no corresponde a una opcion valida. Intente nuevamente ===.")
            }
        }while (opcionSeleccionada !in 1..2 || opcionSeleccionada.toString().isBlank() || opcionSeleccionada.toString().any{it.isLetter()} || opcionSeleccionada.toString().any{it.code in 33..38})

        if(opcionSeleccionada == 1){
            mostrarEventos(loggedUser, repoEventos)
        }else{
            println("""
                .=== Lista de artistas con eventos programados ===.
                Ingrese el valor asociado al artista para continuar
                ===================================================
            """.trimIndent())
            var index = 1
            for(evento in repoEventos.obtenerListaDeEventos()){
                println("${index}. ${evento.artist}")
                index++
            }
            println("===================================================")

            try {
                var artistaSeleccionado: Int
                do{
                    artistaSeleccionado = readln().toInt()

                }while (artistaSeleccionado !in 1..index)
            }catch (e : Exception){
                println(e.message)
            }
        }
    }catch (e:Exception){
        println(e.message)
    }
    TODO()
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
            val reiniciarOperacion = solicitarConfirmacionDeUsuario()

            if (reiniciarOperacion){
                cargarSaldo(loggedUser, repoUsuarios)
            }else{
                menuPrincipalSistema(loggedUser)
            }
        }
    }catch (e : Exception){
        println(e.message)
    }
    println("""
        ===========================================
        Presione Enter para volver al menu anterior
        ===========================================
    """.trimIndent())
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
    println(".=== Presione Enter para volver al menu anterior ===.")
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

fun mostrarMetodosDePagoDisponibles(loggedUser: User, repoMediosPago: PaymentMethodRepository) {
    val listaDeIDsMediosDePago = repoMediosPago.obtenerListaDeIDs()
    println("""
        .=== Medios de pago disponibles actualmente ===.
        ================================================
    """.trimIndent())
    for(id in listaDeIDsMediosDePago){
        val medioDePago = repoMediosPago.obtenerMedioDePagoPorId(id)
        println("""
            Metodo de pago: ${medioDePago?.name}
            Comision aplicada: ${medioDePago?.fee?.times(100)}%
            ================================================
        """.trimIndent())
    }
    println("""
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

fun solicitarConfirmacionDeUsuario(): Boolean {
    var confirmacion : String
    try {
        do {
            confirmacion = readln().uppercase()
            if (confirmacion != "S" && confirmacion != "N") {
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            }else if(confirmacion.isBlank()){
                throw BlankUserDataException()
            }else if (confirmacion.any{it.isDigit()} || confirmacion.any{it.code in 33..38}){
                throw InvalidSelectionException()
            }else if(confirmacion == "S"){
                println(".=== Reiniciando operacion... ===.")
                return true
            }else{
                return false
            }
        }while (true)
    }catch (e: Exception){
        println(e.message)
    }
    return false
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