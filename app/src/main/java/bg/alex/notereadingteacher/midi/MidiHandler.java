package bg.alex.notereadingteacher.midi;

import android.content.res.Resources;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import bg.alex.notereadingteacher.R;

public class MidiHandler {

    private static final String TAG = "MidiHandler";
    private MidiDevice parentDevice;
    private final MidiAware midiAware;
    private final MidiManager midiManager;
    private View view;
    private final TextView deviceStatus;
    private final TextView devicesText;

    public MidiHandler(final MidiAware midiAware, MidiManager midiManager, View view) {
        this.midiAware = midiAware;
        this.midiManager = midiManager;
        this.view = view;
        this.deviceStatus = (TextView) view.findViewById(R.id.status);
        this.devicesText = (TextView) view.findViewById(R.id.devices);

        devicesText.setText(view.getContext().getString(R.string.midi_devices, 0, "N/A"));
        deviceStatus.setText(R.string.midi_devices_disconnected);
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
                devicesText.setText(R.string.midi_devices);
                deviceStatus.setText(R.string.midi_devices_disconnected);

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

    private void openDevice(MidiDeviceInfo deviceInfo, final MidiManager midiManager) {
        final TextView statusText = (TextView) view.findViewById(R.id.status);
        final TextView portsText = (TextView) view.findViewById(R.id.ports);
        final TextView devicesText = (TextView) view.findViewById(R.id.devices);

        Log.i(TAG, "Opening device: ");

        int deviceId = deviceInfo.getId();
        String deviceManufacturer = deviceInfo.getProperties().getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);

        Log.i(TAG, "Device: "+deviceId+" "+deviceManufacturer);

        devicesText.setText(view.getContext().getString(R.string.midi_devices, deviceId, deviceManufacturer));

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

                                    String string = view.getContext().getString(R.string.midi_devices_ports, portInfo.getType(), portInfo.getPortNumber());
                                    portsText.setText(string);

                                    final MidiOutputPort outputPort = device.openOutputPort(portInfo.getPortNumber());

                                    if(midiAware != null){
                                        midiAware.onDeviceOpened(outputPort);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }, new Handler(Looper.getMainLooper())
        );
    }
}
