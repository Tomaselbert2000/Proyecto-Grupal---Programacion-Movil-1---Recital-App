package main.kotlin.repositories

import main.kotlin.data.TicketCollection

object TicketCollectionRepository {

    private val ticketCollections = mutableListOf<TicketCollection>()
    private val repoUsuarios = UserRepository
    private val repoTickets = TicketsRepository
    private val repoMediosDePago = PaymentMethodRepository
    private val repoEventos = EventRepository

    init {

        // cuando se inicializa esta clase, ya carga los cambios apropiados en el saldo de los usuarios que registren compras

        this.registrarNuevaColeccion(TicketCollection(1L, 1510L, 1L, mutableListOf(1L, 3L, 12L, 27L, 5L, 19L, 8L, 30L, 2L, 14L, 22L, 9L)),
            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
            repoTickets.obtenerListaDeIDsDeTickets())

        this.registrarNuevaColeccion(TicketCollection(2L, 1504L, 2L, mutableListOf(1L, 3L, 6L, 17L, 30L, 11L, 24L, 3L, 29L, 18L, 6L, 10L)),
            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
            repoTickets.obtenerListaDeIDsDeTickets())

        this.registrarNuevaColeccion(TicketCollection(3L, 2802L, 3L, mutableListOf(1L, 3L, 25L, 7L, 14L, 30L, 2L, 12L, 28L, 19L, 5L, 25L)),
            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
            repoTickets.obtenerListaDeIDsDeTickets())
    }

    fun get(): List<TicketCollection> {
        return emptyList()
    }

    fun registrarNuevaColeccion( // este metodo registra dentro del repositorio una nueva coleccion que hace las veces de registro de compra

        nuevaColeccion: TicketCollection, // este objeto es la nueva instancia en si
        listaDeIDsDeUsuariosRegistrados: MutableList<Long>, // aca recibimos una lista de tipo Long con los IDs de usuarios del sistema
        listaDeIDsDeTicketsRegistrados: MutableList<Long> // y aca otra lista long con los IDs de los tickets registrados

    ) : Boolean{ // armamos una variable que tenga la suma logica de todas las validaciones que tenemos que tener para guardar un registro
        val listaValidaciones = !this.esDuplicado(nuevaColeccion) && this.validarUserID(nuevaColeccion, listaDeIDsDeUsuariosRegistrados)
                && this.validarId(nuevaColeccion) && this.validarListadoDeIDsTickets(nuevaColeccion, listaDeIDsDeTicketsRegistrados)
                && !this.idRepetido(nuevaColeccion) && this.cuentaConSaldoSuficiente(nuevaColeccion)

        if(!listaValidaciones) { // si alguna de ellas falla, va a dar false, y automaticamente el metodo completo retorna false
            return false
        }

        val montoTotal = this.calcularMontoTotal(nuevaColeccion) // nos guardamos el valor del monto total
        val usuarioQueRealizaLaCompra = this.repoUsuarios.buscarUsuarioPorID(nuevaColeccion.userId) // y aca buscamos el usuario en el repo

        if (usuarioQueRealizaLaCompra != null){ // validamos que el usuario no sea null
            usuarioQueRealizaLaCompra.money -= montoTotal // le restamos del saldo la cantidad que gasto
        }

        for(ticketID in nuevaColeccion.ticketCollection){ // leemos la lista de IDs que trae la nueva coleccion
            val ticketBuscado = this.repoTickets.obtenerTicketPorId(ticketID) // por cada uno vamos a buscar en el repo de tickets cual es el objeto con ese ID
            val eventIdParaBuscar = ticketBuscado?.eventId // una vez tenemos el ticket, obtenemos el eventID para saber a que evento esta asociado
            val eventoBuscado = this.repoEventos.obtenerEventoPorId(eventIdParaBuscar) // con con el eventId pasamos a buscar el objeto evento con ese ID

            if (eventoBuscado != null){ // validamos que no sea null
                ticketBuscado?.quantity?.let { eventoBuscado.cantidadDeAsientosDisponibles -= it } // y le actualizamos la cantidad de asientos segun el valor quantity del ticket
            }
        }

        this.ticketCollections.add(nuevaColeccion)
        return true
    }

    private fun calcularMontoTotal(nuevaColeccion: TicketCollection): Double {
        val precioBasico = nuevaColeccion.ticketCollection.size * 10000.0
        val valorComision = precioBasico * this.obtenerValorDeComisionAplicable(nuevaColeccion)
        return precioBasico + valorComision
    }

    private fun cuentaConSaldoSuficiente(nuevaColeccion: TicketCollection): Boolean {
        val precioDeLaCompra = nuevaColeccion.ticketCollection.size * 10000.0
        val saldoParaChequear = this.obtenerSaldoUsuarioAsociado(nuevaColeccion.userId, repoUsuarios) // llamamos a una funcion que mediante el repo de usuarios busca el saldo a comparar
        return saldoParaChequear >= precioDeLaCompra + precioDeLaCompra * this.obtenerValorDeComisionAplicable(nuevaColeccion) // y devolvemos el valor de verdad de esta comparacion
    }

