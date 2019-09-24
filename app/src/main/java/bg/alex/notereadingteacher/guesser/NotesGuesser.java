package bg.alex.notereadingteacher.guesser;

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


    NotesGuesser(Random random) {
        this.random = random;
    }

    public NotesGuesser(Clef clef){
        this.clef = clef;

        if(clef.equals(Clef.F)){
            minNote = new Note(NotePitch.C, 2);
            maxNote = new Note(NotePitch.F, 5);
        } else if(clef.equals(Clef.G)) {
            minNote = new Note(NotePitch.F, 3);
            maxNote = new Note(NotePitch.F, 6);
        }
        random = new Random();
    }

    public NoteGuess randomNote() {
        int pitchCode = 0;

        Note note = new Note(pitchCode);

        while (minNote.isGreaterThan(note)){
            pitchCode = random.nextInt(maxNote.getAbsolutePitch());
            note = new Note(pitchCode);
        }

        if(note.isSharp()){
            note = new Note(NotePitch.fromCode(note.getNotePitch().getPitchCode()+1), note.getOctave());
        }

        return new NoteGuess(note, this.clef);
    }
}
