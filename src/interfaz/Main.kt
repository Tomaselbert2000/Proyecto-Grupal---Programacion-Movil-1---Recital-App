package interfaz

import customExceptions.BlankSelectionException
import customExceptions.BlankUserDataException
import customExceptions.InvalidInputDataException
import customExceptions.InvalidSelectionException
import data.Event
import data.Ticket
import data.User
import repositories.*
import java.time.LocalDate
import java.util.Locale.getDefault
import kotlin.system.exitProcess

val contieneNumerosOCaracteresEspeciales = { input: String -> input.any { it.isDigit() || it.code in 33..47 || it.code in 58..64}}
val contieneLetrasOCaracteresEspeciales = { input: String -> input.any { it.isLetter() || it.code in 33..47 || it.code in 58..64}}
val estaEnBlanco = {input: String -> input.isBlank() || input == "" }

fun main(){
    val repoUsuarios = UserRepository // tanto para iniciar sesion como crear nuevos usuarios en el sistema, la funcion main del programa accede al repositorio de usuarios

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
        val opcionMenuSeleccionada : Int = seleccionarOpcionDelMenu(1, 3) // llamamos a la funcion y especificamos como rango las opciones en este input

        when(opcionMenuSeleccionada){
            1 -> {
                iniciarSesion(repoUsuarios)
            }
            2 -> {
                crearNuevoUsuario(repoUsuarios)
            }
            3 -> {
                println(".=== Programa finalizado por el usuario ===.")
                exitProcess(0) // con esta linea automaticamente cerramos el proceso actual y programa sale de manera normal
            }
        }
    }
}

