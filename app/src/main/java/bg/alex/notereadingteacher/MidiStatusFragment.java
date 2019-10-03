package bg.alex.notereadingteacher;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bg.alex.notereadingteacher.midi.MidiAware;
import bg.alex.notereadingteacher.midi.MidiHandler;

public class MidiStatusFragment extends Fragment implements View.OnClickListener {

    private MidiHandler midiHandler;
    private View view;
    private ViewGroup fragment;
    private boolean clicked = true;
    private TextView deviceName;

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


        view = inflater.inflate(R.layout.midi_status_fragment, container, false);
        view.setOnClickListener(this);


        if (!(activity instanceof MidiAware)) {
            midiHandler = new MidiHandler(null, midiManager, view);
        } else {
            midiHandler = new MidiHandler((MidiAware) activity, midiManager, view);
        }

        midiHandler.registerMidiHandler();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fragment = (ViewGroup ) view.findViewById(R.id.midi_status_fragment);
        deviceName = (TextView) view.findViewById(R.id.device_name);

        fragment.removeView(deviceName);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        midiHandler.openConnectedDevice();
        super.onStart();
    }

    @Override
    public void onClick(View view) {

        if(clicked) {
            TransitionManager.beginDelayedTransition(fragment);
            fragment.addView(deviceName);
            deviceName.setVisibility(View.VISIBLE);
        } else {
            deviceName.setVisibility(View.INVISIBLE);
            TransitionManager.beginDelayedTransition(fragment);
            fragment.removeView(deviceName);
        }

        clicked = !clicked;
    }
}
