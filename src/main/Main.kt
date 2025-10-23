package main

import customExceptions.BlankSelectionException
import customExceptions.BlankUserDataException
import customExceptions.InvalidInputDataException
import customExceptions.InvalidSelectionException
import data.superclass.Event
import data.superclass.PaymentMethod
import data.superclass.Ticket
import data.superclass.User
import repositories.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.system.exitProcess

val contieneNumerosOCaracteresEspeciales =
    { input: String -> input.any { it.isDigit() || it.code in 33..47 || it.code in 58..64 } }
val contieneLetrasOCaracteresEspeciales =
    { input: String -> input.any { it.isLetter() || it.code in 33..47 || it.code in 58..64 } }
val estaEnBlanco = { input: String -> input.isBlank() || input == "" }

fun main() {
    // tanto para iniciar sesion como crear nuevos usuarios en el sistema, la funcion main del programa accede al repositorio de usuarios como de colecciones
    val repoUsuarios = UserRepository
    val repoTicketCollection = TicketCollectionRepository

    while (true) {
        println("\n")
        println(
            """
        .=== Sistema de gestion ===.
        +==========================+
        | 1. Iniciar sesion        |
        | 2. Crear nuevo usuario   |
        | 3. Salir del programa    |
        +==========================+
    """.trimIndent()
        )
        val opcionMenuSeleccionada: Int = seleccionarOpcionDelMenu(
            1,
            3
        ) // llamamos a la funcion y especificamos como rango las opciones en este input

        when (opcionMenuSeleccionada) {
            1 -> {
                iniciarSesion(repoUsuarios)
            }

            2 -> {
                crearNuevoUsuario(repoUsuarios, repoTicketCollection)
            }

            3 -> {
                println(".=== Programa finalizado por el usuario ===.")
                exitProcess(0) // con esta linea automaticamente cerramos el proceso actual y programa sale de manera normal
            }
        }
    }
}

