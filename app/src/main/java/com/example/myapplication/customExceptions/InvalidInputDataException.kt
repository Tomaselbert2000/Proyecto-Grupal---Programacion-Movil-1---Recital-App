package com.example.myapplication.customExceptions

class InvalidInputDataException : Exception(){

    // vamos a lanzar esta excepcion cuando necesitemos validar que un campo de texto tiene caracteres no admitidos
    override val message: String = ".=== Los campos identificatorios de usuario no pueden contener numeros o caracteres especiales. Intente nuevamente ===."
}
