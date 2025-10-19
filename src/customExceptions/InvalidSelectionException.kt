package customExceptions

class InvalidSelectionException : Exception(){
    override val message : String = ".=== El campo de confirmacion no puede contener caracteres especiales o numericos. Intente nuevamente ===."
}
