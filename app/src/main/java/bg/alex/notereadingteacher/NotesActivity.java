package bg.alex.notereadingteacher;

import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

public class NotesActivity extends AppCompatActivity {

    private Handler handler;
    private static final String TAG = "NotesActivity";
    private MidiDevice parentDevice;
    private NotesActivity that = this;
    private ImageView noteImage;

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Closing device: ");
        try {
            if (this.parentDevice != null) {
                Log.i(TAG, "Device to close : " + parentDevice.getInfo().getId());
                this.parentDevice.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "Starting application: ");
        this.handler = new Handler();

        setContentView(R.layout.activity_notes);
        Context context = getApplicationContext();

        final MidiManager midiManager = (MidiManager) context.getSystemService(Context.MIDI_SERVICE);

        final TextView deviceStatus = (TextView) findViewById(R.id.status);

        for (MidiDeviceInfo info : midiManager.getDevices()) {
            openDevice(info, midiManager);
            break;
        }

        final TextView devicesText = (TextView) findViewById(R.id.devices);

        midiManager.registerDeviceCallback(new MidiManager.DeviceCallback() {
            @Override
            public void onDeviceAdded(final MidiDeviceInfo device) {
                openDevice(device, midiManager);
            }

            @Override
            public void onDeviceRemoved(MidiDeviceInfo device) {
                devicesText.setText("Devices: ");
                deviceStatus.setText("Device disconnected");
            }
        }, handler);


        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
//        printNote(new Note(NotePitch.C, 0));
////        printNote(new Note(NotePitch.D, 0));
//        printNote(new Note(NotePitch.E, 0));
////        printNote(new Note(NotePitch.F, 0));
//        printNote(new Note(NotePitch.G, 0));
//        printNote(new Note(NotePitch.B, 0));
        super.onStart();
    }

    public void printNote(final Note note) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(noteImage == null){
                    noteImage = new ImageView(that);
                    noteImage.setImageResource(R.drawable.note);
                    noteImage.setX(400);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(95, 75);
                    noteImage.setLayoutParams(params);
                    

                    ((RelativeLayout) findViewById(R.id.staff_container)).addView(noteImage);
                }

                noteImage.setY((330 - (note.getPosition() * 27)));
            }
        });
    }

    private void openDevice(MidiDeviceInfo deviceInfo, MidiManager midiManager) {
        final TextView statusText = (TextView) findViewById(R.id.status);
        final TextView portsText = (TextView) findViewById(R.id.ports);
        final TextView devicesText = (TextView) findViewById(R.id.devices);

        Log.i(TAG, "Opening device: ");

        devicesText.setText("Devices: " + deviceInfo.getId() + " Manifacturer: " + deviceInfo.getProperties().getString(MidiDeviceInfo.PROPERTY_MANUFACTURER));

        midiManager.openDevice(deviceInfo, new MidiManager.OnDeviceOpenedListener() {
                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            statusText.setText("Device ERR");
                        } else {
                            parentDevice = device;
                            statusText.setText("Device opened");

                            MidiDeviceInfo.PortInfo[] portInfos = device.getInfo().getPorts();


                            for (MidiDeviceInfo.PortInfo portInfo : portInfos) {
                                Log.i(TAG, "Cycling port " + portInfo.getPortNumber());
                                if (portInfo.getType() == MidiDeviceInfo.PortInfo.TYPE_OUTPUT) {
                                    Log.i(TAG, "Found OUTPUT port");
                                    portsText.setText("Ports: " + portInfo.getType() + " - " + portInfo.getPortNumber());

                                    final MidiOutputPort outputPort = device.openOutputPort(portInfo.getPortNumber());

                                    outputPort.connect(new MidiNotesReceiver(that));
                                    break;
                                }
                            }
                        }
                    }
                }, new Handler(Looper.getMainLooper())
        );
    }
}
