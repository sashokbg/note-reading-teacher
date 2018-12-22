package bg.alex.notereadingteacher;

import android.media.midi.MidiReceiver;
import android.util.Log;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;

public class MidiNotesReceiver extends MidiReceiver {
    private static final String TAG = "MidiNotesReceiver";
    private final NotesActivity activity;

    public MidiNotesReceiver(NotesActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onSend(byte[] data, int offset, int count, long timestamp) {

        Log.i(TAG,"------ New Message ------");
        Log.i(TAG,"Size: " + count);
        Log.i(TAG,"Offset: " + offset);
        Log.i(TAG,"Type: " + format(data[offset]));

        for (int i = 0; i < count; i++) {
            int currentByte = data[offset] & 0xFF;

            if(currentByte >= 0x90 && currentByte < 0xA0){
                Log.i(TAG,"Note On: " + format(data[offset]));
                Log.i(TAG,"Note Value: " + data[offset+1]);
                Log.i(TAG,"Velocity: " + data[offset+2]);
                activity.printNote(new Note(data[offset+1]), Clef.G);
                break;
            }

            if(currentByte >= 0x80 && currentByte < 0x90){
                Log.i(TAG,"Note Off: " + format(data[offset]));
                Log.i(TAG,"Note Value: " + data[offset+1]);
                Log.i(TAG,"Velocity: " + data[offset+2]);
                break;
            }
            ++offset;
        }
    }

    private String format(byte b){
        return String.format("0x%02X", (b & 0xFF));
    }
}