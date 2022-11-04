package exercicis

import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:sqlite:Rutes.sqlite"
    val con = DriverManager.getConnection(url)
    val st = con.createStatement()

    val sentSQL = "CREATE TABLE RUTES(" +
            "num_r INTEGER PRIMARY KEY, " +
            "nom_r TEXT, " +
            "desn INTEGER, " +
            "desn_ac INTEGER " +
            ")"

    val sentSQL2 = "CREATE TABLE PUNTS("+
            "num_r INTEGER,"+
            "num_p INTEGER,"+
            "nom_p TEXT,"+
            "latitud REAL,"+
            "longitud REAL,"+
            "PRIMARY KEY (num_r,num_p),"+
            "FOREIGN KEY (num_r) REFERENCES RUTES(num_r)"+
            ")"
    st.executeUpdate(sentSQL)
    st.executeUpdate(sentSQL2)

    st.close()
    con.close()
}