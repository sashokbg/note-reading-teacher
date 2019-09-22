package bg.alex.notereadingteacher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class MidiService extends Service {

    private static final String TAG = "MidiService";
    private Handler handler;
    private MidiDevice parentDevice;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "On Bind");
        return null;
    }

    @Override
    public void onDestroy() {
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        final MidiManager midiManager = (MidiManager) context.getSystemService(Context.MIDI_SERVICE);
        Log.i(TAG, "On Start");

        this.handler = new Handler();

        final TextView deviceStatus = (TextView) findViewById(R.id.status);
        final TextView devicesText = (TextView) findViewById(R.id.devices);

        for (MidiDeviceInfo info : midiManager.getDevices()) {
            openDevice(info, midiManager);
            break;
        }

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

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "On Create");
        super.onCreate();
    }

    private void openDevice(MidiDeviceInfo deviceInfo, MidiManager midiManager) {
        final TextView statusText = (TextView) findViewById(R.id.status);
        final TextView portsText = (TextView) findViewById(R.id.ports);
        final TextView devicesText = (TextView) findViewById(R.id.devices);

        Log.i(TAG, "Opening device: ");

        int deviceId = deviceInfo.getId();
        String deviceManufacturer = deviceInfo.getProperties().getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);

        Log.i(TAG, "Device: "+deviceId+" "+deviceManufacturer);

        devicesText.setText(getString(R.string.midi_devices, deviceId, deviceManufacturer));

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
                                    portsText.setText(getString(R.string.midi_devices_ports, portInfo.getType(), portInfo.getPortNumber()));

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
