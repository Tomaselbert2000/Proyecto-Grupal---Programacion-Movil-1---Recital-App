package com.example.myapplication.customExceptions

class BlankUserDataException() : Exception(){

    // vamos a lanzar esta excepcion cuando se intente pasar un campo de formulario en blanco
    override val message : String = ".=== Los campos identificatorios de usuario no pueden quedar vacios. Intente nuevamente ===."
}