fun crearNuevoUsuario(repoUsuarios: UserRepository, repoTicketCollection: TicketCollectionRepository) {
    println(
        """
        .=== Alta de nuevo usuario en el sistema ===.
        =============================================
    """.trimIndent()
    )

    while (true) { // se encapsula la logica de creacion de usuario dentro de un bucle iniciado en true

        // declaramos todas las variables necesarias dentro del bucle, en caso de reiniciarse, se recargan en limpio
        var nombre: String
        var apellido: String
        var nickname: String
        var password: String
        var newUser: User? =
            null // como una de las condiciones para salir del bucle es que el usuario no sea null, lo inicializamos como tal
        var excepcionLanzada =
            false // esta variable funciona como bandera en caso que se ingresen datos invalidos durante el proceso, evitando que el repositorio de usuarios registre el objeto erroneamente

        do {

            // vamos a pedir que se ingresen datos para el nuevo usuario, cualquiera de ellos podria generar una excepcion capturable por el bloque catch siguiente
            try {
                println(".=== Ingresar nombre (incluyendo 2do nombre si corresponde) ===.")
                nombre = readln()

                println(".=== Ingresar apellido (incluyendo 2do apellido si corresponde) ===.")
                apellido = readln()

                println(".=== Ingresar nickname de usuario ===.")
                nickname = readln()

                do { // este ciclo do while se encarga de validar que la contraseña ingresada tenga un minimo de seguridad antes de crear el usuario
                    println(
                        """
            .=== Ingresar contraseña ===.
            =============================
            Requisitos minimos:
            1 letra mayuscula
            1 caracter especial
            1 caracter numerico
            8 caracteres de longitud
            =============================
        """.trimIndent()
                    )
                    password = readln()
                    if (!passwordValida(password)) { // llamamos a la funcion y le pasamos la password como parametro, devolvera un boolean
                        println(".=== La contraseña ingresada no cumple los requisitos minimos de seguridad. Intente nuevamente ===.")
                    } else { // en caso de ser valida, inicializamos la instancia del nuevo usuario pero todavia no la registramos
                        newUser = User(
                            generarNuevoId(repoUsuarios),
                            nickname,
                            password,
                            nombre,
                            apellido,
                            0.0,
                            LocalDate.now().toString()
                        )
                    }
                } while (!passwordValida(password))

                // si alguno de los campos lanzo una excepcion pasamos la bandera a true
                if (estaEnBlanco(nombre) || estaEnBlanco(apellido) || estaEnBlanco(nickname) || estaEnBlanco(password)) {
                    excepcionLanzada = true
                    throw BlankUserDataException()
                } else if (contieneNumerosOCaracteresEspeciales(nombre) || contieneNumerosOCaracteresEspeciales(apellido)) {
                    excepcionLanzada = true
                    throw InvalidInputDataException()
                }
            } catch (e: Exception) {
                println(e.message)
            }
        } while (newUser == null)

        if (!excepcionLanzada) { // si ninguna excepcion se lanzo, vamos al siguiente paso
            if (repoUsuarios.registrarNuevoUsuario(newUser) && repoTicketCollection.crearNuevaColeccion(generarNuevoId(repoTicketCollection), newUser)) {
                println(
                    """
            .=== Usuario creado exitosamente ===.
            =====================================
                Volviendo al menu principal...
            =====================================
            """.trimIndent()
                )
                return // y con este return salimos automaticamente de esta funcion
            } else {
                println(
                    """
                        .=== Error al crear el usuario ===.
            ===========================================================
            ¿Desea reintentar la operacion? Ingresar S/N para continuar
            ===========================================================
        """.trimIndent()
                )
                val reiniciarOperacion = solicitarConfirmacionDeUsuario()
                if (reiniciarOperacion) {
                    continue // si el usuario elige seguir, con esta linea pasamos a la siguiente iteracion del bucle y pedimos de nuevo los datos
                } else {
                    return // y si no, este return nos saca al menu principal
                }
            }
        }
    }
}

fun iniciarSesion(repoUsuarios: UserRepository) {
    var nickname: String
    var password: String
    var loggedUser: User? = null

    do {
        try {
            println(".=== Ingresar nickname ===.")
            nickname = readln()
            println(".=== Ingresar contraseña ===.")
            password = readln()

            if (estaEnBlanco(nickname) || estaEnBlanco(password)) {
                throw BlankUserDataException()
            }

            loggedUser = repoUsuarios.login(nickname, password)

            if (loggedUser == null) {
                println(
                    """
                            .=== Usuario o contraseña incorrectos ===.
                    ===========================================================
                    ¿Desea reintentar la operacion? Ingresar S/N para continuar
                    ===========================================================
                """.trimIndent()
                )

                val reiniciarOperacion = solicitarConfirmacionDeUsuario()

                if (reiniciarOperacion) {
                    continue
                } else {
                    break
                }

            } else {
                return menuPrincipalSistema(loggedUser)
            }
        } catch (e: Exception) {
            println(e.message)
        }
    } while (loggedUser == null)
}

