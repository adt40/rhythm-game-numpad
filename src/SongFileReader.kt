import java.io.BufferedReader
import java.io.FileReader

class SongFileReader {
    fun readSongFile(fileName: String, ignoreDiv: Boolean): Song {
        val br = BufferedReader(FileReader(fileName))

        val songInfo = br.readLine()
        val (name, bpm, offset) = songInfo.split(' ')

        val noteList = ArrayList<NoteGroup>()
        br.lines().forEach {
            if (ignoreDiv) {
                val (notesPerDiv, noteValueString) = it.split(' ')
                val div = 1
                addToNoteList(noteValueString, notesPerDiv, div, noteList)
            } else {
                val (notesPerDiv, div, noteValueString) = it.split(' ')
                addToNoteList(noteValueString, notesPerDiv, div.toInt(), noteList)
            }
        }

        br.close()

        return Song(name, bpm.toInt(), offset.toLong(), noteList)
    }

    private fun addToNoteList(noteValueString: String, notesPerDiv: String, div: Int, noteList: ArrayList<NoteGroup>) {
        val noteValues = noteValueString.map(Character::getNumericValue)
        val notes = noteValues.map { Note(it) }
        val note = NoteGroup(notesPerDiv.toInt(), div, notes)
        noteList.add(note)
    }
}