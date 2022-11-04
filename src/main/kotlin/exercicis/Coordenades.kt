package exercicis.Ex3_2

import java.io.Serializable

class Coordenades(var longitud : Double, var latitud : Double) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }

}