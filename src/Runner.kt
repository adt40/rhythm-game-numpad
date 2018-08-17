import javax.swing.JFrame
import javax.swing.WindowConstants

fun main(args: Array<String>) {

    val reader = SongFileReader()
    val song = reader.readSongFile("song1.dat", true)

    val panel = GamePanel(song)
    val frame = JFrame(song.name)
    frame.contentPane.add(panel)
    frame.setSize(600,600)
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.isVisible = true
}