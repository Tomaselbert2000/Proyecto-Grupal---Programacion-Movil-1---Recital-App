package main.kotlin.repositories

import main.kotlin.data.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

object EventRepository {

    private val events = mutableListOf<Event>()

    init {
        events.add(
            Event(
                1L,
                1000,
                "2025-10-02",
                "21:00",
                "Luna Park",
                "Abel Pintos",
                "https://i.pinimg.com/originals/ea/3c/84/ea3c844c21b0812535bafe66358a213d.jpg"
            )
        )

        events.add(
            Event(
                2L,
                1000,
                "2025-12-29",
                "20:00",
                "Estadio River Plate",
                "Duki",
                "https://nuebo.es/wp-content/uploads/2023/02/P2250134.jpg"
            )
        )

        events.add(
            Event(
                3L,
                1000,
                "2021-7-30",
                "22:00",
                "Estadio Velez Sarsfield",
                "Fito Paez",
                "https://valaaguelaquesipuedo.com/wp-content/uploads/2017/02/FITO-PAEZ-768x1024.jpg"
            )
        )

        events.add(
            Event(
                4L,
                1000,
                "2025-11-16",
                "20:00",
                "Teatro Gran Rex",
                "Tini",
                "https://www.hitfm.es/wp-content/uploads/2021/11/TINI-4-768x1024.jpg"
            )
        )

        events.add(
            Event(
                5L,
                1000,
                "2025-9-21",
                "19:00",
                "Movistar Arena",
                "La Renga",
                "https://www.lapoliticaonline.com/files/image/252/252925/67fd77a5c3b5d-screen-and-max-width768px_768_1024!.jpg?s=e69c0d47fdddc9b64d747994d26f0bc2&d=1751382069"
            )
        )

        events.add(
            Event(
                6L,
                1000,
                "2025-11-09",
                "21:00",
                "Hipodromo de Palermo",
                "Bizarrap",
                "https://urbandamagazine.com/wp-content/uploads/2023/01/CROP-Bizarrap-8-sept-2021-prensa22980-1-768x1024.jpg"
            )
        )

        events.add(
            Event(
                7L,
                1000,
                "2025-11-07",
                "20:00",
                "Teatro Vorterix",
                "Skrillex",
                "https://myhotposters.com/cdn/shop/products/mR0034.jpeg?v=1748542540"
            )
        )
    }

    fun registrarNuevoEvento(evento: Event) : Boolean{
        return !this.seSuperponeAOtro(evento)
                && this.cantidadDeAsientosValida(evento)
                && this.esFechaValida(evento)
                && this.esHoraValida(evento)
                && this.validarId(evento)
                && !this.esDuplicado(evento)
                && this.events.add(evento)
    }

    private fun cantidadDeAsientosValida(evento: Event): Boolean {
        return evento.cantidadDeAsientosDisponibles >= 1
    }

    /*
    Para el caso de la fecha y hora de un evento, lo que vamos a plantear
    es que la misma deba poder ser parseada usando DateTimeParser, es decir,
    dentro de los metodos de validacion de fecha y hora buscamos obtener un
    objeto de fecha o bien de hora a partir del String que tenemos como valor
    de entrada en el constructor de evento.

    Esto implica el uso de una excepcion ya que si el intento de parseo falla,
    arroja una DateTimeParserException, pero si ponemos esto dentro de un bloque
    try & catch, ambos metodos van a intentar parsear el objeto y en caso de que
    lo hubieran hecho, indica que el valor es valido y solo retornan true

    Y si dentro de alguno de ellos se lanzara una excepcion, es decir, que o bien
    la fecha o la hora no sean validos, la excepcion se captura y se retorna false
    haciendo que el metodo principal de registro de eventos retorne tambien false.
     */

    private fun esHoraValida(evento: Event) : Boolean{
        try {
            LocalTime.parse(evento.time)
            return true
        }
        catch (e: DateTimeParseException){
            return false
        }
    }

