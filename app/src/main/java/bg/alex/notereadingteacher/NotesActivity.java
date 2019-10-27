package bg.alex.notereadingteacher;

import android.app.Activity;
import android.content.Intent;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private List<NoteGuess> noteGuessList;
    private int currentNoteGuess = 0;
    private NotesPrinter printer;
    private ImageView staff;
    private TextView debug;
    private static final int MAX_NUMBER_OF_NOTES = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

        this.debug = findViewById(R.id.note_debug);

        Log.i(TAG, "Starting application: ");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        staff = findViewById(R.id.staff);
        Intent intent = getIntent();

        TextView gameType = findViewById(R.id.game_type);
        String intentMessage = intent.getStringExtra(HomeActivity.GAME_TYPE);
        gameType.setText(intentMessage);
        staff.setImageResource(R.drawable.staff);
        ImageView key = findViewById(R.id.key);

        if(intentMessage.equals("Game in F")) {
            key.setImageResource(R.drawable.fa_key);
            notesGuesser = new NotesGuesser(Clef.F);
            printer = new AdvancedNotesPrinter(Clef.F, this);
        } else {
            key.setImageResource(R.drawable.sol_key);
            notesGuesser = new NotesGuesser(Clef.G);
            printer = new AdvancedNotesPrinter(Clef.G, this);
        }

        advanceToNextLine(null);
    }

    public void stopGuessNote(Note note) {
        if (!noteGuessList.get(currentNoteGuess).getNote().equals(note)) {
            printer.removeMistakes();
        }
    }

    public void guessNote(final Note note) {
        runOnUiThread(() -> {
            if(currentNoteGuess == noteGuessList.size() - 1) {
                advanceToNextLine(null);
            } else if (noteGuessList.get(currentNoteGuess).getNote().equals(note)) {
                advanceToNextNote(null);
            } else {
                printer.printMistake(new NoteGuess(note, null), currentNoteGuess);
            }
        });
    }

    @Override
    public void onDeviceOpened(MidiOutputPort midiOutputPort) {
        Log.i(TAG, "Midi device has been connected");

        midiOutputPort.connect(new MidiNotesReceiver(this));
    }

    public void advanceToNextNote(View view) {
        currentNoteGuess++;
        if(currentNoteGuess >= MAX_NUMBER_OF_NOTES) {
            advanceToNextLine(view);
        } else {
            TransitionManager.endTransitions(findViewById(R.id.notes_layout));
            TransitionManager.beginDelayedTransition(findViewById(R.id.notes_layout));
            debug.setText(noteGuessList.get(currentNoteGuess).getNote().toString());
            printer.printNoteIndicator(currentNoteGuess);
        }
    }

    public void advanceToNextLine(View view) {
        noteGuessList = new ArrayList<>();
        currentNoteGuess = 0;

        for(int i = 0; i< MAX_NUMBER_OF_NOTES; i++) {
            noteGuessList.add(notesGuesser.randomNote());
        }

        debug.setText(noteGuessList.get(currentNoteGuess).getNote().toString());
        printer.printNoteGuesses(noteGuessList);
        printer.printNoteIndicator(currentNoteGuess);
    }
}