fun crearNuevoUsuario(repoUsuarios: UserRepository){
    println("""
        .=== Alta de nuevo usuario en el sistema ===.
        =============================================
    """.trimIndent())

    while (true){ // se encapsula la logica de creacion de usuario dentro de un bucle iniciado en true

        // declaramos todas las variables necesarias dentro del bucle, en caso de reiniciarse, se recargan en limpio
        var nombre : String
        var apellido : String
        var nickname : String
        var password : String
        var newUser : User? = null // como una de las condiciones para salir del bucle es que el usuario no sea null, lo inicializamos como tal
        var excepcionLanzada = false // esta variable funciona como bandera en caso que se ingresen datos invalidos durante el proceso, evitando que el repositorio de usuarios registre el objeto erroneamente

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
                    if(!passwordValida(password)){ // llamamos a la funcion y le pasamos la password como parametro, devolvera un boolean
                        println(".=== La contraseña ingresada no cumple los requisitos minimos de seguridad. Intente nuevamente ===.")
                    }else{ // en caso de ser valida, inicializamos la instancia del nuevo usuario pero todavia no la registramos
                        newUser = User(1L, nickname, password, nombre, apellido, 0.0, LocalDate.now().toString())
                    }
                }while(!passwordValida(password))

                // si alguno de los campos lanzo una excepcion pasamos la bandera a true
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

        if(!excepcionLanzada){ // si ninguna excepcion se lanzo, vamos al siguiente paso
            repoUsuarios.registrarNuevoUsuario(newUser) // ya con la instancia creada, la guardamos en el registro de usuarios
            println("""
            .=== Usuario creado exitosamente ===.
            =====================================
                Volviendo al menu principal...
            =====================================
        """.trimIndent())
            return // y con este return salimos automaticamente de esta funcion
        }else{ // en caso de algun error, preguntamos al usuario si desea reintentar el proceso
            println("""
                        .=== Error al crear el usuario ===.
            ===========================================================
            ¿Desea reintentar la operacion? Ingresar S/N para continuar
            ===========================================================
        """.trimIndent())
            val reiniciarOperacion = solicitarConfirmacionDeUsuario()
            if(reiniciarOperacion){
                continue // si el usuario elige seguir, con esta linea pasamos a la siguiente iteracion del bucle y pedimos de nuevo los datos
            }else{
                return // y si no, este return nos saca al menu principal
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
                    continue
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

                comprarTickets(loggedUser, repoEventos, repoTickets, repoTicketCollection, repoMediosPago)
            }
            3 -> {
                cargarSaldo(loggedUser)
            }
            4 ->{
                mostrarHistorialDeComprasDeUsuario(loggedUser, repoTicketCollection, repoTickets, repoEventos)
            }
            5 -> {
                verSaldoActualUsuario(loggedUser)
            }
            6 ->{
                // mostrarMetodosDePagoDisponibles(repoMediosPago)
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
            Numero de evento: ${listaDeEventos.indexOf(event)}
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

    while (true){
        val opcionSeleccionada = seleccionarOpcionDelMenu(1, 2)
        when (opcionSeleccionada){
            1 -> {
                mostrarEventos(repoEventos)
                val eventoSeleccionado = seleccionarEvento(repoEventos)
                val tipoSeccionElegida = elegirSeccionEnElEstadio()
                val cantidadDeAsientos = ingresarCantidadAsientos(eventoSeleccionado)

                // pasamos a crear la instancia del ticket que queremos registrar
                val nuevoTicket = Ticket(60L, eventoSeleccionado.id, cantidadDeAsientos, tipoSeccionElegida)

                if(loggedUser.money >= nuevoTicket.calcularTotalPorTicket()){
                    if(procesarCompra(loggedUser, nuevoTicket, repoTickets, repoEventos,repoTicketCollection)){
                        break
                    }
                    else{
                        println(".=== Ocurrio un error al procesar la compra. Intente nuevamente ===.")
                    }
                }else{
                    println(".=== Saldo insuficiente para completar la compra ===.")
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
    var indiceEventoSeleccionado : Int = -1
    do {
        try {
            indiceEventoSeleccionado = readln().toInt()
            if(indiceEventoSeleccionado !in 0..<repoEventos.obtenerListaDeEventos().size){
                println(".=== El valor seleccionado no corresponde a un evento programado ===.")
            }
        }catch (_:NumberFormatException){
            println(".=== Este campo solo acepta valores numericos. Intente nuevamente ===.")
        }
    }while (indiceEventoSeleccionado !in 0..<repoEventos.obtenerListaDeEventos().size)

    return repoEventos.obtenerListaDeEventos()[indiceEventoSeleccionado]
}

fun ingresarCantidadAsientos(eventoSeleccionado: Event): Int {
    println(".=== Ingresar cantidad de asientos ===.")
    var cantidadAsientosElegida: Int = -1
    do {
        try {
            cantidadAsientosElegida = readln().toInt()
            if(cantidadAsientosElegida > eventoSeleccionado.cantidadDeAsientosDisponibles){
                println(".=== No se encuentran suficientes asientos disponibles para la compra. Intente nuevamente ===.")
            }else if(cantidadAsientosElegida <= 0){
                println(".=== La cantidad de asientos ingresada no es valida. Intente nuevamente ===.")
            }
        }catch (_:NumberFormatException){
            println(".=== Este campo solo acepta valores numericos. Intente nuevamente ===.")
        }
    }while (cantidadAsientosElegida <=0 || cantidadAsientosElegida > eventoSeleccionado.cantidadDeAsientosDisponibles)
    return cantidadAsientosElegida
}

fun procesarCompra(
    loggedUser: User,
    nuevoTicket: Ticket,
    repoTickets: TicketsRepository,
    repoEventos: EventRepository,
    repoTicketCollection: TicketCollectionRepository
) : Boolean{
    if(repoTickets.registrarNuevoTicket(nuevoTicket, repoEventos.obtenerListaDeIDsEventos())){

    }else{
        return false
    }
    return false
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
){
    // traemos del repositorio de colecciones, aquella asociada al usuario mediante su id
    val listaIDsTicketsCompradosPorElUsuario = repoTicketCollection.buscarComprasPorId(loggedUser.id)

    // ahora leemos la coleccion para sacar cada id y obtener su ticket asociado

    var acumuladorTotalPorCompras = 0.0 // para obtener el total de todas las compras declaramos este acumulador

    for(idTicket in listaIDsTicketsCompradosPorElUsuario){
        val ticketAsociado = repoTickets.obtenerTicketPorId(idTicket) // obtenemos del repositorio el ticket en esta linea
        val eventoAsociado = repoEventos.obtenerEventoPorId(ticketAsociado?.eventId) // mediante el ticket obtenemos el evento


        // aca generamos la salida combinando toda la informacion relevante de ambos
        println("""
            .=== Numero de ticket: ${ticketAsociado?.id}
            .=== Informacion del evento asistido ===
            Fecha: ${eventoAsociado?.date}
            Hora: ${eventoAsociado?.time}
            Lugar: ${eventoAsociado?.location}
            ===============================
            . Asientos adquiridos: ${ticketAsociado?.quantity}
            . Precio unitario: $${ticketAsociado?.precio}
            . Total abonado: $${ticketAsociado?.calcularTotalPorTicket()}
            ===============================
        """.trimIndent())
        if(ticketAsociado != null){
            acumuladorTotalPorCompras += ticketAsociado.calcularTotalPorTicket() // acumulamos en cada pasada el total por ese ticket puntual
        }
    }
    println(".=== Monto total acumulado por todas las compras: $${acumuladorTotalPorCompras}\n")
}

fun verSaldoActualUsuario(loggedUser: User) {
    println(".=== Saldo actual del usuario: $${loggedUser.money} ===.\n")
}

//fun mostrarMetodosDePagoDisponibles(repoMediosPago: PaymentMethodRepository) {
//    val listaDeIDsMediosDePago = repoMediosPago.obtenerListaDeIDs()
//    println("""
//        .=== Medios de pago disponibles actualmente ===.
//        ================================================
//    """.trimIndent())
//    for(id in listaDeIDsMediosDePago){
//        val medioDePago = repoMediosPago.obtenerMedioDePagoPorId(id)
//        println("""
//            Metodo de pago: ${medioDePago?.name}
//            Comision aplicada: ${medioDePago?.fee?.times(100)}%
//            ================================================
//        """.trimIndent())
//    }
//}

fun cerrarSesion(loggedUser: User) {
    loggedUser.estadoDeSesion = false
    println(".=== Sesion finalizada por el usuario ===.")
}

fun seleccionarOpcionDelMenu(rangoInferior : Int, rangoSuperior: Int): Int { // recibimos el margen de opciones que tiene se tienen que validar
    var opcionMenuSeleccionada = 0 // inicializamos este valor como cero antes de entrar al do while

    do {
        try { // como el ingreso de datos puede causar excepciones, se envuelve toda la logica en un bloque try

            println("Ingrese un valor: ")
            opcionMenuSeleccionada = readln().toInt() // casteamos el readln() a entero
            if(opcionMenuSeleccionada !in rangoInferior..rangoSuperior){ // primero vemos que se encuentre en el rango pedido
                println(".=== El valor ingresado no corresponde a una opcion del menu, intente nuevamente ===.")
            }
        }catch (_ : NumberFormatException){ // en caso que se ingrese cualquier otra cosa que no sea un numero, directamente capturamos la excepcion lanzada y mostramos un mensaje
            println(".=== Solo se aceptan valores numericos, intente nuevamente ===.")
        }
    }while (opcionMenuSeleccionada !in rangoInferior..rangoSuperior)
    return opcionMenuSeleccionada // una vez validado el numero de la opcion lo retornamos al metodo que lo haya requerido
}

fun solicitarConfirmacionDeUsuario(): Boolean { // esta funcion trabaja en aquellos casos en los cuales se pida al usuario elegir Si o No
    var opcionSeleccionada = "" // inicializamos este string como vacio

    do {
        try {
            opcionSeleccionada = readln().uppercase(getDefault()) // pasamos el valor ingresado por teclado a mayusculas antes de usarlo
            if (opcionSeleccionada == "") {
                throw BlankSelectionException() // si sigue en blanco (el usuario presionó Enter sin elegir nada) lanzamos esta excepcion
            }else if(contieneNumerosOCaracteresEspeciales(opcionSeleccionada)){ // si ingreso valores incorrectos como caracteres especiales o numeros lo capturamos aca
                throw InvalidSelectionException()
            }else if(opcionSeleccionada != "S" && opcionSeleccionada != "N"){ // si no es alguna de las dos letras especificadas, mostramos un mensaje
                println(".=== El valor ingresado no corresponde a una opcion del menu. Intente nuevamente ===.")
            }else{
                return true // en caso que haya confirmado, retorna true y salimos de la funcion
            }
        }catch (e:Exception){
            println(e.message)
        }
    }while (opcionSeleccionada == "" || contieneNumerosOCaracteresEspeciales(opcionSeleccionada) || opcionSeleccionada != "S" && opcionSeleccionada != "N")
    return false
}

fun passwordValida(password: String): Boolean {
    // esta funcion trabaja contando los tipos de caracteres de una cadena de texto, y devuelve true o false si tiene un minimo de cada tipo incluido
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

//fun seleccionarTarjeta(listaDeEventosDelArtista: MutableList<Event>): Int {
//    var tarjetaSeleccionada = -1
//    do {
//        try {
//            val input = readln()
//            if (estaEnBlanco(input)) {
//                throw BlankSelectionException()
//            } else if (contieneLetrasOCaracteresEspeciales(input)) {
//                throw InvalidSelectionException()
//            }
//
//            tarjetaSeleccionada = input.toInt()
//
//            if (tarjetaSeleccionada !in 1..listaDeEventosDelArtista.size) {
//                println(".=== El valor ingresado no corresponde a una opción del menú. Intente nuevamente ===.")
//                tarjetaSeleccionada = -1
//            }
//        } catch (e: Exception) {
//            println(e.message)
//        }
//    } while (tarjetaSeleccionada !in 1..listaDeEventosDelArtista.size)
//
//    return tarjetaSeleccionada
//}


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