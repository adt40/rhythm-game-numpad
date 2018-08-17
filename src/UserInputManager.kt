import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*

class UserInputManager(val activeNoteGroups: LinkedList<NoteGroup>): KeyListener {

    private val pressedKeys = Array(9, { _ -> false })

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            val key = keyCodeToInt(e.keyCode)
            if (key != null && !pressedKeys[key - 1]) {
                pressedKeys[key - 1] = true
                for (noteGroup in activeNoteGroups) {
                    for (note in noteGroup.notes) {
                        if (key == note.value) {
                            note.isHit = true
                            break
                        }
                    }
                }
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        if (e != null) {
            val key = keyCodeToInt(e.keyCode)
            if (key != null) {
                pressedKeys[key - 1] = false
            }
        }
    }

    private fun keyCodeToInt(keyCode: Int): Int? {
        return when(keyCode) {
            KeyEvent.VK_NUMPAD1 -> 1
            KeyEvent.VK_NUMPAD2 -> 2
            KeyEvent.VK_NUMPAD3 -> 3
            KeyEvent.VK_NUMPAD4 -> 4
            KeyEvent.VK_NUMPAD5 -> 5
            KeyEvent.VK_NUMPAD6 -> 6
            KeyEvent.VK_NUMPAD7 -> 7
            KeyEvent.VK_NUMPAD8 -> 8
            KeyEvent.VK_NUMPAD9 -> 9
            else -> null
        }
    }
}