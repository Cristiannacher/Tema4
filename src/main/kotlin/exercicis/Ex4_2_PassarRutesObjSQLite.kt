package exercicis

import exercicis.Ex3_2.Ruta
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:Rutes.sqlite"
    val con = DriverManager.getConnection(url)
    val st = con.createStatement()

    val file = File("src/main/kotlin/exercicis/Rutes.obj")
    val reader = ObjectInputStream(FileInputStream(file))
    var contador = 1
    var contador2 = 1
    try {
        while (true){
            val ruta = reader.readObject() as Ruta
            val comanda = "INSERT INTO RUTES VALUES ($contador,'${ruta.nom}',${ruta.desnivell},${ruta.desnivellAcumulat})"
            println(comanda)
            st.executeUpdate(comanda)
            for (punt in ruta.llistaDePunts){
                val comanda2 = "INSERT INTO PUNTS VALUES($contador,$contador2,'${punt.nom}',${punt.coord.latitud},${punt.coord.longitud})"
                println(comanda2)
                st.executeUpdate(comanda2)
                contador2++
            }

            contador++
        }
    } catch (e: EOFException) {
        reader.close()
        st.close()
        con.close()
    }


}