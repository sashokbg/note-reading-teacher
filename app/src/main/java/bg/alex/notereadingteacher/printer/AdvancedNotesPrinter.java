package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class AdvancedNotesPrinter implements NotesPrinter {
    private TextView debug;
    private ImageView currentNoteView;
    private Activity activity;
    private Clef clef;
    private NumberFormat formatter;

    public AdvancedNotesPrinter(Clef clef, Activity activity) {
        this.debug = activity.findViewById(R.id.note_debug);
        this.activity = activity;
        this.clef = clef;
        this.formatter = new DecimalFormat("00");
    }

    private String getImageNameForNote(Note note, Clef clef) {
        Note baseNote;

        if (clef == Clef.F) {
            baseNote = new Note(NotePitch.A, 1);
        } else if (clef == Clef.G){
            baseNote = new Note(NotePitch.F, 3);
        } else {
            throw new RuntimeException("Unsupported clef " + clef);
        }

        int noteCounter = 1;

        while (!note.equals(baseNote)) {
            noteCounter++;
            baseNote = baseNote.nextWholeNote();
        }

        return "note_" + formatter.format(noteCounter);
    }

    @Override
    public void printNoteGuess(final NoteGuess noteGuess) {
        activity.runOnUiThread(new Runnable() {

            Note note = noteGuess.getNote();

            @Override
            public void run() {
                currentNoteView = activity.findViewById(R.id.current_note);
                debug.setText(note.toString());

                Class<R.drawable> clazz1 = R.drawable.class;
                Class<ImageView> clazz2 = ImageView.class;

                try {

                    Field field = clazz1.getDeclaredField(getImageNameForNote(note, clef));
                    Method method = clazz2.getDeclaredMethod("setImageResource", int.class);

                    method.invoke(currentNoteView, field.get(currentNoteView));
                } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
