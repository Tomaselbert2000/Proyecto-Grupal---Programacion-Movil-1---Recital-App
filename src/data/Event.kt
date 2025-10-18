package main.kotlin.data

data class Event(
    val id: Long,
    var cantidadDeAsientosDisponibles: Int,
    var date: String,
    val time: String,
    val location: String,
    val artist: String,
    val image: String
)
{
    override fun toString(): String {
        return "Event(id=$id, cantidadDeAsientosDisponibles=$cantidadDeAsientosDisponibles, date='$date', time='$time', location='$location', artist='$artist', image='$image')"
    }
}
