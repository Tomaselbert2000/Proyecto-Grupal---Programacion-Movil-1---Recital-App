package com.example.myapplication.customExceptions

class InvalidSelectionException : Exception(){
    override val message : String = ".=== No se permiten caracteres especiales y/o numericos. Intente nuevamente ===."
}
