class NoteGroup(notesPerDiv: Int, div: Int, notes: List<Note>) {
    val notes: List<Note>
    val notesPerDiv: Int
    val div: Int

    var time: Long = 0

    init {
        if (notes.stream().allMatch { it.value in 1..9 }) {
            this.notes = notes
        } else {
            throw IndexOutOfBoundsException("All values must be between 1 and 9")
        }

        if (notesPerDiv > 0) {
            this.notesPerDiv = notesPerDiv
        } else {
            throw IndexOutOfBoundsException("Notes per div cannot be 0")
        }

        if (div > 0) {
            this.div = div
        } else {
            throw  IndexOutOfBoundsException("Div cannot be 0")
        }
    }

    override fun toString(): String {
        return "NoteGroup(notes=${notes.toString()}, notesPerDiv=$notesPerDiv, div=$div, time=$time)"
    }


}