    // con esta funcion se busca dentro del repositorio de metodos de pago usando el ID, y mediante eso se saca el valor de comision
    private fun obtenerValorDeComisionAplicable(nuevaColeccion: TicketCollection): Double {
        val metodoAplicable = this.repoMediosDePago.obtenerMedioDePagoPorId(nuevaColeccion.paymentId)
        return metodoAplicable?.fee ?: 0.0
    }


    private fun validarListadoDeIDsTickets(nuevaColeccion: TicketCollection, listaDeIDsDeTicketsRegistrados: MutableList<Long>): Boolean {

        /*
        Aca tenemos una funcion compacta que lo que hace es leer la lista de IDs registrados que obtiene la funcion principal como parametro.
        Esta lista contiene todos los IDs de Tickets ya registrados en el sistema, es decir, aquellos que ya estan validados y que existen

        ¿Que buscamos hacer aca?
        Validar que todos los numeros de ID de tickets que tenga una nueva coleccion efectivamente existan dentro
        de la lista de tickets registrados. Basicamente, el sistema debe comparar los numeros de ID de la nueva coleccion y asegurarse
        que ninguno de ellos apunta a un numero de id de un ticket que no exista para asi evitar errores.

        Con el metodo all se leen todos los elementos en la lista que se quiere ingresar y se los compara con la lista validada,
        para ello el metodo contains() devuelve true o false segun si ese elemento está presente (usamos el it apuntando a ese valor).
        Si en algun momento encuentra un id de ticket que no exista en el repo principal, va a retornar false y ya sabemos entonces
        que alguno de los IDs no es correcto, por lo tanto se descarta el objeto.
         */

        return nuevaColeccion.ticketCollection.all { listaDeIDsDeTicketsRegistrados.contains(it) }
    }

    private fun validarUserID(nuevaColeccion: TicketCollection, listaDeIDsRegistrados: MutableList<Long>): Boolean {
        for (item in listaDeIDsRegistrados) {
            if (nuevaColeccion.userId == item) {
                return true
            }
        }
        return false
    }

    private fun validarId(nuevaColeccion: TicketCollection): Boolean {
        return nuevaColeccion.id >= 1L
    }

    private fun idRepetido(nuevaColeccion: TicketCollection): Boolean {
        for (c in this.ticketCollections){
            if (c.id == nuevaColeccion.id){
                return true
            }
        }
        return false
    }

    private fun esDuplicado(nuevaColeccion: TicketCollection): Boolean {
        for (collection in this.ticketCollections){
            if (nuevaColeccion == collection){
                return true
            }
        }
        return false
    }

    fun obtenerTotalDeTicketsCompradosPorUserId(userIdQueBuscamos: Long): Int {
        var cantidad = 0
        for(item in this.ticketCollections){
            if (item.userId == userIdQueBuscamos){
                cantidad += item.ticketCollection.size
            }
        }
        return cantidad
    }

    fun obtenerMontoTotalAcumuladoPorCompras(userIdQueBuscamos: Long): Double {
        var montoTotal = 0.0 // inicializamos un acumulador aca
        for (item in this.ticketCollections){ // iteramos sobre todas las compras
            if (item.userId == userIdQueBuscamos){ // vemos si se corresponde el userId con el que buscamos
                montoTotal += item.ticketCollection.size * 10000 // en caso de serlo, multiplicamos el size de esa compra (la cantidad de tickets) por el precio unitario
            }
        }
        return montoTotal
    }

    fun obtenerSaldoUsuarioAsociado(userIdQueBuscamos: Long, userRepo: UserRepository): Double {
        return userRepo.obtenerSaldoDeUsuario(userIdQueBuscamos)
    }

    fun buscarComprasPorId(userIdQueCompro: Long): MutableList<TicketCollection> {
        val listaDeComprasDelUsuario = mutableListOf<TicketCollection>()
        for (compra in this.ticketCollections){
            if (compra.userId == userIdQueCompro){
                listaDeComprasDelUsuario.add(compra)
            }
        }
        return listaDeComprasDelUsuario
    }

    fun obtenerListaDeIDsDeColecciones(): MutableList<Long> {
        val listaDeIDsDeColeccionesRegistrados = mutableListOf<Long>()
        for(item in this.ticketCollections){
            listaDeIDsDeColeccionesRegistrados.add(item.id)
        }
        return listaDeIDsDeColeccionesRegistrados
    }

    fun limpiarInstancia() {
        ticketCollections.clear()
    }

    fun reiniciarInstancia() {
        this.registrarNuevaColeccion(TicketCollection(1L, 1510L, 1L, mutableListOf(1L, 3L, 12L, 27L, 5L, 19L, 8L, 30L, 2L, 14L, 22L, 9L)),
            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
            repoTickets.obtenerListaDeIDsDeTickets())

        this.registrarNuevaColeccion(TicketCollection(2L, 1504L, 2L, mutableListOf(1L, 3L, 6L, 17L, 30L, 11L, 24L, 3L, 29L, 18L, 6L, 10L)),
            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
            repoTickets.obtenerListaDeIDsDeTickets())

        this.registrarNuevaColeccion(TicketCollection(3L, 2802L, 3L, mutableListOf(1L, 3L, 25L, 7L, 14L, 30L, 2L, 12L, 28L, 19L, 5L, 25L)),
            repoUsuarios.obtenerListaDeIDsDeUsuarios(),
            repoTickets.obtenerListaDeIDsDeTickets())
    }
}