fun menuPrincipalSistema(loggedUser: User) {
    val repoUsuarios = UserRepository
    val repoEventos = EventRepository
    val repoTickets = TicketsRepository
    val repoTicketCollection = TicketCollectionRepository
    val repoMediosPago = PaymentMethodRepository

    while (true) {
        println(
            """
            .=== Menu principal ===.
        * Bienvenido ${loggedUser.name} *
        ===================================
        1. Ver eventos disponibles.
        2. Comprar tickets.
        3. Cargar saldo.
        4. Ver historial de compras.
        5. Ver saldo actual de usuario.
        6. Ver metodos de pago disponibles
        7. Modificar datos de usuario.
        8. Cerrar sesion.
        9. Salir del programa.
        ==================================
    """.trimIndent()
        )
        val opcionSeleccionada = seleccionarOpcionDelMenu(1, 9)

        when (opcionSeleccionada) {
            1 -> {
                mostrarEventos(repoEventos)
            }

            2 -> {

                comprarTickets(loggedUser, repoEventos, repoTickets, repoTicketCollection, repoMediosPago)
            }

            3 -> {
                cargarSaldo(loggedUser)
            }

            4 -> {
                mostrarHistorialDeComprasDeUsuario(
                    loggedUser, repoTicketCollection, repoTickets, repoEventos, repoMediosPago
                )
            }

            5 -> {
                verSaldoActualUsuario(loggedUser)
            }

            6 -> {
                mostrarMetodosDePagoDisponibles(repoMediosPago)
            }

            7 -> {
                modificarDatosDeUsuario(loggedUser, repoUsuarios)
            }

            8 -> {
                cerrarSesion(loggedUser)
                break
            }

            9 -> {
                println(".=== Programa finalizado por el usuario ===.")
                exitProcess(0)
            }
        }
    }
}

fun mostrarEventos(repoEventos: EventRepository) {
    val listaDeEventos: MutableList<Event> = repoEventos.obtenerListaDeEventos()
    println(".=== Información de eventos ===.")
    for (event in listaDeEventos) {
        println(
            """
            =========================================================================================================================================================================
            Numero de evento: ${listaDeEventos.indexOf(event)}
            Fecha: ${event.date}
            Hora: ${event.time}
            Lugar: ${event.location}
            Artista invitado: ${event.artist}
            URL Imagen: ${event.image}
            =========================================================================================================================================================================
        """.trimIndent()
        )
    }
}

