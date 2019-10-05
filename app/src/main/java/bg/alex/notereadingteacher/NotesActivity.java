package bg.alex.notereadingteacher;

import android.app.Activity;
import android.content.Intent;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

        Intent intent = getIntent();

        TextView gameType = (TextView) findViewById(R.id.game_type);
        String intentMessage = intent.getStringExtra(HomeActivity.GAME_TYPE);
        gameType.setText(intentMessage);

        Log.i(TAG, "Starting application: ");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        notesGuesser = new NotesGuesser(Clef.G);
        printer = new AdvancedNotesPrinter(this);

        final Button button = (Button) findViewById(R.id.next_note);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateRandomNote();
            }
        });

        generateRandomNote();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void generateRandomNote() {
        noteGuess = notesGuesser.randomNote();
        printer.printNoteGuess(noteGuess);
    }

    public void guessNote(final Note note) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (noteGuess.getNote().equals(note)) {
                    noteGuess = notesGuesser.randomNote();
                    printer.printNoteGuess(noteGuess);
                }
            }
        });
    }

    @Override
    public void onDeviceOpened(MidiOutputPort midiOutputPort) {
        Log.i(TAG, "Midi device has been connected");

        midiOutputPort.connect(new MidiNotesReceiver(this));
    }
}
