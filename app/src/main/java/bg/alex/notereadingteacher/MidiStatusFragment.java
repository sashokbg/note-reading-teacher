package bg.alex.notereadingteacher;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bg.alex.notereadingteacher.midi.MidiAware;
import bg.alex.notereadingteacher.midi.MidiHandler;

public class MidiStatusFragment extends Fragment {

    private MidiHandler midiHandler;

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        midiHandler.removeDevice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity activity = this.getActivity();
        MidiManager midiManager = (MidiManager) activity.getSystemService(Context.MIDI_SERVICE);

        View view = inflater.inflate(R.layout.midi_status_fragment, container, false);

        if (!(activity instanceof MidiAware)) {
            midiHandler = new MidiHandler(null, midiManager, view);
        } else {
            midiHandler = new MidiHandler((MidiAware) activity, midiManager, view);
        }

        midiHandler.registerMidiHandler();


        return view;
    }

    @Override
    public void onStart() {
        midiHandler.openConnectedDevice();
        super.onStart();
    }
}
