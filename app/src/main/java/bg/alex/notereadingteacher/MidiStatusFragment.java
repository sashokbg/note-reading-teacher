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
    private ViewGroup fragment;
    private boolean clicked = true;

    private TextView deviceInfos;

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
        view.setOnClickListener(this);


        if (activity instanceof MidiAware) {
            midiHandler = new MidiHandler((MidiAware) activity, midiManager, view);
        } else {
            midiHandler = new MidiHandler(null, midiManager, view);
        }

        midiHandler.registerMidiHandler();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fragment = view.findViewById(R.id.midi_status_fragment);
        deviceInfos = view.findViewById(R.id.device_infos);

        deviceInfos.setVisibility(View.GONE);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        midiHandler.openConnectedDevice();
        super.onStart();
    }

    @Override
    public void onClick(View view) {

        TransitionManager.beginDelayedTransition(fragment);
        if(clicked) {
            deviceInfos.setVisibility(View.VISIBLE);
        } else {
            deviceInfos.setVisibility(View.GONE);
        }

        clicked = !clicked;
    }
}