    private fun esFechaValida(evento: Event) : Boolean{
        try {
            LocalDate.parse(evento.date)
            return true
        }
        catch (e: DateTimeParseException){
            return false
        }
    }

    private fun validarId(evento: Event): Boolean {
        return evento.id >= 1L
    }

    private fun seSuperponeAOtro(evento: Event): Boolean {
        for (e in this.events){
            if (e.date == evento.date && e.time == evento.time && e.location == evento.location){
                return true
            }
        }
        return false
    }

    private fun esDuplicado(evento: Event) : Boolean{
        for (e in this.events){
            if (e == evento || e.id == evento.id){
                return true
            }
        }
        return false
    }

    fun obtenerEventoPorId(eventIdParaBuscar: Long?): Event? {
        for (e in this.events){
            if (e.id == eventIdParaBuscar){
                return e
            }
        }
        return null
    }

    fun buscarEventoPorId(idBuscado: Long): Event? {
        for (e in this.events){
            if (e.id == idBuscado){
                return e
            }
        }
        return null
    }

    fun obtenerListaDeIDsEventos(): MutableList<Long> {
        val listadoDeIds = mutableListOf<Long>()
        for (e in this.events){
            listadoDeIds.add(e.id)
        }
        return listadoDeIds
    }

    fun obtenerListaDeEventos(): MutableList<Event> {
        return this.events
    }

    fun obtenerAsientosDisponibles(id: Long): Int? {
        for (e in this.events){
            if (e.id == id){
                return e.cantidadDeAsientosDisponibles
            }
        }
        return null
    }

    fun limpiarInstancia() { // con esta funcion restauramos el estado de la instancia acorde al template proporcionado
        events.clear()
    }

    fun reiniciarInstancia() {
        events.add(
            Event(
                1L,
                1000,
                "2025-10-02",
                "21:00",
                "Luna Park",
                "Abel Pintos",
                "https://i.pinimg.com/originals/ea/3c/84/ea3c844c21b0812535bafe66358a213d.jpg"
            )
        )

        events.add(
            Event(
                2L,
                1000,
                "2025-12-29",
                "20:00",
                "Estadio River Plate",
                "Duki",
                "https://nuebo.es/wp-content/uploads/2023/02/P2250134.jpg"
            )
        )

        events.add(
            Event(
                3L,
                1000,
                "2021-7-30",
                "22:00",
                "Estadio Velez Sarsfield",
                "Fito Paez",
                "https://valaaguelaquesipuedo.com/wp-content/uploads/2017/02/FITO-PAEZ-768x1024.jpg"
            )
        )

        events.add(
            Event(
                4L,
                1000,
                "2025-11-16",
                "20:00",
                "Teatro Gran Rex",
                "Tini",
                "https://www.hitfm.es/wp-content/uploads/2021/11/TINI-4-768x1024.jpg"
            )
        )

        events.add(
            Event(
                5L,
                1000,
                "2025-9-21",
                "19:00",
                "Movistar Arena",
                "La Renga",
                "https://www.lapoliticaonline.com/files/image/252/252925/67fd77a5c3b5d-screen-and-max-width768px_768_1024!.jpg?s=e69c0d47fdddc9b64d747994d26f0bc2&d=1751382069"
            )
        )

        events.add(
            Event(
                6L,
                1000,
                "2025-11-09",
                "21:00",
                "Hipodromo de Palermo",
                "Bizarrap",
                "https://urbandamagazine.com/wp-content/uploads/2023/01/CROP-Bizarrap-8-sept-2021-prensa22980-1-768x1024.jpg"
            )
        )

        events.add(
            Event(
                7L,
                1000,
                "2025-11-07",
                "20:00",
                "Teatro Vorterix",
                "Skrillex",
                "https://myhotposters.com/cdn/shop/products/mR0034.jpeg?v=1748542540"
            )
        )
    }
}