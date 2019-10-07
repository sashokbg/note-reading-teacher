package bg.alex.notereadingteacher.guesser;

import android.util.Log;

import java.util.Random;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NotesGuesser {
    private static final String TAG = "NotesGuesser";
    private Random random;
    private Note minNote;
    private Note maxNote;
    private Clef clef;

    NotesGuesser(Clef clef, Random random) {
        this.random = random;
        this.clef = clef;
        setNotesRange(clef);
    }

    public NotesGuesser(Clef clef){
        this.clef = clef;

        setNotesRange(clef);
        random = new Random();
    }

    private void setNotesRange(Clef clef) {
        if(clef.equals(Clef.F)){
            minNote = new Note(NotePitch.E, 2);
            maxNote = new Note(NotePitch.E, 4);
        } else if(clef.equals(Clef.G)) {
            minNote = new Note(NotePitch.A, 3);
            maxNote = new Note(NotePitch.C, 6);
        }
    }

    public NoteGuess randomNote() {
        int pitchCode = 0;

        Note note = new Note(pitchCode);

        while (minNote.isGreaterThan(note)){
            pitchCode = random.nextInt(maxNote.getAbsolutePitch());
            note = new Note(pitchCode);
        }

        if(note.isGreaterThan(maxNote)) {
            Log.e(TAG, "A max note higher than the expected has been generated !");
            note = maxNote;
        }

        if(note.isSharp()){
            note = new Note(NotePitch.fromCode(note.getNotePitch().getPitchCode()+1), note.getOctave());
        }

        return new NoteGuess(note, this.clef);
    }
}
