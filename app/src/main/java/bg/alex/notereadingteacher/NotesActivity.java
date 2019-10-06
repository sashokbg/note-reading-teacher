package bg.alex.notereadingteacher;

import android.app.Activity;
import android.content.Intent;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.guesser.NotesGuesser;
import bg.alex.notereadingteacher.midi.MidiAware;
import bg.alex.notereadingteacher.midi.MidiNotesReceiver;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.printer.AdvancedNotesPrinter;
import bg.alex.notereadingteacher.printer.NotesPrinter;

public class NotesActivity extends Activity implements MidiAware {

    private static final String TAG = "NotesActivity";

    private NotesGuesser notesGuesser;
    private NoteGuess noteGuess;
    private NotesPrinter printer;
    private ImageView staff;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

        Log.i(TAG, "Starting application: ");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        staff = findViewById(R.id.staff);

        final Button nextNoteButton = findViewById(R.id.next_note);
        nextNoteButton.setOnClickListener(v -> generateRandomNote());

        Intent intent = getIntent();

        TextView gameType = findViewById(R.id.game_type);
        String intentMessage = intent.getStringExtra(HomeActivity.GAME_TYPE);
        gameType.setText(intentMessage);

        if(intentMessage.equals("Game in F")) {
            staff.setImageResource(R.drawable.staff_f);
            notesGuesser = new NotesGuesser(Clef.F);
            printer = new AdvancedNotesPrinter(Clef.F, this);
        } else {
            staff.setImageResource(R.drawable.staff_g);
            notesGuesser = new NotesGuesser(Clef.G);
            printer = new AdvancedNotesPrinter(Clef.G, this);
        }

        generateRandomNote();
    }

    public void guessNote(final Note note) {
        runOnUiThread(() -> {
            if (noteGuess.getNote().equals(note)) {
                generateRandomNote();
            }
        });
    }

    @Override
    public void onDeviceOpened(MidiOutputPort midiOutputPort) {
        Log.i(TAG, "Midi device has been connected");

        midiOutputPort.connect(new MidiNotesReceiver(this));
    }

    public void generateRandomNote() {
        noteGuess = notesGuesser.randomNote();
        printer.printNoteGuess(noteGuess);
    }
}
