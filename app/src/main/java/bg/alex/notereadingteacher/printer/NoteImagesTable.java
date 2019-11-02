package bg.alex.notereadingteacher.printer;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NoteImagesTable {
    private static final String TAG = "AdvancedNotesPrinter";

    private static Map<Note, String> noteImagesF = new HashMap<>();
    private static Map<Note, String> noteImagesG = new HashMap<>();

    static {
        noteImagesF.put(new Note(NotePitch.A, 1), "note_01");
        noteImagesF.put(new Note(NotePitch.B, 1), "note_02");
        noteImagesF.put(new Note(NotePitch.C, 2), "note_03");
        noteImagesF.put(new Note(NotePitch.D, 2), "note_04");
        noteImagesF.put(new Note(NotePitch.E, 2), "note_05");
        noteImagesF.put(new Note(NotePitch.F, 2), "note_06");
        noteImagesF.put(new Note(NotePitch.G, 2), "note_07");
        noteImagesF.put(new Note(NotePitch.A, 2), "note_08");
        noteImagesF.put(new Note(NotePitch.B, 2), "note_09");
        noteImagesF.put(new Note(NotePitch.C, 3), "note_10");
        noteImagesF.put(new Note(NotePitch.D, 3), "note_11");
        noteImagesF.put(new Note(NotePitch.E, 3), "note_12");
        noteImagesF.put(new Note(NotePitch.F, 3), "note_13");
        noteImagesF.put(new Note(NotePitch.G, 3), "note_14");
        noteImagesF.put(new Note(NotePitch.A, 3), "note_15");
        noteImagesF.put(new Note(NotePitch.B, 3), "note_16");
        noteImagesF.put(new Note(NotePitch.C, 4), "note_17");
        noteImagesF.put(new Note(NotePitch.D, 4), "note_18");
        noteImagesF.put(new Note(NotePitch.E, 4), "note_19");
        noteImagesF.put(new Note(NotePitch.F, 4), "note_20");
        noteImagesF.put(new Note(NotePitch.G, 4), "note_21");
        noteImagesF.put(new Note(NotePitch.A, 4), "note_22");

        noteImagesG.put(new Note(NotePitch.F, 3), "note_01");
        noteImagesG.put(new Note(NotePitch.G, 3), "note_02");
        noteImagesG.put(new Note(NotePitch.A, 3), "note_03");
        noteImagesG.put(new Note(NotePitch.B, 3), "note_04");
        noteImagesG.put(new Note(NotePitch.C, 4), "note_05");
        noteImagesG.put(new Note(NotePitch.D, 4), "note_06");
        noteImagesG.put(new Note(NotePitch.E, 4), "note_07");
        noteImagesG.put(new Note(NotePitch.F, 4), "note_08");
        noteImagesG.put(new Note(NotePitch.G, 4), "note_09");
        noteImagesG.put(new Note(NotePitch.A, 4), "note_10");
        noteImagesG.put(new Note(NotePitch.B, 4), "note_11");
        noteImagesG.put(new Note(NotePitch.C, 5), "note_12");
        noteImagesG.put(new Note(NotePitch.D, 5), "note_13");
        noteImagesG.put(new Note(NotePitch.E, 5), "note_14");
        noteImagesG.put(new Note(NotePitch.F, 5), "note_15");
        noteImagesG.put(new Note(NotePitch.G, 5), "note_16");
        noteImagesG.put(new Note(NotePitch.A, 5), "note_17");
        noteImagesG.put(new Note(NotePitch.B, 5), "note_18");
        noteImagesG.put(new Note(NotePitch.C, 6), "note_19");
        noteImagesG.put(new Note(NotePitch.D, 6), "note_20");
        noteImagesG.put(new Note(NotePitch.E, 6), "note_21");
        noteImagesG.put(new Note(NotePitch.F, 6), "note_22");
    }

    public static String getImageNameForNote(Note note, Clef clef) {
        String noteImage;

        if(note.isSharp()) {
            note = note.previousWholeNote();
        }

        Note baseNote;
        Note maxNote;

        if (clef == Clef.F) {
            baseNote = new Note(NotePitch.A, 1);
            maxNote = new Note(NotePitch.A, 4);

            noteImage = noteImagesF.get(note);
        } else if (clef == Clef.G) {
            baseNote = new Note(NotePitch.F, 3);
            maxNote = new Note(NotePitch.F, 6);

            noteImage = noteImagesG.get(note);
        } else {
            throw new RuntimeException("Unsupported clef " + clef);
        }

        if(baseNote.isGreaterThan(note) || note.isGreaterThan(maxNote)) {
            noteImage = "note_empty";
            Log.i(TAG, "Resolved empty note image : "+ noteImage + " for note " + note);
            return noteImage;
        }

        Log.i(TAG, "Resolved note image : "+ noteImage + " for note " + note);

        return noteImage;
    }
}
