package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class AdvancedNotesPrinter implements NotesPrinter {
    private Activity activity;
    private Clef clef;
    private NumberFormat formatter;

    public AdvancedNotesPrinter(Clef clef, Activity activity) {
        this.activity = activity;
        this.clef = clef;
        this.formatter = new DecimalFormat("00");
    }

    public void setClef(Clef clef) {
        this.clef = clef;
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

    public void applyNoteImageTo(ImageView noteView, NoteGuess noteGuess) {
        Note note = noteGuess.getNote();

        Class<R.drawable> clazz1 = R.drawable.class;
        Class<ImageView> clazz2 = ImageView.class;

        try {

            Field field = clazz1.getDeclaredField(getImageNameForNote(note, clef));
            Method method = clazz2.getDeclaredMethod("setImageResource", int.class);

            method.invoke(noteView, field.get(noteView));
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printNoteGuesses(final List<NoteGuess> noteGuesses) {
        activity.runOnUiThread(() -> {
            ImageView previousNoteView = null;
            ConstraintLayout constraintLayout = activity.findViewById(R.id.notes_layout);

            View noteToRemove;
            do {
                noteToRemove = constraintLayout.findViewWithTag("note");
                constraintLayout.removeView(noteToRemove);
            } while (noteToRemove != null);


            for (int i = 0; i < noteGuesses.size(); i++) {
                NoteGuess noteGuess = noteGuesses.get(i);
                
                ImageView noteView = new ImageView(activity);
                noteView.setId(View.generateViewId());
                noteView.setTag("note");

                if(i%4 == 0) {
                    noteView.setPadding(100, 0, 0, 0);
                } else {
                    noteView.setPadding(4, 0, 0, 0);
                }
                noteView.setAdjustViewBounds(true);

                ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                        new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

                noteView.setLayoutParams(constraintLayoutParams);
                constraintLayout.addView(noteView);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.connect(noteView.getId(), ConstraintSet.TOP, R.id.staff, ConstraintSet.TOP);
                constraintSet.connect(noteView.getId(), ConstraintSet.BOTTOM, R.id.staff, ConstraintSet.BOTTOM);
                if(i == 0) {
                    constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                    constraintSet.connect(noteView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
                    constraintSet.setHorizontalBias(noteView.getId(), 0.15f);
                } else {
                    constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, previousNoteView.getId(), ConstraintSet.RIGHT);
                }

                constraintSet.applyTo(constraintLayout);

                applyNoteImageTo(noteView, noteGuess);

                previousNoteView = noteView;
            }
        });
    }
}
