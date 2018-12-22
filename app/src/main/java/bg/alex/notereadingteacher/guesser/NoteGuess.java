package bg.alex.notereadingteacher.guesser;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;

public class NoteGuess {
    private Note note;
    private Clef clef;

    public NoteGuess(Note note, Clef clef) {
        this.note = note;
        this.clef = clef;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Clef getClef() {
        return clef;
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }
}
