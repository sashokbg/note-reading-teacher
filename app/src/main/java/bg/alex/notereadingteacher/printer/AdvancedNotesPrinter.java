package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Note;

public class AdvancedNotesPrinter implements NotesPrinter {
    private TextView debug;
    private ImageView currentNote;
    private Activity activity;

    public AdvancedNotesPrinter(Activity activity) {
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
