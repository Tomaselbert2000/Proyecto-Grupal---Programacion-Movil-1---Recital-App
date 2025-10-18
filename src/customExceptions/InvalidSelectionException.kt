package customExceptions

class InvalidSelectionException : Exception(){
    override val message : String = ".=== El campo de confirmacion solo puede contener 'S' o 'N'. Intente nuevamente ===."
}
