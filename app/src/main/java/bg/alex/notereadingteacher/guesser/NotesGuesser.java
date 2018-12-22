package bg.alex.notereadingteacher.guesser;

import android.util.Log;

import java.util.Random;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;

public class NotesGuesser {
    private static final String TAG = "NotesGuesser";

    public NoteGuess randomNote() {
        Random random = new Random();

        int pitchCode = 0;

        while(pitchCode < 36){
            pitchCode = random.nextInt(73);
        }

        Note note = new Note(pitchCode);
        Log.i(TAG,"Generated random note: "+ note);

        Clef clef;

        if(note.getOctave() > 3){
            clef = Clef.G;
        } else {
            clef = Clef.F;
        }

        return new NoteGuess(note, clef);
    }
}
