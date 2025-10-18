package customExceptions

class BlankSelectionException : Exception() {
    override val message : String = ".=== El campo de confirmacion no puede quedar en blanco. Intente nuevamente ===."
}
