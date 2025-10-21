package customExceptions

class InvalidSelectionException : Exception(){
    override val message : String = ".=== No se permiten caracteres especiales o numericos. Intente nuevamente ===."
}
