package bg.alex.notereadingteacher;

import android.app.Fragment;
import android.content.Intent;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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

import static bg.alex.notereadingteacher.guesser.NotesGuesser.MAX_NUMBER_OF_NOTES;

public class NotesActivity extends FragmentActivity implements MidiAware {

    private static final String TAG = "NotesActivity";

    private NotesGuesser notesGuesser;

    private ImageView staff;

    private StaffFragment staffFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

        Log.i(TAG, "Starting application: ");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof StaffFragment) {
            this.staffFragment = (StaffFragment) fragment;
        }

        super.onAttachFragment(fragment);
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
        } else {
            key.setImageResource(R.drawable.sol_key);
            notesGuesser = new NotesGuesser(Clef.G);
        }

        advanceToNextLine(null);
    }

    public void advanceToNextNote(View view) {
        staffFragment.advanceToNextNote();
    }

    public void advanceToNextLine(View view) {
        List<NoteGuess> noteGuessList = new ArrayList<>();

        for(int i = 0; i< MAX_NUMBER_OF_NOTES; i++) {
            noteGuessList.add(notesGuesser.randomNote());
        }

        staffFragment.advanceToNextLine(noteGuessList);
    }

    public void stopGuessNote(Note note) {
        staffFragment.stopGuessNote(note);
    }

    public void guessNote(final Note note) {
        staffFragment.guessNote(note);
    }

    @Override
    public void onDeviceOpened(MidiOutputPort midiOutputPort) {
        Log.i(TAG, "Midi device has been connected");

        midiOutputPort.connect(new MidiNotesReceiver(this));
    }
}
