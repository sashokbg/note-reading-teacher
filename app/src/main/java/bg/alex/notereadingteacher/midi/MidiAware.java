package bg.alex.notereadingteacher.midi;

import android.media.midi.MidiOutputPort;

public interface MidiAware {

    void onDeviceOpened(MidiOutputPort midiOutputPort);
}
