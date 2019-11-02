package bg.alex.notereadingteacher.printer;

import android.support.annotation.DrawableRes;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NoteImagesTable {
    private static final String TAG = "AdvancedNotesPrinter";

    private static Map<Note, Integer> noteImagesF = new HashMap<>();
    private static Map<Note, Integer> noteImagesG = new HashMap<>();

    static {
        noteImagesF.put(new Note(NotePitch.A, 1), R.drawable.note_01);
        noteImagesF.put(new Note(NotePitch.B, 1), R.drawable.note_02);
        noteImagesF.put(new Note(NotePitch.C, 2), R.drawable.note_03);
        noteImagesF.put(new Note(NotePitch.D, 2), R.drawable.note_04);
        noteImagesF.put(new Note(NotePitch.E, 2), R.drawable.note_05);
        noteImagesF.put(new Note(NotePitch.F, 2), R.drawable.note_06);
        noteImagesF.put(new Note(NotePitch.G, 2), R.drawable.note_07);
        noteImagesF.put(new Note(NotePitch.A, 2), R.drawable.note_08);
        noteImagesF.put(new Note(NotePitch.B, 2), R.drawable.note_09);
        noteImagesF.put(new Note(NotePitch.C, 3), R.drawable.note_10);
        noteImagesF.put(new Note(NotePitch.D, 3), R.drawable.note_11);
        noteImagesF.put(new Note(NotePitch.E, 3), R.drawable.note_12);
        noteImagesF.put(new Note(NotePitch.F, 3), R.drawable.note_13);
        noteImagesF.put(new Note(NotePitch.G, 3), R.drawable.note_14);
        noteImagesF.put(new Note(NotePitch.A, 3), R.drawable.note_15);
        noteImagesF.put(new Note(NotePitch.B, 3), R.drawable.note_16);
        noteImagesF.put(new Note(NotePitch.C, 4), R.drawable.note_17);
        noteImagesF.put(new Note(NotePitch.D, 4), R.drawable.note_18);
        noteImagesF.put(new Note(NotePitch.E, 4), R.drawable.note_19);
        noteImagesF.put(new Note(NotePitch.F, 4), R.drawable.note_20);
        noteImagesF.put(new Note(NotePitch.G, 4), R.drawable.note_21);
        noteImagesF.put(new Note(NotePitch.A, 4), R.drawable.note_22);

        noteImagesG.put(new Note(NotePitch.F, 3), R.drawable.note_01);
        noteImagesG.put(new Note(NotePitch.G, 3), R.drawable.note_02);
        noteImagesG.put(new Note(NotePitch.A, 3), R.drawable.note_03);
        noteImagesG.put(new Note(NotePitch.B, 3), R.drawable.note_04);
        noteImagesG.put(new Note(NotePitch.C, 4), R.drawable.note_05);
        noteImagesG.put(new Note(NotePitch.D, 4), R.drawable.note_06);
        noteImagesG.put(new Note(NotePitch.E, 4), R.drawable.note_07);
        noteImagesG.put(new Note(NotePitch.F, 4), R.drawable.note_08);
        noteImagesG.put(new Note(NotePitch.G, 4), R.drawable.note_09);
        noteImagesG.put(new Note(NotePitch.A, 4), R.drawable.note_10);
        noteImagesG.put(new Note(NotePitch.B, 4), R.drawable.note_11);
        noteImagesG.put(new Note(NotePitch.C, 5), R.drawable.note_12);
        noteImagesG.put(new Note(NotePitch.D, 5), R.drawable.note_13);
        noteImagesG.put(new Note(NotePitch.E, 5), R.drawable.note_14);
        noteImagesG.put(new Note(NotePitch.F, 5), R.drawable.note_15);
        noteImagesG.put(new Note(NotePitch.G, 5), R.drawable.note_16);
        noteImagesG.put(new Note(NotePitch.A, 5), R.drawable.note_17);
        noteImagesG.put(new Note(NotePitch.B, 5), R.drawable.note_18);
        noteImagesG.put(new Note(NotePitch.C, 6), R.drawable.note_19);
        noteImagesG.put(new Note(NotePitch.D, 6), R.drawable.note_20);
        noteImagesG.put(new Note(NotePitch.E, 6), R.drawable.note_21);
        noteImagesG.put(new Note(NotePitch.F, 6), R.drawable.note_22);
    }

    static @DrawableRes Integer getImageNameForNote(Note note, Clef clef) {
        Integer noteImage;

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
            noteImage = R.drawable.note_empty;
            Log.i(TAG, "Resolved empty note image : "+ noteImage + " for note " + note);
            return noteImage;
        }

        Log.i(TAG, "Resolved note image : "+ noteImage + " for note " + note);

        return noteImage;
    }
}
