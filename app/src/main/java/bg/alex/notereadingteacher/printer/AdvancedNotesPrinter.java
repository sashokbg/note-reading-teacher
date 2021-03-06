package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;

import static bg.alex.notereadingteacher.printer.NoteImagesTable.getImageNameForNote;

public class AdvancedNotesPrinter implements NotesPrinter {
    private static final String TAG = "AdvancedNotesPrinter";

    private Activity activity;
    private Clef clef;

    private View indicator;
    private ConstraintLayout constraintLayout;
    private List<ImageView> notesToGuess;

    public AdvancedNotesPrinter(Clef clef, Activity activity, ConstraintLayout constraintLayout) {
        this.activity = activity;
        this.clef = clef;
        this.constraintLayout = constraintLayout;
        this.notesToGuess = new ArrayList<>();
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }

    public void applyNoteImageTo(ImageView noteView, NoteGuess noteGuess) {
        Note note = noteGuess.getNote();

        if(!this.clef.equals(noteGuess.getClef()) && !noteGuess.isMistake()) {
            noteView.setImageResource(R.drawable.note_empty);
        } else {
            noteView.setImageResource(getImageNameForNote(note, clef));
        }

    }

    @Override
    public void removeMistakes() {
        activity.runOnUiThread(() -> {
            ImageView noteToRemove;

            while ((noteToRemove = constraintLayout.findViewWithTag("note-mistake")) != null ){
                constraintLayout.removeView(noteToRemove);
            }
        });
    }

    @Override
    public void printMistake(NoteGuess noteGuess, int currentNoteGuess) {
        ImageView mistakeNote = new ImageView(activity);

        mistakeNote.setImageAlpha(128);
        ImageView currentNoteView = notesToGuess.get(currentNoteGuess);

        mistakeNote.setId(View.generateViewId());
        mistakeNote.setTag("note-mistake");

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

        applyNoteImageTo(mistakeNote, noteGuess);

        activity.runOnUiThread(() -> {
            constraintLayout.addView(mistakeNote);
            constraintSet.applyTo(constraintLayout);
        });
    }

    @Override
    public void printNoteIndicator(int noteGuess) {
        activity.runOnUiThread(() -> {

            indicator = constraintLayout.findViewWithTag("indicator");
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
            Log.i(TAG, "Starting printing note guesses");
            ImageView previousNoteView = null;
            ImageView divider = null;
            notesToGuess = new ArrayList<>();

            View noteToRemove, dividerToRemove;
            do {
                noteToRemove = constraintLayout.findViewWithTag("note" + this.clef);
                dividerToRemove = constraintLayout.findViewWithTag("divider");
                Log.i(TAG, "Removing note " + noteToRemove);
                constraintLayout.removeView(noteToRemove);
                constraintLayout.removeView(dividerToRemove);
            } while (noteToRemove != null);

            for (int i = 0; i < noteGuesses.size(); i++) {

                if(i%4 == 0 && (i != 0)) {
                    divider = new ImageView(activity);
                    divider.setId(View.generateViewId());
                    divider.setImageResource(R.drawable.divider);
                    divider.setTag("divider");

                    ConstraintLayout.LayoutParams csp = new ConstraintLayout.LayoutParams(
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    0));

                    divider.setLayoutParams(csp);
                    constraintLayout.addView(divider);
                }

                NoteGuess noteGuess = noteGuesses.get(i);

                ImageView noteView = new ImageView(activity);
                noteView.setId(View.generateViewId());
                noteView.setTag("note" + this.clef);
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
                    constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, R.id.key, ConstraintSet.RIGHT);
                } else {
                    if (i % 4 == 0) {
                        constraintSet.connect(divider.getId(), ConstraintSet.TOP, R.id.staff, ConstraintSet.TOP);
                        constraintSet.connect(divider.getId(), ConstraintSet.BOTTOM, R.id.staff, ConstraintSet.BOTTOM);

                        constraintSet.connect(divider.getId(), ConstraintSet.LEFT, previousNoteView.getId(), ConstraintSet.RIGHT);

                        constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, divider.getId(), ConstraintSet.RIGHT);
                    } else {
                        constraintSet.connect(noteView.getId(), ConstraintSet.LEFT, previousNoteView.getId(), ConstraintSet.RIGHT);
                    }

                }

                constraintSet.applyTo(constraintLayout);
                applyNoteImageTo(noteView, noteGuess);
                previousNoteView = noteView;
            }

            Log.i(TAG, "End printing note guesses");
        });
    }
}
