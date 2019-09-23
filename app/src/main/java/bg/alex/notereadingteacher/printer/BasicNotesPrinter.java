package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class BasicNotesPrinter implements NotesPrinter {
    private static final int NOTE_HEIGHT = 40;
    private static final int NOTE_WIDTH = 67;
    private static int OFFSET = 260;

    private TextView debug;
    private ImageView noteImage;
    private FrameLayout staff;
    Activity activity;

    public BasicNotesPrinter(Activity activity) {
        this.staff = ((FrameLayout) activity.findViewById(R.id.staff_container));
        this.debug = ((TextView) activity.findViewById(R.id.notes));
        this.activity = activity;
    }

    public void printNoteGuess(final NoteGuess noteGuess) {
        activity.runOnUiThread(new Runnable() {
            Note note = noteGuess.getNote();
            Clef clef = noteGuess.getClef();

            @Override
            public void run() {
                debug.setText(note.toString());

                if (noteImage == null) {
                    noteImage = new ImageView(activity);
                    noteImage.setX(400);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(NOTE_WIDTH, NOTE_HEIGHT);
                    noteImage.setLayoutParams(params);


                    staff.addView(noteImage);
                }

                int octaveHeight = NOTE_HEIGHT / 2 * 7;

                noteImage.setImageResource(R.drawable.note);

                if (clef == Clef.G) {
                    OFFSET = 260;
                    if(note.compareTo(new Note(NotePitch.G, 5)) > 0){
                        noteImage.setImageResource(R.drawable.note_barred);
                    }

                } else if (clef == Clef.F) {
                    OFFSET = 380;
                }
                noteImage.setY(OFFSET - ((note.getPosition() * (NOTE_HEIGHT / 2)) + (note.getOctave() - 4) * octaveHeight));
            }
        });
    }
}
