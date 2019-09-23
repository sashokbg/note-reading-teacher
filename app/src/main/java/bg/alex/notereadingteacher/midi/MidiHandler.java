package bg.alex.notereadingteacher.midi;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import bg.alex.notereadingteacher.NotesActivity;
import bg.alex.notereadingteacher.R;

public class MidiHandler {

    private static final String TAG = "MidiHandler";
    private MidiDevice parentDevice;
    private final NotesActivity activity;
    private final MidiManager midiManager;

    public MidiHandler(final NotesActivity activity) {
        this.activity = activity;
        this.midiManager = (MidiManager) activity.getSystemService(Context.MIDI_SERVICE);
    }

    public void removeDevice() {
        Log.i(TAG, "Closing device: ");
        try {
            if (this.parentDevice != null) {
                Log.i(TAG, "Device to close : " + parentDevice.getInfo().getId());
                this.parentDevice.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerMidiHandler() {
        Log.i(TAG, "On Start");
        Handler handler = new Handler();

        midiManager.registerDeviceCallback(new MidiManager.DeviceCallback() {
            @Override
            public void onDeviceAdded(final MidiDeviceInfo device) {
                openDevice(device, midiManager);
            }

            @Override
            public void onDeviceRemoved(MidiDeviceInfo device) {
                final TextView deviceStatus = (TextView) activity.findViewById(R.id.status);
                final TextView devicesText = (TextView) activity.findViewById(R.id.devices);

                devicesText.setText("Devices: ");
                deviceStatus.setText("Device disconnected");

                removeDevice();
            }
        }, handler);
    }

    public void openConnectedDevice() {
        for (MidiDeviceInfo info : midiManager.getDevices()) {
            openDevice(info, midiManager);
            break;
        }
    }

    private void openDevice(MidiDeviceInfo deviceInfo, MidiManager midiManager) {
        final TextView statusText = (TextView) activity.findViewById(R.id.status);
        final TextView portsText = (TextView) activity.findViewById(R.id.ports);
        final TextView devicesText = (TextView) activity.findViewById(R.id.devices);

        Log.i(TAG, "Opening device: ");

        int deviceId = deviceInfo.getId();
        String deviceManufacturer = deviceInfo.getProperties().getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);

        Log.i(TAG, "Device: "+deviceId+" "+deviceManufacturer);

        devicesText.setText(activity.getString(R.string.midi_devices, deviceId, deviceManufacturer));

        midiManager.openDevice(deviceInfo, new MidiManager.OnDeviceOpenedListener() {
                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            statusText.setText(R.string.midi_devices_error);
                        } else {
                            parentDevice = device;
                            statusText.setText(R.string.midi_devices_ok);

                            MidiDeviceInfo.PortInfo[] portInfos = device.getInfo().getPorts();


                            for (MidiDeviceInfo.PortInfo portInfo : portInfos) {
                                Log.i(TAG, "Cycling port " + portInfo.getPortNumber());
                                if (portInfo.getType() == MidiDeviceInfo.PortInfo.TYPE_OUTPUT) {
                                    Log.i(TAG, "Found OUTPUT port");
                                    portsText.setText(activity.getString(R.string.midi_devices_ports, portInfo.getType(), portInfo.getPortNumber()));

                                    final MidiOutputPort outputPort = device.openOutputPort(portInfo.getPortNumber());

                                    outputPort.connect(new MidiNotesReceiver(activity));
                                    break;
                                }
                            }
                        }
                    }
                }, new Handler(Looper.getMainLooper())
        );
    }
}
