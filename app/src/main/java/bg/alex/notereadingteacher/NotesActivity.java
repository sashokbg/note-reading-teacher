package bg.alex.notereadingteacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.guesser.NotesGuesser;
import bg.alex.notereadingteacher.midi.MidiHandler;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.printer.AdvancedNotesPrinter;
import bg.alex.notereadingteacher.printer.NotesPrinter;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "NotesActivity";

    private NotesGuesser notesGuesser;
    private NoteGuess noteGuess;
    private NotesPrinter printer;
    private MidiHandler midiHandler;

    @Override
    protected void onDestroy() {
        midiHandler.removeDevice();
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = getIntent();

        TextView gameType = (TextView) findViewById(R.id.game_type);
        String intentMessage = intent.getStringExtra(HomeActivity.GAME_TYPE);
        gameType.setText(intentMessage);


        Intent i = new Intent(this, MidiHandler.class);
        startService(i);

        Log.i(TAG, "Starting application: ");

        midiHandler = new MidiHandler(this);
        midiHandler.registerMidiHandler();

    }

    @Override
    protected void onStart() {
        midiHandler.openConnectedDevice();
        notesGuesser = new NotesGuesser(Clef.G);
        printer = new AdvancedNotesPrinter(this);

        generateRandomNote();

        final Button button = (Button) findViewById(R.id.next_note);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateRandomNote();
            }
        });

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
}