fun comprarTickets(
    loggedUser: User,
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
        2. Volver al menu anterior.
        ======================================================
    """.trimIndent()
    )

    while (true) {
        val opcionSeleccionada = seleccionarOpcionDelMenu(1, 2)
        when (opcionSeleccionada) {
            1 -> {
                mostrarEventos(repoEventos)
                val eventoSeleccionado = seleccionarEvento(repoEventos)
                val tipoSeccionElegida = elegirSeccionEnElEstadio()
                val cantidadDeAsientos = ingresarCantidadAsientos(eventoSeleccionado)
                val medioDePagoElegido = seleccionarMedioDePago(repoMediosPago)
                val nuevoTicket =
                    Ticket(
                        generarNuevoId(repoTickets),
                        eventoSeleccionado.id,
                        cantidadDeAsientos,
                        tipoSeccionElegida,
                        idMedioDePagoUsado = medioDePagoElegido.id
                    )
                val comisionPorMedioDePago =
                    medioDePagoElegido.calcularMontoComision(nuevoTicket.calcularTotalPorTicket())
                val montoTotalAAbonar = nuevoTicket.calcularTotalPorTicket() + comisionPorMedioDePago

                println(
                    """
                    .=== Revise la informacion de compra antes de continuar ===.
                    . Evento seleccionado: ${eventoSeleccionado.artist}
                    . A realizarse el dia ${eventoSeleccionado.date} a las ${eventoSeleccionado.time} en ${eventoSeleccionado.location}
                    . El presente ticket otorga derecho de acceso a $cantidadDeAsientos plazas en la seccion: $tipoSeccionElegida
                    . Valor unitario por asiento: $${nuevoTicket.precio}
                    . Medio de pago elegido: ${medioDePagoElegido.name}, se aplica una comision de $$comisionPorMedioDePago
                    === Total a abonar por su compra: $${montoTotalAAbonar}
                """.trimIndent()
                )
                println(
                    "\n.=== ¿Desea confirmar la compra? ===." +
                            "\n.=== Ingresar S/N para continuar ===.\n"
                )
                val confirmarCompra = solicitarConfirmacionDeUsuario()

                if (confirmarCompra) {
                    if (loggedUser.money >= montoTotalAAbonar) {
                        if (procesarCompra(
                                loggedUser,
                                nuevoTicket,
                                repoTickets,
                                repoEventos,
                                repoTicketCollection,
                                montoTotalAAbonar
                            )
                        ) {
                            println(".=== Compra realizada con exito. Muchas gracias :) ===.")
                            break
                        } else {
                            println(".=== Ocurrio un error al procesar la compra. Intente nuevamente ===.")
                            break
                        }
                    } else {
                        println(".=== Saldo insuficiente para completar la compra ===.")
                        break
                    }
                } else {
                    println(".=== Operacion de compra cancelada por el usuario ===.")
                    break
                }
            }

            2 -> {
                break
            }
        }
    }
}

fun seleccionarEvento(repoEventos: EventRepository): Event {
    println(".=== Seleccione el evento al cual desea asistir ===.")
    var indiceEventoSeleccionado: Int = -1
    do {
        try {
            indiceEventoSeleccionado = readln().toInt()
            if (indiceEventoSeleccionado !in 0..<repoEventos.obtenerListaDeEventos().size) {
                println(".=== El valor seleccionado no corresponde a un evento programado ===.")
            }
        } catch (_: NumberFormatException) {
            mostrarErrorSoloSeAceptanValoresNumericos()
        }
    } while (indiceEventoSeleccionado !in 0..<repoEventos.obtenerListaDeEventos().size)
    return repoEventos.obtenerListaDeEventos()[indiceEventoSeleccionado]
}

fun ingresarCantidadAsientos(eventoSeleccionado: Event): Int {
    println(".=== Ingresar cantidad de asientos ===.")
    var cantidadAsientosElegida: Int = -1
    do {
        try {
            cantidadAsientosElegida = readln().toInt()
            if (cantidadAsientosElegida > eventoSeleccionado.cantidadDeAsientosDisponibles) {
                println(".=== No se encuentran suficientes asientos disponibles para la compra. Intente nuevamente ===.")
            } else if (cantidadAsientosElegida <= 0) {
                println(".=== La cantidad de asientos ingresada no es valida. Intente nuevamente ===.")
            }
        } catch (_: NumberFormatException) {
            mostrarErrorSoloSeAceptanValoresNumericos()
        }
    } while (cantidadAsientosElegida <= 0 || cantidadAsientosElegida > eventoSeleccionado.cantidadDeAsientosDisponibles)
    return cantidadAsientosElegida
}

fun seleccionarMedioDePago(repoMediosPago: PaymentMethodRepository): PaymentMethod.MetodoDePago {
    mostrarMetodosDePagoDisponibles(repoMediosPago)
    println(".=== Seleccione un medio de pago a continuacion ===.")
    var indiceMedioDePago: Int = -1
    do {
        try {
            indiceMedioDePago = readln().toInt()
            if (indiceMedioDePago !in 0..<repoMediosPago.listaMetodosDePago.size) {
                println(".=== El valor ingresado no corresponde a un metodo de pago disponible. Intente nuevamente ===.")
            }
        } catch (_: NumberFormatException) {
            mostrarErrorSoloSeAceptanValoresNumericos()
        }
    } while (indiceMedioDePago !in 0..<repoMediosPago.listaMetodosDePago.size)
    return repoMediosPago.listaMetodosDePago[indiceMedioDePago]
}

fun mostrarErrorSoloSeAceptanValoresNumericos() {
    println(".=== Este campo solo acepta valores numericos. Intente nuevamente ===.")
}

// se aplica sobrecarga en este caso para elegir sobre cual objeto queremos trabajar
fun generarNuevoId(repoTickets: TicketsRepository): Long {
    var randomId: Long
    do {
        randomId = Random.nextLong().absoluteValue
    } while (randomId in repoTickets.obtenerListaDeIDsDeTickets())
    return randomId
}

fun generarNuevoId(repoUsuarios: UserRepository): Long {
    var randomId: Long
    do {
        randomId = Random.nextLong().absoluteValue
    } while (randomId in repoUsuarios.obtenerListaDeIDsDeUsuarios())
    return randomId
}

fun generarNuevoId(repoTicketCollection: TicketCollectionRepository): Long {
    var randomId: Long
    do {
        randomId = Random.nextLong().absoluteValue
    }while (randomId in repoTicketCollection.obtenerListaDeIDsDeColecciones())
    return randomId
}

fun procesarCompra(
    loggedUser: User,
    nuevoTicket: Ticket,
    repoTickets: TicketsRepository,
    repoEventos: EventRepository,
    repoTicketCollection: TicketCollectionRepository,
    montoTotalAAbonar: Double
): Boolean {
    if (repoTickets.registrarNuevoTicket(nuevoTicket, repoEventos.obtenerListaDeIDsEventos())) {
        repoTicketCollection.buscarComprasPorId(loggedUser.id).add(nuevoTicket.id)
        loggedUser.descontarSaldo(montoTotalAAbonar)
        return true
    } else {
        return false
    }
}

fun cargarSaldo(loggedUser: User) {
    println(
        """
        .=== Carga de saldo de usuario ===.
         Saldo actual: $${loggedUser.money}
        ===================================
              Ingresar saldo a cargar:
        ===================================
    """.trimIndent()
    )
    while (true) {
        try {
            val saldoACargar: Double = readln().toDouble()
            if (loggedUser.cargarSaldo(saldoACargar)) {
                println(
                    """
                .=== Saldo agregado en cuenta de manera exitosa ===.
                - Valor de la operacion: $${saldoACargar}
                - Saldo actualizado: $${loggedUser.money}
                ====================================================
            """.trimIndent()
                )
                return
            } else {
                println(
                    """
                .=== El saldo minimo para realizar un ingreso es $1000 ===.
                .=== El saldo maximo para realizar un ingreso es $1000000 ===.
                  ¿Desea reintentar la operacion? Ingresar S/N para continuar
                ==============================================================
            """.trimIndent()
                )
                val reiniciarOperacion = solicitarConfirmacionDeUsuario()
                if (reiniciarOperacion) {
                    println(".=== Reingresar saldo a cargar ===.")
                    continue
                } else {
                    return
                }
            }
        } catch (_: Exception) {
            mostrarErrorSoloSeAceptanValoresNumericos()
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
) {
    // traemos del repositorio de colecciones, aquella asociada al usuario mediante su id
    val listaIDsTicketsCompradosPorElUsuario = repoTicketCollection.buscarComprasPorId(loggedUser.id)

    // ahora leemos la coleccion para sacar cada id y obtener su ticket asociado

    var acumuladorTotalPorCompras = 0.0 // para obtener el total de todas las compras declaramos este acumulador

    for (idTicket in listaIDsTicketsCompradosPorElUsuario) {
        val ticketAsociado =
            repoTickets.obtenerTicketPorId(idTicket) // obtenemos del repositorio el ticket en esta linea
        val eventoAsociado =
            repoEventos.obtenerEventoPorId(ticketAsociado?.eventId) // mediante el ticket obtenemos el evento
        val medioDePagoUsado = repoMediosPago.buscarMetodoDePagoPorId(ticketAsociado?.idMedioDePagoUsado ?: 0)

        // aca generamos la salida combinando toda la informacion relevante de ambos
        println(
            """
            .=== Numero de ticket: ${ticketAsociado?.id}
            .=== Informacion del evento asistido ===
            Fecha: ${eventoAsociado?.date}
            Hora: ${eventoAsociado?.time}
            Lugar: ${eventoAsociado?.location}
            ===============================
            . Asientos adquiridos: ${ticketAsociado?.quantity}
            . Medio de pago utilizado: ${medioDePagoUsado?.name}
            . Precio unitario: $${ticketAsociado?.precio}
            . Comisiones: $${calcularTotalComisionesDeTicket(ticketAsociado, medioDePagoUsado)}
            . Total abonado: $${ticketAsociado?.calcularTotalPorTicket()}
            ===============================
        """.trimIndent()
        )
        if (ticketAsociado != null) {
            acumuladorTotalPorCompras += ticketAsociado.calcularTotalPorTicket() + calcularTotalComisionesDeTicket(ticketAsociado, medioDePagoUsado)// acumulamos en cada pasada el total por ese ticket puntual
        }
    }
    println(".=== Monto total acumulado por todas las compras: $${acumuladorTotalPorCompras}\n")
}

fun calcularTotalComisionesDeTicket(ticketAsociado: Ticket?, medioDePagoUsado: PaymentMethod.MetodoDePago?): Double {
    if(ticketAsociado !=null) {
        if (medioDePagoUsado != null) {
            return medioDePagoUsado.calcularMontoComision(ticketAsociado.calcularTotalPorTicket(), LocalDateTime.now())
        }
    }
    return 0.0
}

fun verSaldoActualUsuario(loggedUser: User) {
    println(".=== Saldo actual del usuario: $${loggedUser.money} ===.\n")
}

fun mostrarMetodosDePagoDisponibles(repoMediosPago: PaymentMethodRepository) {
    println(".=== Medios de pago disponibles actualmente ===.")
    val listaMediosDePago = repoMediosPago.listaMetodosDePago
    for (medio in listaMediosDePago) {
        println(
            """
            ${repoMediosPago.listaMetodosDePago.indexOf(medio)} . ${medio.name}, comision aplicable: ${medio.fee * 100}%
            ====================================================
        """.trimIndent()
        )
    }
}

fun cerrarSesion(loggedUser: User) {
    loggedUser.estadoDeSesion = false
    println(".=== Sesion finalizada por el usuario ===.")
}

fun modificarDatosDeUsuario(loggedUser: User, repoUsuarios: UserRepository){
    println("""
        .=== Seleccionar apartado que desea modificar ===.
        . Nickname
        . Contraseña
        ==================================================
    """.trimIndent())

    val opcionSeleccionada = seleccionarOpcionDelMenu(1, 2)

    if (opcionSeleccionada == 1) {
        modificarNickname(loggedUser, repoUsuarios)
    }else{
        modificarPassword(loggedUser)
    }
}

fun modificarNickname(loggedUser: User, repoUsuarios: UserRepository) {
    println(".=== Ingresar nuevo nickname de usuario ===.")

    var nuevoNickname: String
    do {
        nuevoNickname = readln()
        if(nuevoNickname in repoUsuarios.obtenerListaDeNicknames()){
            println(".=== El nombre de usuario elegido ya se encuentra ocupado. Intente nuevamente ===.")
        }
    }while (nuevoNickname in repoUsuarios.obtenerListaDeNicknames())
    loggedUser.actualizarNickname(nuevoNickname)
}

fun modificarPassword(loggedUser: User) {
    println(".=== Para continuar con el proceso, ingrese su contraseña actual ===.")
    val currentPassword = readln()
    if(currentPassword == loggedUser.password){
        var newPassword : String
        do {
            newPassword = readln()
            if(!passwordValida(newPassword)){
                println(".=== La contraseña ingresada no cumple los requisitos minimos de seguridad. Intente nuevamente ===.")
            }
        }while(!passwordValida(newPassword))
        loggedUser.actualizarPassword(newPassword)
    }else{
        println(".=== No fue posible actualizar la contraseña. Intente nuevamente ===.")
    }
}

fun seleccionarOpcionDelMenu(
    rangoInferior: Int,
    rangoSuperior: Int
): Int { // recibimos el margen de opciones que tiene se tienen que validar
    var opcionMenuSeleccionada = 0 // inicializamos este valor como cero antes de entrar al do while

    do {
        try { // como el ingreso de datos puede causar excepciones, se envuelve toda la logica en un bloque try

            println("Ingrese un valor: ")
            opcionMenuSeleccionada = readln().toInt() // casteamos el readln() a entero
            if (opcionMenuSeleccionada !in rangoInferior..rangoSuperior) { // primero vemos que se encuentre en el rango pedido
                println(".=== El valor ingresado no corresponde a una opcion del menu, intente nuevamente ===.")
            }
        } catch (_: NumberFormatException) { // en caso que se ingrese cualquier otra cosa que no sea un numero, directamente capturamos la excepcion lanzada y mostramos un mensaje
            mostrarErrorSoloSeAceptanValoresNumericos()
        }
    } while (opcionMenuSeleccionada !in rangoInferior..rangoSuperior)
    return opcionMenuSeleccionada // una vez validado el numero de la opcion lo retornamos al metodo que lo haya requerido
}

fun solicitarConfirmacionDeUsuario(): Boolean { // esta funcion trabaja en aquellos casos en los cuales se pida al usuario elegir Si o No
    var opcionSeleccionada = "" // inicializamos este string como vacio

    do {
        try {
            opcionSeleccionada =
                readln().uppercase() // pasamos el valor ingresado por teclado a mayusculas antes de usarlo
            if (opcionSeleccionada == "") {
                throw BlankSelectionException() // si sigue en blanco (el usuario presionó Enter sin elegir nada) lanzamos esta excepcion
            } else if (contieneNumerosOCaracteresEspeciales(opcionSeleccionada)) { // si ingreso valores incorrectos como caracteres especiales o numeros lo capturamos aca
                throw InvalidSelectionException()
            } else if (opcionSeleccionada != "S" && opcionSeleccionada != "N") { // si no es alguna de las dos letras especificadas, mostramos un mensaje
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            } else {
                return opcionSeleccionada == "S" // en caso que haya confirmado, retorna true y salimos de la funcion
            }
        } catch (e: Exception) {
            println(e.message)
        }
    } while (opcionSeleccionada == "" || contieneNumerosOCaracteresEspeciales(opcionSeleccionada) || opcionSeleccionada != "S" && opcionSeleccionada != "N")
    return false
}

fun passwordValida(password: String): Boolean {
    // esta funcion trabaja contando los tipos de caracteres de una cadena de texto, y devuelve true o false si tiene un minimo de cada tipo incluido
    var contadorMayusculas = 0
    var contadorEspeciales = 0
    var contadorNumeros = 0
    for (letter in password) {
        if (letter.isDigit()) {
            contadorNumeros++
        } else if ((letter).code in 33..38) {
            contadorEspeciales++
        } else if (letter.isUpperCase()) {
            contadorMayusculas++
        }
    }
    return contadorMayusculas >= 1 && contadorEspeciales >= 1 && contadorNumeros >= 1 && password.length >= 8
}

fun elegirSeccionEnElEstadio(): String {
    var seccionElegida = ""
    println(
        """
        .=== Seleccionar el tipo de ubicacion deseada ===.
        1 . Campo.
        2 . Platea.
        3 . Palco.
        ==================================================
    """.trimIndent()
    )
    do {
        try {
            seccionElegida = readln()
            if (estaEnBlanco(seccionElegida)) {
                throw BlankSelectionException()
            } else if (contieneLetrasOCaracteresEspeciales(seccionElegida)) {
                throw InvalidSelectionException()
            } else if (seccionElegida.toInt() !in 1..3) {
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            } else {
                when (seccionElegida.toInt()) {
                    1 -> return "Campo"
                    2 -> return "Platea"
                    3 -> return "Palco"
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    } while (seccionElegida.toInt() !in 1..3 || estaEnBlanco(seccionElegida) || contieneLetrasOCaracteresEspeciales(
            seccionElegida
        )
    )
    return seccionElegida
}