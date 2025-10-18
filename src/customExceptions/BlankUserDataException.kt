package customExceptions

class BlankUserDataException() : Exception(){

    /*
    Creamos una excepcion personalizada que se dispara si
    los datos ingresados quedan en blanco, como por ejemplo
    si el usuario presiona Enter directamente al momento
    de llenar un formulario.
     */

    override val message : String = ".=== Los campos identificatorios de usuario no pueden quedar vacios. Intente nuevamente ===."
}