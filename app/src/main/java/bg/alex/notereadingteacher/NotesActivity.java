package bg.alex.notereadingteacher;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.media.midi.MidiReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class NotesActivity extends AppCompatActivity {

    private Handler handler;
    private static final String TAG = "NotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.handler = new Handler();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);
        Context context = getApplicationContext();

        final MidiManager midiManager = (MidiManager) context.getSystemService(Context.MIDI_SERVICE);
        final TextView devicesText = (TextView) findViewById(R.id.devices);
        final TextView deviceStatus = (TextView) findViewById(R.id.status);


        midiManager.registerDeviceCallback(new MidiManager.DeviceCallback() {
            @Override
            public void onDeviceAdded(final MidiDeviceInfo device) {
                devicesText.setText("Devices: " + device.getId() + " Manifacturer: " + device.getProperties().getString(MidiDeviceInfo.PROPERTY_MANUFACTURER));

                openDevice(device, midiManager);
            }

            @Override
            public void onDeviceRemoved(MidiDeviceInfo device) {
                devicesText.setText("Devices: ");
                deviceStatus.setText("Device disconnected");
            }
        }, handler);
    }

    private void openDevice(MidiDeviceInfo deviceInfo, MidiManager midiManager) {
        final TextView statusText = (TextView) findViewById(R.id.status);
        final TextView portsText = (TextView) findViewById(R.id.ports);
        final TextView notesText = (TextView) findViewById(R.id.notes);

        midiManager.openDevice(deviceInfo, new MidiManager.OnDeviceOpenedListener() {
                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            statusText.setText("Device ERR");
                        } else {
                            statusText.setText("Device opened");

                            MidiDeviceInfo.PortInfo[] portInfos = device.getInfo().getPorts();


                            for (MidiDeviceInfo.PortInfo portInfo : portInfos) {
                                Log.i(TAG, "Cycling port " + portInfo.getPortNumber());
                                if (portInfo.getType() == MidiDeviceInfo.PortInfo.TYPE_OUTPUT) {
                                    Log.i(TAG, "Found OUTPUT port");
                                    portsText.setText("Ports: " + portInfo.getType() + " - " + portInfo.getPortNumber());

                                    final MidiOutputPort outputPort = device.openOutputPort(portInfo.getPortNumber());


                                    class MyReceiver extends MidiReceiver {
                                        public void onSend(final byte[] data, final int offset,
                                                           final int count, long timestamp) throws IOException {
                                            Log.i(TAG, "Received info "+ data.length + " length");
                                            Log.i(TAG, "Received info "+ count + " count");
                                            Log.i(TAG, "Received info "+ offset+ " offset");

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    int _offset = offset;
                                                    StringBuilder sb = new StringBuilder();
                                                    for (int i = 0; i < _offset; i++) {
                                                        byte currentByte = data[_offset];
                                                        final int currentInt = currentByte & 0xFF;

                                                        if(currentInt < 0xF0){
                                                            sb.append(String.format("%2b\n", currentByte));
                                                            _offset++;
                                                        }
                                                    }

                                                    notesText.setText("Notes: " + sb.toString());
                                                }
                                            });
                                        }
                                    }
                                    outputPort.connect(new MidiNotesReceiver());
                                    break;
                                }
                            }
                        }
                    }
                }, new Handler(Looper.getMainLooper())
        );
    }
}
