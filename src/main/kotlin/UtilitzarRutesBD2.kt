import exercicis.Ex3_2.Coordenades
import exercicis.Ex3_2.PuntGeo
import exercicis.Ex3_2.Ruta
import util.db.GestionarRutesBD

fun main(args: Array<String>) {
    // Creació del gestionador
    val gRutes = GestionarRutesBD()

    var r = gRutes.buscar(1) as Ruta
    r.mostrarRuta()
    r.desnivellAcumulat = 606
    gRutes.guardar(r)

    r = gRutes.buscar(2) as Ruta
    r.mostrarRuta()
    r.llistaDePunts.add(0, PuntGeo("Plaça M.Agustina", Coordenades(39.988507, -0.034533)))
    gRutes.guardar(r)

    println("Després de modificar")
    r = gRutes.buscar(1) as Ruta
    r.mostrarRuta()
    r = gRutes.buscar(2) as Ruta
    r.mostrarRuta()

    gRutes.close()
}