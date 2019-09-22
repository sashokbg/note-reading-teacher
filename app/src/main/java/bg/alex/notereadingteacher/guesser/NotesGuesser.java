package bg.alex.notereadingteacher.guesser;

import java.util.Random;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NotesGuesser {
    private static final String TAG = "NotesGuesser";
    public static final int C_2_PITCH = 36;
    public static final int C_6_PITCH = 84;
    private Random random;

    public NotesGuesser(Random random) {
        this.random = random;
    }

    public NotesGuesser(){
        random = new Random();
    }

    public NoteGuess randomNote() {
        int pitchCode = 0;

        while(pitchCode < C_2_PITCH){
            pitchCode = random.nextInt(C_6_PITCH +1);
        }

        if(pitchCode > C_6_PITCH){
            throw new RuntimeException("This random does not work well !");
        }

        Note note = new Note(pitchCode);

        if(note.isSharp()){
            note = new Note(NotePitch.fromCode(note.getNotePitch().getPitchCode()-1), note.getOctave());
        }

        Clef clef;

        if(note.getOctave() > 3){
            clef = Clef.G;
        } else { //<= 3
            clef = Clef.F;
        }

        return new NoteGuess(note, clef);
    }
}
