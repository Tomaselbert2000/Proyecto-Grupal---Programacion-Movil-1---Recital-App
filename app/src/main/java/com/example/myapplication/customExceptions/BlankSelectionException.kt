package com.example.myapplication.customExceptions

class BlankSelectionException : Exception() {
    override val message : String = ".=== Este campo de ingreso/confirmacion no puede quedar en blanco. Intente nuevamente ===."
}
