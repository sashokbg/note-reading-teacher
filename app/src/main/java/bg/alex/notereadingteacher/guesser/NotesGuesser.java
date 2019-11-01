package bg.alex.notereadingteacher.guesser;

import android.util.Log;

import java.util.Random;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NotesGuesser {
    public static final int MAX_NUMBER_OF_NOTES = 8;
    private static final String TAG = "NotesGuesser";
    private Random random;
    private Note minNote;
    private Note maxNote;
    private Clef clef;
    public static Note MIN_NOTE_F = new Note(NotePitch.E, 2);
    public static Note MAX_NOTE_F = new Note(NotePitch.E, 4);
    public static Note MIN_NOTE_G = new Note(NotePitch.A, 3);
    public static Note MAX_NOTE_G = new Note(NotePitch.C, 6);

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
            minNote = MIN_NOTE_F;
            maxNote = MAX_NOTE_F;
        } else if(clef.equals(Clef.G)) {
            minNote = MIN_NOTE_G;
            maxNote = MAX_NOTE_G;
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
