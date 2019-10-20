package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.guesser.NotesGuesser;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;

public class AdvancedNotesPrinter implements NotesPrinter {
    private Activity activity;
    private Clef clef;
    private NumberFormat formatter;
    private View indicator;
    private ConstraintLayout constraintLayout;
    private List<ImageView> notesToGuess;

    public AdvancedNotesPrinter(Clef clef, Activity activity) {
        this.activity = activity;
        this.clef = clef;
        this.formatter = new DecimalFormat("00");
        this.constraintLayout = activity.findViewById(R.id.notes_layout);
        this.notesToGuess = new ArrayList<>();
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }

    private String getImageNameForNote(Note note, Clef clef) {
        if(note.isSharp()) {
            note = note.previousWholeNote();
        }


        Note baseNote;
        Note maxNote;

        if (clef == Clef.F) {
            baseNote = NotesGuesser.MIN_NOTE_F;
            maxNote = NotesGuesser.MAX_NOTE_F;
        } else if (clef == Clef.G) {
            baseNote = NotesGuesser.MIN_NOTE_G;
            maxNote = NotesGuesser.MAX_NOTE_G;
        } else {
            throw new RuntimeException("Unsupported clef " + clef);
        }

        if(baseNote.isGreaterThan(note)) {
            note = baseNote;
        }

        if(note.isGreaterThan(maxNote)) {
            note = maxNote;
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
    public void removeMistakes() {
        activity.runOnUiThread(() -> {
            ImageView noteToRemove;
            do {
                noteToRemove = constraintLayout.findViewWithTag("note-mistake");
                TransitionManager.beginDelayedTransition(constraintLayout);
                constraintLayout.removeView(noteToRemove);
            } while (noteToRemove != null);
        });
    }

    @Override
    public void printMistake(NoteGuess noteGuess, int currentNoteGuess) {
        activity.runOnUiThread(() -> {
            ImageView mistakeNote = new ImageView(activity);
            mistakeNote.setImageAlpha(128);
            ImageView currentNoteView = notesToGuess.get(currentNoteGuess);

            mistakeNote.setId(View.generateViewId());
            mistakeNote.setTag("note-mistake");

            constraintLayout.addView(mistakeNote);

            mistakeNote.setAdjustViewBounds(true);

            ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            0));

            mistakeNote.setLayoutParams(constraintLayoutParams);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            mistakeNote.setPadding(4, 0, 0, 0);

            constraintSet.connect(mistakeNote.getId(), ConstraintSet.TOP, R.id.staff, ConstraintSet.TOP);
            constraintSet.connect(mistakeNote.getId(), ConstraintSet.BOTTOM, R.id.staff, ConstraintSet.BOTTOM);
            constraintSet.connect(mistakeNote.getId(), ConstraintSet.LEFT, currentNoteView.getId(), ConstraintSet.LEFT);
            constraintSet.connect(mistakeNote.getId(), ConstraintSet.RIGHT, currentNoteView.getId(), ConstraintSet.RIGHT);

            constraintSet.applyTo(constraintLayout);
            applyNoteImageTo(mistakeNote, noteGuess);
        });
    }

    @Override
    public void printNoteIndicator(int noteGuess) {
        activity.runOnUiThread(() -> {
            TransitionManager.beginDelayedTransition(constraintLayout);

            indicator = activity.findViewById(R.id.indicator);
            ImageView currentNoteView = notesToGuess.get(noteGuess);

            ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                    indicator.getLayoutParams());

            indicator.setLayoutParams(constraintLayoutParams);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(indicator.getId(), ConstraintSet.TOP, currentNoteView.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(indicator.getId(), ConstraintSet.LEFT, currentNoteView.getId(), ConstraintSet.LEFT);
            constraintSet.connect(indicator.getId(), ConstraintSet.RIGHT, currentNoteView.getId(), ConstraintSet.RIGHT);

            constraintSet.applyTo(constraintLayout);
        });
    }

    @Override
    public void printNoteGuesses(final List<NoteGuess> noteGuesses) {
        activity.runOnUiThread(() -> {
            ImageView previousNoteView = null;
            notesToGuess = new ArrayList<>();

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
                notesToGuess.add(noteView);

                noteView.setAdjustViewBounds(true);

                ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                0));

                noteView.setLayoutParams(constraintLayoutParams);
                constraintLayout.addView(noteView);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                noteView.setPadding(4, 0, 0, 0);

                constraintSet.connect(noteView.getId(), ConstraintSet.TOP, R.id.staff, ConstraintSet.TOP);
                constraintSet.connect(noteView.getId(), ConstraintSet.BOTTOM, R.id.staff, ConstraintSet.BOTTOM);
                if (i == 0) {
                    constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, R.id.staff, ConstraintSet.LEFT);
                    constraintSet.connect(noteView.getId(), ConstraintSet.RIGHT, R.id.staff, ConstraintSet.RIGHT);
                    constraintSet.setHorizontalBias(noteView.getId(), 0.22f);
                } else {
                    if (i % 4 == 0) {
                        constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, R.id.line_separator, ConstraintSet.RIGHT);
                    } else {
                        constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, previousNoteView.getId(), ConstraintSet.RIGHT);
                    }

                }

                constraintSet.applyTo(constraintLayout);
                applyNoteImageTo(noteView, noteGuess);
                previousNoteView = noteView;
            }
        });
    }
}
