package interfaz

import customExceptions.BlankSelectionException
import customExceptions.BlankUserDataException
import customExceptions.InvalidInputDataException
import customExceptions.InvalidSelectionException
import data.Event
import data.Ticket
import data.TicketCollection
import data.User
import main.kotlin.repositories.PaymentMethodRepository
import repositories.EventRepository
import repositories.TicketCollectionRepository
import repositories.TicketsRepository
import repositories.UserRepository
import java.time.LocalDate
import java.util.Locale.getDefault
import kotlin.system.exitProcess

val contieneNumerosOCaracteresEspeciales = { input: String -> input.any { it.isDigit() || it.code in 33..47 || it.code in 58..64}}
val contieneLetrasOCaracteresEspeciales = { input: String -> input.any { it.isLetter() || it.code in 33..47 || it.code in 58..64}}
val estaEnBlanco = {input: String -> input.isBlank() || input == "" }

fun main(){
    val repoUsuarios = UserRepository

    while (true){
        println("\n")
        println("""
        .=== Sistema de gestion ===.
        +==========================+
        | 1. Iniciar sesion        |
        | 2. Crear nuevo usuario   |
        | 3. Salir del programa    |
        +==========================+
    """.trimIndent())
        val opcionMenuSeleccionada : Int = seleccionarOpcionDelMenu(1, 3)

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
}

fun crearNuevoUsuario(repoUsuarios: UserRepository){
    println("""
        .=== Alta de nuevo usuario en el sistema ===.
        =============================================
    """.trimIndent())

    while (true){
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

                if (estaEnBlanco(nombre) || estaEnBlanco(apellido) || estaEnBlanco(nickname) || estaEnBlanco(password)){
                    excepcionLanzada = true
                    throw BlankUserDataException()
                }
                else if(contieneNumerosOCaracteresEspeciales(nombre) || contieneNumerosOCaracteresEspeciales(apellido)){
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
            return
        }else{
            println("""
                        .=== Error al crear el usuario ===.
            ===========================================================
            ¿Desea reintentar la operacion? Ingresar S/N para continuar
            ===========================================================
        """.trimIndent())
            val reiniciarOperacion = solicitarConfirmacionDeUsuario()
            if(reiniciarOperacion){
                continue
            }else{
                return
            }
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

            if (estaEnBlanco(nickname) || estaEnBlanco(password)){
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
                    // iniciarSesion(repoUsuarios)
                }else{
                    break
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

    while (true){
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
        val opcionSeleccionada = seleccionarOpcionDelMenu(1, 8)

        when (opcionSeleccionada){
            1 -> {
                mostrarEventos(repoEventos)
            }
            2 -> {
                comprarTickets(loggedUser, repoUsuarios, repoEventos, repoTickets, repoTicketCollection, repoMediosPago)
            }
            3 -> {
                cargarSaldo(loggedUser)
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
                break
            }
            8 -> {
                println(".=== Programa finalizado por el usuario ===.")
                exitProcess(0)
            }
        }
    }
}

fun mostrarEventos(repoEventos: EventRepository) {
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
}

fun comprarTickets(
    loggedUser: User,
    repoUsuarios: UserRepository,
    repoEventos: EventRepository,
    repoTickets: TicketsRepository,
    repoTicketCollection: TicketCollectionRepository,
    repoMediosPago: PaymentMethodRepository
) {
    println(
        """
        .=== Pestaña de compra de tickets ===.
        ======================================
        Saldo actual de usuario: $${loggedUser.money}
        ======================================
    """.trimIndent()
    )
    println(
        """
        .=== Seleccione una opcion para iniciar la compra ===.
        1. Ver lista de eventos programados.
        2. Ver lista de artistas.
        3. Volver al menu anterior.
        ======================================================
    """.trimIndent()
    )

    var opcionSeleccionada = "0"

    do {
        try {
            opcionSeleccionada = readln()
            if (estaEnBlanco(opcionSeleccionada)) {
                throw BlankSelectionException()
            } else if (contieneLetrasOCaracteresEspeciales(opcionSeleccionada)) {
                throw InvalidSelectionException()
            } else if (opcionSeleccionada.toInt() !in 1..3) {
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            } else {
                when (opcionSeleccionada.toInt()) {
                    1 -> {
                        mostrarEventos(repoEventos)
                    }

                    2 -> {
                        val artistaSeleccionado = seleccionarArtista(repoEventos)
                        val listaDeEventosDelArtista =
                            repoEventos.obtenerListaDeEventosPorNombreDeArtista(artistaSeleccionado)

                        println(".=== Fechas programadas para el artista seleccionado ===.")
                        for ((index, eventoProgramado) in listaDeEventosDelArtista.withIndex()) {
                            println(
                                """
                                Tarjeta: ${index + 1}
                                =========================================
                                Fecha: ${eventoProgramado.date}
                                Hora: ${eventoProgramado.time}
                                Lugar: ${eventoProgramado.location}
                                Asientos disponibles: ${eventoProgramado.cantidadDeAsientosDisponibles}
                                =========================================
                            """.trimIndent()
                            )
                        }
                        println(
                            """
                            =============================================================
                            Ingrese el número correspondiente a la tarjeta para continuar
                            =============================================================
                        """.trimIndent()
                        )

                        val tarjetaSeleccionada = seleccionarTarjeta(listaDeEventosDelArtista)
                        val eventoSeleccionado = listaDeEventosDelArtista[tarjetaSeleccionada - 1]


                        println(".=== Ingrese la cantidad de entradas que desea comprar (máximo ${eventoSeleccionado.cantidadDeAsientosDisponibles}) ===.")
                        val cantidad = readln().toInt()

                        if (cantidad > eventoSeleccionado.cantidadDeAsientosDisponibles) {
                            println(".=== No hay suficientes entradas disponibles ===.")
                            menuPrincipalSistema(loggedUser)
                        }


                        val seccionElegida = elegirSeccionEnElEstadio()


                        println(".=== Seleccione el medio de pago ===.")
                        val listaMedios = repoMediosPago.obtenerListaDeIDs()
                        for (id in listaMedios) {
                            val medio = repoMediosPago.obtenerMedioDePagoPorId(id)
                            println("$id - ${medio?.name} (Comisión ${medio?.fee?.times(100)}%)")
                        }
                        val medioSeleccionadoId = readln().toLong()


                        val precioBase = 10000.0 * cantidad
                        val medioPago = repoMediosPago.obtenerMedioDePagoPorId(medioSeleccionadoId)
                        val totalConComision = precioBase + (precioBase * (medioPago?.fee ?: 0.0))

                        if (loggedUser.money < totalConComision) {
                            println(".=== Saldo insuficiente. Recargue dinero para continuar ===.")
                            menuPrincipalSistema(loggedUser)
                        }


                        val nuevoTicket = Ticket(
                            id = (0..10000).random().toLong(),
                            eventId = eventoSeleccionado.id,
                            quantity = cantidad,
                            section = seccionElegida
                        )
                        repoTickets.registrarNuevoTicket(nuevoTicket, repoEventos.obtenerListaDeIDsEventos())

                        val nuevaColeccion = TicketCollection(
                            id = (0..10000).random().toLong(),
                            userId = loggedUser.id,
                            paymentId = medioSeleccionadoId,
                            ticketCollection = mutableListOf(nuevoTicket.id)
                        )

                        val registroExitoso = repoTicketCollection.registrarNuevaColeccion(
                            nuevaColeccion,
                            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
                            repoTickets.obtenerListaDeIDsDeTickets()
                        )

                        if (registroExitoso) {
                            println(
                                """
                                .=== Compra realizada con éxito ===.
                                ===========================================
                                Entradas compradas: $cantidad
                                Total abonado (con comisión): $${totalConComision}
                                Nuevo saldo: $${loggedUser.money}
                                ===========================================
                            """.trimIndent()
                            )
                        } else {
                            println(".=== Error al registrar la compra. Verifique los datos e intente nuevamente ===.")
                        }

                        println("Presione Enter para volver al menú principal")
                        readln()
                        menuPrincipalSistema(loggedUser)
                    }

                    3 -> {
                        menuPrincipalSistema(loggedUser)
                    }
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    } while (opcionSeleccionada != "0" || opcionSeleccionada.toInt() !in 1..3 || estaEnBlanco(opcionSeleccionada) || contieneLetrasOCaracteresEspeciales(
            opcionSeleccionada
        )
    )
}

fun cargarSaldo(loggedUser: User) {
    println("""
        .=== Carga de saldo de usuario ===.
         Saldo actual: $${loggedUser.money}
        ===================================
              Ingresar saldo a cargar:
        ===================================
    """.trimIndent())
    while (true){
        try {
            val saldoACargar : Double = readln().toDouble()
            if(loggedUser.cargarSaldo(saldoACargar)){
                println("""
                .=== Saldo agregado en cuenta de manera exitosa ===.
                - Valor de la operacion: $${saldoACargar}
                - Saldo actualizado: $${loggedUser.money}
                ====================================================
            """.trimIndent())
                return
            }else{
                println("""
                .=== El saldo minimo para realizar un ingreso es $1000 ===.
                .=== El saldo maximo para realizar un ingreso es $1000000 ===.
                  ¿Desea reintentar la operacion? Ingresar S/N para continuar
                ==============================================================
            """.trimIndent())
                val reiniciarOperacion = solicitarConfirmacionDeUsuario()
                if (reiniciarOperacion){
                    println(".=== Reingresar saldo a cargar ===.")
                    continue
                }else{
                    return
                }
            }
        }catch (_ : Exception){
            println(".=== Este campo solo acepta valores numericos. Intente nuevamente ===.")
            continue
        }
    }
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

    if(!listaCompras.isNotEmpty()){
        println("""
                .=== No se registran compras de tickets hasta el momento ===.
                =============================================================
                        Presionar Enter para volver al menu anterior
                =============================================================
            """.trimIndent())
        readln()
        menuPrincipalSistema(loggedUser)
    }else {
        for(compra in listaCompras){

            val medioDePagoUsadoEnLaCompra = repoMediosPago.obtenerMedioDePagoPorId(compra.paymentId)

            println("""
            .=== Numero de compra: ${compra.id} ===.
            Medio de pago utilizado: ${medioDePagoUsadoEnLaCompra?.name}
        """.trimIndent())
            if(compra.ticketCollection.isNotEmpty()){

                var acumuladorTotal = 0.0
                var acumuladorCantidadEntradas = 0

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

                    if(ticketParaMostrar != null){
                        acumuladorTotal += ticketParaMostrar.precio
                        acumuladorCantidadEntradas ++
                    }
                }

                println("""
                    ================================================
                    .=== Cantidad total de entradas adquiridas: $acumuladorCantidadEntradas
                    .=== Monto total abonado en entradas: $${acumuladorTotal}
                    ================================================
                """.trimIndent())
            }
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
    """.trimIndent())
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
}

fun cerrarSesion(loggedUser: User) {
    loggedUser.estadoDeSesion = false
    println(".=== Sesion finalizada por el usuario ===.")
}

fun seleccionarOpcionDelMenu(rangoInferior : Int, rangoSuperior: Int): Int {
    var opcionMenuSeleccionada = 0
    do {
        try {
            println("Ingrese un valor: ")
            opcionMenuSeleccionada = readln().toInt()
            if(opcionMenuSeleccionada !in rangoInferior..rangoSuperior){
                println(".=== El valor ingresado no corresponde a una opcion del menu, intente nuevamente ===.")
            }else if(estaEnBlanco(opcionMenuSeleccionada.toString())){
                throw BlankSelectionException()
            }else if(contieneLetrasOCaracteresEspeciales(opcionMenuSeleccionada.toString())){
                throw InvalidSelectionException()
            }
        }catch (_ : NumberFormatException){
            println(".=== Solo se aceptan valores numericos, intente nuevamente ===.")
        }
    }while (opcionMenuSeleccionada !in rangoInferior..rangoSuperior || estaEnBlanco(opcionMenuSeleccionada.toString()) || contieneLetrasOCaracteresEspeciales(opcionMenuSeleccionada.toString()))
    return opcionMenuSeleccionada
}

fun solicitarConfirmacionDeUsuario(): Boolean {
    var opcionSeleccionada = ""
    do {
        try {
            opcionSeleccionada = readln().uppercase(getDefault())
            if (opcionSeleccionada == "") {
                throw BlankSelectionException()
            }else if(contieneNumerosOCaracteresEspeciales(opcionSeleccionada)){
                throw InvalidSelectionException()
            }else if(opcionSeleccionada != "S" && opcionSeleccionada != "N"){
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            }else{
                return opcionSeleccionada == "S"
            }
        }catch (e:Exception){
            println(e.message)
        }
    }while (opcionSeleccionada == "" || contieneNumerosOCaracteresEspeciales(opcionSeleccionada) || opcionSeleccionada != "S" && opcionSeleccionada != "N")
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

fun seleccionarArtista(repoEventos: EventRepository): String {
    val nombresArtistas = mutableListOf<String>()
    for(ev in repoEventos.obtenerListaDeEventos()){
        nombresArtistas.add(ev.artist)
    }
    println(".=== Lista de artistas ===.")
    for(nombre in nombresArtistas){
        println("${nombresArtistas.indexOf(nombre)} . $nombre")
    }
    println(".=== Ingrese el numero de artista para continuar ===.")
    var artista : Int = -1

    do {
        try {
            artista = readln().toInt()

            if(artista !in 0..<nombresArtistas.size){
                println(".=== El valor ingresado no corresponde con una opcion del menu. Intente nuevamente ===.")
            }else if(contieneLetrasOCaracteresEspeciales(artista.toString())){
                throw InvalidSelectionException()
            }else if (estaEnBlanco(artista.toString())){
                throw BlankSelectionException()
            }else{
                println(".=== Artista seleccionado: ${nombresArtistas[artista]}")
            }
        }catch (e : Exception){
            println(e.message)
        }
    }while (artista !in 0..<nombresArtistas.size || contieneLetrasOCaracteresEspeciales(artista.toString()) || estaEnBlanco(artista.toString()))

    return nombresArtistas[artista]
}

fun seleccionarTarjeta(listaDeEventosDelArtista: MutableList<Event>): Int {
    var tarjetaSeleccionada = -1
    do {
        try {
            val input = readln()
            if (estaEnBlanco(input)) {
                throw BlankSelectionException()
            } else if (contieneLetrasOCaracteresEspeciales(input)) {
                throw InvalidSelectionException()
            }

            tarjetaSeleccionada = input.toInt()

            if (tarjetaSeleccionada !in 1..listaDeEventosDelArtista.size) {
                println(".=== El valor ingresado no corresponde a una opción del menú. Intente nuevamente ===.")
                tarjetaSeleccionada = -1
            }
        } catch (e: Exception) {
            println(e.message)
        }
    } while (tarjetaSeleccionada !in 1..listaDeEventosDelArtista.size)

    return tarjetaSeleccionada
}


fun elegirSeccionEnElEstadio(): String {
    var seccionElegida = ""
    println("""
        .=== Seleccionar el tipo de ubicacion deseada ===.
        1 . Campo.
        2 . Platea.
        3 . Palco.
        ==================================================
    """.trimIndent())
    do {
        try {
            seccionElegida = readln()
            if(estaEnBlanco(seccionElegida)){
                throw BlankSelectionException()
            }else if(contieneLetrasOCaracteresEspeciales(seccionElegida)){
                throw InvalidSelectionException()
            }else if(seccionElegida.toInt() !in 1..3){
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            }else{
                when (seccionElegida.toInt()){
                    1 -> return "Campo"
                    2 -> return "Platea"
                    3 -> return "Palco"
                }
            }
        }catch (e:Exception){
            println(e.message)
        }
    }while (seccionElegida.toInt() !in 1..3 || estaEnBlanco(seccionElegida) || contieneLetrasOCaracteresEspeciales(seccionElegida))
    return seccionElegida
}