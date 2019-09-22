package bg.alex.notereadingteacher;

import android.content.Context;
import android.content.Intent;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.guesser.NotesGuesser;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.printer.NotesPrinter;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "NotesActivity";

    private NotesActivity that = this;
    private NotesGuesser notesGuesser;
    private NoteGuess noteGuess;
    private NotesPrinter printer;

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent i= new Intent(this, MidiService.class);
        startService(i);

        Log.i(TAG, "Starting application: ");


        setContentView(R.layout.activity_notes);






        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        notesGuesser = new NotesGuesser();
        printer = new NotesPrinter(this);

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
