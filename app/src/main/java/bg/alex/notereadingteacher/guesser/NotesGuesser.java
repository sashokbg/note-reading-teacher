package bg.alex.notereadingteacher.guesser;

import android.util.Log;

import java.util.Random;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NotesGuesser {
    private static final String TAG = "NotesGuesser";
    private Random random;

    public NotesGuesser(Random random) {
        this.random = random;
    }

    public NotesGuesser(){
        random = new Random();
    }

    public NoteGuess randomNote() {
        int pitchCode = 0;

        while(pitchCode < 36){
            pitchCode = random.nextInt(73);
        }

        if(pitchCode > 72){
            throw new RuntimeException("This random does not work well !");
        }

        Note note = new Note(pitchCode);

        if(note.isSharp()){
            note = new Note(NotePitch.fromCode(note.getNotePitch().getPitchCode()-1), note.getOctave());
        }

        Clef clef;

        if(note.getOctave() > 3){
            clef = Clef.G;
        } else {
            clef = Clef.F;
        }

        return new NoteGuess(note, clef);
    }
}
