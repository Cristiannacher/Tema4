package util.db

import exercicis.Ex3_2.Coordenades
import exercicis.Ex3_2.PuntGeo
import exercicis.Ex3_2.Ruta
import java.sql.Connection
import java.sql.DriverManager

class GestionarRutesBD {

    val con: Connection

    init {
        val url = "jdbc:sqlite:Rutes.sqlite"
        con = DriverManager.getConnection(url)
        val st = con.createStatement()

        val sentSQL = "CREATE TABLE IF NOT EXISTS RUTES(" +
                "num_r INTEGER PRIMARY KEY, " +
                "nom_r TEXT, " +
                "desn INTEGER, " +
                "desn_ac INTEGER " +
                ")"

        val sentSQL2 = "CREATE TABLE IF NOT EXISTS PUNTS(" +
                "num_r INTEGER," +
                "num_p INTEGER," +
                "nom_p TEXT," +
                "latitud REAL," +
                "longitud REAL," +
                "PRIMARY KEY (num_r,num_p)," +
                "FOREIGN KEY (num_r) REFERENCES RUTES(num_r)" +
                ")"
        st.executeUpdate(sentSQL)
        st.executeUpdate(sentSQL2)
    }

    fun close() {
        con.close()
    }

    fun inserir(ruta: Ruta) {
        val st = con.createStatement()
        val sentenciaSQL = "SELECT MAX(num_r) FROM RUTES"
        val maxRuta = st.executeQuery(sentenciaSQL).getInt(1) + 1
        var contador = 1;
        val comanda = "INSERT INTO RUTES VALUES ($maxRuta,'${ruta.nom}',${ruta.desnivell},${ruta.desnivellAcumulat})"
        println(comanda)
        st.executeUpdate(comanda)
        for (punt in ruta.llistaDePunts) {
            val st2= con.createStatement()
            val comanda2 =
                "INSERT INTO PUNTS VALUES($maxRuta,$contador,'${punt.nom}',${punt.coord.latitud},${punt.coord.longitud})"
            println(comanda2)
            st2.executeUpdate(comanda2)
            contador++
        }
    }

    fun guardar(r: Ruta) {
        val st = con.createStatement()
        val sentenciaNomIgual = "SELECT nom_r FROM RUTES"
        val nomRutes = st.executeQuery((sentenciaNomIgual))
        var comprovador = false
        while(nomRutes.next()){
            if(nomRutes.getString(1).equals(r.nom))
                comprovador = true
        }

        if (comprovador){
            val st2 = con.createStatement()
            val sentenciaUpdate = "UPDATE RUTES SET desn = ${r.desnivell}, desn_ac = ${r.desnivellAcumulat} WHERE nom_r = '${r.nom}'"
            println(sentenciaUpdate)
            st2.executeUpdate(sentenciaUpdate)
            val borrarPunts = "DELETE FROM PUNTS WHERE num_r = (SELECT num_r FROM RUTES WHERE nom_r = '${r.nom}')"
            println(borrarPunts)
            st2.executeUpdate(borrarPunts)
            val sentenciaNumRuta = "SELECT num_r FROM RUTES WHERE nom_r = '${r.nom}'"
            val numRuta = st2.executeQuery(sentenciaNumRuta).getInt(1)
            var contador = 1
            for (punt in r.llistaDePunts) {
                val comanda2 =
                    "INSERT INTO PUNTS VALUES($numRuta,$contador,'${punt.nom}',${punt.coord.latitud},${punt.coord.longitud})"
                println()
                st2.executeUpdate(comanda2)
                contador++
            }
        } else{
            inserir(r)
        }

    }

    fun buscar(num: Int): Ruta {
        val stRutes = con.createStatement()
        val stPunts = con.createStatement()
        val traureRuta = "SELECT * FROM RUTES WHERE num_r = $num"
        val traurePunts = "SELECT * FROM PUNTS WHERE num_r = $num"
        val infoRuta = stRutes.executeQuery(traureRuta)
        val punts = stPunts.executeQuery(traurePunts)
        val llistaPunts = mutableListOf<PuntGeo>()
        if (infoRuta.next()) {
            while (punts.next()) {
                val cordenada = Coordenades(punts.getBigDecimal(4).toDouble(), punts.getBigDecimal(5).toDouble())
                val punt = PuntGeo(punts.getString(3), cordenada)
                llistaPunts.add(punt)
            }
            val ruta = Ruta(infoRuta.getString(2), infoRuta.getInt(3), infoRuta.getInt(4), llistaPunts)
            stRutes.close()
            stPunts.close()
            return ruta
        } else {
            stRutes.close()
            stPunts.close()
            println("No hay ninguna ruta, se")
            return Ruta("", 0, 0, mutableListOf())
        }
    }

    fun llistat(): ArrayList<Ruta> {
        val stRutes = con.createStatement()
        val stPunts = con.createStatement()
        val llistaRutes = arrayListOf<Ruta>()
        val traureRuta = "SELECT * FROM RUTES ORDER BY num_r"
        val infoRuta = stRutes.executeQuery(traureRuta)
        while (infoRuta.next()) {
            val traurePunts = "SELECT * FROM PUNTS WHERE num_r = ${infoRuta.getInt(1)}"
            val punts = stPunts.executeQuery(traurePunts)
            val llistaPunts = mutableListOf<PuntGeo>()
            while (punts.next()) {
                val cordenada = Coordenades(punts.getBigDecimal(4).toDouble(), punts.getBigDecimal(5).toDouble())
                val punt = PuntGeo(punts.getString(3), cordenada)
                llistaPunts.add(punt)
            }
            llistaRutes.add(Ruta(infoRuta.getString(2), infoRuta.getInt(3), infoRuta.getInt(4), llistaPunts))
        }
        stRutes.close()
        stPunts.close()
        return llistaRutes
    }

    fun esborrar(i: Int) {
        val st = con.createStatement()
        st.executeQuery("DELETE FROM RUTES WHERE num_r = $i")
        st.executeQuery("DELETE FROM PUNTS WHERE nom_r = $i")
    }

}




