package exercicis

import javax.swing.JFrame
import java.awt.EventQueue
import java.awt.BorderLayout
import javax.swing.JPanel
import java.awt.FlowLayout
import java.sql.DriverManager
import javax.swing.JComboBox
import javax.swing.JButton
import javax.swing.JTextArea
import javax.swing.JLabel

class Finestra : JFrame() {

    init {
        val url = "jdbc:sqlite:Rutes.sqlite"
        val con = DriverManager.getConnection(url)
        val st = con.createStatement()

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setTitle("JDBC: Visualitzar Rutes")
        setSize(450, 450)
        setLayout(BorderLayout())

        val panell1 = JPanel(FlowLayout())
        val panell2 = JPanel(BorderLayout())
        add(panell1, BorderLayout.NORTH)
        add(panell2, BorderLayout.CENTER)

        val llistaRutes = arrayListOf<String>()
        val sentenciaSQL = "SELECT nom_r FROM RUTES"
        val rs = st.executeQuery(sentenciaSQL)
        while (rs.next()) {
            llistaRutes.add(rs.getString(1))
        }


        val combo = JComboBox<String>(llistaRutes.toTypedArray())
        panell1.add(combo)
        val eixir = JButton("Eixir")
        panell1.add(eixir)
        val area = JTextArea()
        panell2.add(JLabel("Llista de punts de la ruta:"), BorderLayout.NORTH)
        panell2.add(area, BorderLayout.CENTER)


        combo.addActionListener() {
            // Sentèncis quan s'ha seleccionat un element del JComboBox
            // Han de consistir en omplir el JTextArea
            area.text = ""
            val index = combo.selectedIndex + 1
            val sentenciaSQL = "SELECT * FROM PUNTS WHERE num_r = $index"
            val rs = st.executeQuery(sentenciaSQL)
            while (rs.next()) {
                area.append("${rs.getString(3)} (${rs.getBigDecimal(4)},${rs.getBigDecimal(5)})\n")
            }

        }

        eixir.addActionListener() {
            rs.close()
            con.close()

        }
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        Finestra().isVisible = true
    }
}