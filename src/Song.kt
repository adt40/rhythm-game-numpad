import java.util.*

class Song(val name: String, bpm: Int, val msOffset: Long, noteGroupList: List<NoteGroup>) {

    val msPerBeat: Long = 60000 / bpm.toLong()
    private val noteQueue = LinkedList<NoteGroup>()

    init {
        var currentTime = msOffset
        for (note in noteGroupList) {
            note.time = currentTime
            noteQueue.add(note)
            currentTime += msPerBeat * note.div / note.notesPerDiv
        }
        println(msPerBeat)
    }

    fun popNote(): NoteGroup? {
        return noteQueue.poll()
    }

    fun peekNote(): NoteGroup? {
        return noteQueue.peek()
    }
}