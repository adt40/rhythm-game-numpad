import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.time.Instant
import java.util.*
import javax.swing.JPanel

class GamePanel(val song: Song): JPanel() {

    private var currentTime: Long = 0

    private val fadeInNotes = LinkedList<NoteGroup>()
    private val activeNotes = LinkedList<NoteGroup>()
    private val fadeOutNotes = LinkedList<NoteGroup>()

    private val timingWindow: Long = 100
    private val fadeTime: Long = (song.msPerBeat * 0.75).toLong()

    init {
        val userInputManager = UserInputManager(activeNotes)
        addKeyListener(userInputManager)
        isFocusable = true
        val timer = Timer()
        val startTime = Instant.now().toEpochMilli()
        class Task(): TimerTask() {
            override fun run() {
                val deltaTime = Instant.now().toEpochMilli() - currentTime - startTime
                currentTime = Instant.now().toEpochMilli() - startTime
                update(deltaTime)
            }
        }
        timer.schedule(Task(), 0, 17)
    }

    private fun update(deltaTime: Long) {
        if (song.peekNote() != null && currentTime + fadeTime >= song.peekNote()!!.time) {
            fadeInNotes.add(song.popNote()!!)
        }
        if (fadeInNotes.peek() != null && currentTime >= fadeInNotes.peek().time) {
            fadeOutNotes.add(fadeInNotes.poll())
        }
        if (fadeOutNotes.peek() != null && currentTime - fadeTime >= fadeOutNotes.peek().time) {
            fadeOutNotes.poll()
        }

        val noteNotInActiveNotes = (activeNotes.isEmpty() || !activeNotes.contains(fadeInNotes.peek()))
        if (fadeInNotes.peek() != null && noteNotInActiveNotes && currentTime + timingWindow >= fadeInNotes.peek().time) {
            activeNotes.add(fadeInNotes.peek())
        }

        val noteInActiveNotes = activeNotes.contains(fadeOutNotes.peek())
        if (fadeOutNotes.peek() != null && !activeNotes.isEmpty() && noteInActiveNotes && currentTime - timingWindow >= fadeOutNotes.peek().time) {
            activeNotes.poll()
        }

        repaint()
    }

    override fun paint(g: Graphics) {
        super.paint(g)

        //Squares
        g.color = Color.BLACK
        for (i in 1..9) {
            val vertexOfBox = getPositionOfNoteValue(i)
            val size = Math.min(width / 3, height / 3)
            g.drawRect(vertexOfBox.x, vertexOfBox.y, size, size)
            g.font = Font(Font.SANS_SERIF, Font.BOLD, 20)
            g.drawString(i.toString(), vertexOfBox.x + size / 2, vertexOfBox.y + size / 2)
        }

        //Fading in notes
        for (noteGroup in fadeInNotes) {
            val diff = currentTime - noteGroup.time + fadeTime
            val scale = diff.toFloat() / fadeTime.toFloat()
            val maxSize = Math.min(width / 3, height / 3)
            for (note in noteGroup.notes) {
                val vertexOfBox = getPositionOfNoteValue(note.value)
                val centerVector = Vector(maxSize / 2, maxSize / 2)
                val halfSideLength = centerVector * scale
                val vertexOfNote = vertexOfBox + centerVector - halfSideLength
                g.color = getColorOfNote(note, 1 - scale)
                g.fillRoundRect(vertexOfNote.x, vertexOfNote.y, halfSideLength.x * 2, halfSideLength.y * 2, halfSideLength.x / 2, halfSideLength.y / 2)
            }
        }

        //Fading out notes
        for (noteGroup in fadeOutNotes) {
            val diff = currentTime - noteGroup.time
            val scale = diff.toFloat() / fadeTime.toFloat()
            val maxSize = Math.min(width / 3, height / 3)
            for (note in noteGroup.notes) {
                val vertexOfBox = getPositionOfNoteValue(note.value)
                g.color = getColorOfNote(note, scale)
                g.fillRoundRect(vertexOfBox.x, vertexOfBox.y, maxSize, maxSize, maxSize / 4, maxSize / 4)
            }
        }
    }

    private fun getColorOfNote(note: Note, scale: Float): Color {
        val alpha = 1 - Math.min(1f, scale)
        val notHit = Color(0.9f, 0.3f, 0.2f, alpha)
        val hit = Color(0.2f, 0.1f, 0.9f, alpha)
        return if (note.isHit) {
            hit
        } else {
            notHit
        }
    }

    private fun getPositionOfNoteValue(value: Int): Vector {
        val size = Math.min(width / 3, height / 3)
        return when(value) {
            1 -> Vector(0, size * 2)
            2 -> Vector(size, size * 2)
            3 -> Vector(size * 2, size * 2)
            4 -> Vector(0, size)
            5 -> Vector(size, size)
            6 -> Vector(size * 2, size)
            7 -> Vector(0, 0)
            8 -> Vector(size, 0)
            9 -> Vector(size * 2, 0)
            else -> Vector(0, 0)
        }
    }
}