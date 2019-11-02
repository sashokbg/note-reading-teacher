package bg.alex.notereadingteacher;

import android.content.Intent;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import static bg.alex.notereadingteacher.StaffFragment.HIDE_NOTE_INDICATOR;
import static bg.alex.notereadingteacher.guesser.NotesGuesser.MAX_NUMBER_OF_NOTES;

public class NotesActivity extends FragmentActivity implements MidiAware {

    private static final String TAG = "NotesActivity";
    public static final String KEY_G = "KEY_G";
    public static final String KEY_F = "KEY_F";
    public static final String KEY_BOTH = "KEY_BOTH";

    private NotesGuesser notesGuesser;

    private ImageView staff;

    private StaffFragment staffFragment1;
    private StaffFragment staffFragment2;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentManager = this.getSupportFragmentManager();

        setContentView(R.layout.notes_activity);

        Intent intent = getIntent();

        TextView gameType = findViewById(R.id.game_type);
        String intentMessage = intent.getStringExtra(HomeActivity.GAME_TYPE);
        gameType.setText(intentMessage);

        if(intentMessage.equals(KEY_F)) {
            notesGuesser = new NotesGuesser(Clef.F);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            staffFragment1 = new StaffFragment();
            fragmentTransaction.add(R.id.staff_fragment1, staffFragment1);

            Bundle bundle = new Bundle();
            bundle.putSerializable("KEY", Clef.F);
            staffFragment1.setArguments(bundle);

            fragmentTransaction.commit();
        } else if (intentMessage.equals(KEY_G)) {
            notesGuesser = new NotesGuesser(Clef.G);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            staffFragment1 = new StaffFragment();
            fragmentTransaction.add(R.id.staff_fragment1, staffFragment1);

            Bundle bundle = new Bundle();
            bundle.putSerializable("KEY", Clef.G);
            staffFragment1.setArguments(bundle);

            fragmentTransaction.commit();
        } else { // BOTH
            notesGuesser = new NotesGuesser();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            staffFragment1 = new StaffFragment();
            staffFragment2 = new StaffFragment();
            fragmentTransaction.add(R.id.staff_fragment1, staffFragment1);
            fragmentTransaction.add(R.id.staff_fragment2, staffFragment2);

            Bundle bundle = new Bundle();
            bundle.putSerializable("KEY", Clef.G);
            bundle.putBoolean(HIDE_NOTE_INDICATOR, true);
            staffFragment1.setArguments(bundle);

            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("KEY", Clef.F);
            bundle2.putBoolean(HIDE_NOTE_INDICATOR, false);
            staffFragment2.setArguments(bundle2);

            fragmentTransaction.commit();
        }

        Log.i(TAG, "Starting application: ");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        advanceToNextLine(null);
    }

    public void advanceToNextNote(View view) {
        staffFragment1.advanceToNextNote();
        if(staffFragment2 != null){
            staffFragment2.advanceToNextNote();
        }
    }

    public void advanceToNextLine(View view) {
        List<NoteGuess> noteGuessList = new ArrayList<>();

        for(int i = 0; i< MAX_NUMBER_OF_NOTES; i++) {
            noteGuessList.add(notesGuesser.randomNote());
        }

        staffFragment1.advanceToNextLine(noteGuessList);
        if(staffFragment2 != null){
            staffFragment2.advanceToNextLine(noteGuessList);
        }
    }

    public void stopGuessNote(Note note) {
        staffFragment1.stopGuessNote(note);
        if(staffFragment2 != null){
            staffFragment2.stopGuessNote(note);
        }
    }

    public void guessNote(final Note note) {
        staffFragment1.guessNote(note);
        if(staffFragment2 != null){
            staffFragment2.guessNote(note);
        }
    }

    @Override
    public void onDeviceOpened(MidiOutputPort midiOutputPort) {
        Log.i(TAG, "Midi device has been connected");

        midiOutputPort.connect(new MidiNotesReceiver(this));
    }
}
