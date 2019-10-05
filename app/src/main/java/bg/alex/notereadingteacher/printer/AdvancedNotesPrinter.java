package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Note;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AdvancedNotesPrinter implements NotesPrinter {
    private static int OFFSET = 260;

    private TextView debug;
    private ImageView currentNote;
    private ConstraintLayout staff;
    private Activity activity;

    public AdvancedNotesPrinter(Activity activity) {
        this.staff = ((ConstraintLayout) activity.findViewById(R.id.notes_layout));
        this.debug = ((TextView) activity.findViewById(R.id.note_debug));
        this.activity = activity;
    }

    @Override
    public void printNoteGuess(final NoteGuess noteGuess) {
        activity.runOnUiThread(new Runnable() {

            Note note = noteGuess.getNote();

            @Override
            public void run() {
                currentNote = (ImageView) activity.findViewById(R.id.current_note);
                debug.setText(note.toString());

                Class<R.drawable> clazz1 = R.drawable.class;
                Class<ImageView> clazz2 = ImageView.class;

                try {
                    Field field = clazz1.getDeclaredField(note.getNotePitch().getLabel().toLowerCase() + note.getOctave());
                    Method method = clazz2.getDeclaredMethod("setImageResource", int.class);

                    method.invoke(currentNote, field.get(currentNote));
                } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
