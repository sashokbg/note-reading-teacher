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
    private MidiDevice parentDevice;

    @Override
    protected void onDestroy() {
        Log.i(TAG,"Closing device: ");
        try {
            if(this.parentDevice != null){
                Log.i(TAG,"Device to close : "+ parentDevice.getInfo().getId());
                this.parentDevice.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"Starting application: ");
        this.handler = new Handler();

        setContentView(R.layout.activity_notes);
        Context context = getApplicationContext();

        final MidiManager midiManager = (MidiManager) context.getSystemService(Context.MIDI_SERVICE);

        final TextView deviceStatus = (TextView) findViewById(R.id.status);

        for(MidiDeviceInfo info : midiManager.getDevices()){
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

        super.onStart();
    }

    private void openDevice(MidiDeviceInfo deviceInfo, MidiManager midiManager) {
        final TextView statusText = (TextView) findViewById(R.id.status);
        final TextView portsText = (TextView) findViewById(R.id.ports);
        final TextView notesText = (TextView) findViewById(R.id.notes);
        final TextView devicesText = (TextView) findViewById(R.id.devices);

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
