package bg.alex.notereadingteacher.midi;

import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    private final TextView deviceInfos;

    private ImageView statusImageDisconnected;
    private ImageView statusImageConnected;

    public MidiHandler(final MidiAware midiAware, MidiManager midiManager, View view) {
        this.midiAware = midiAware;
        this.midiManager = midiManager;
        this.view = view;
        this.deviceStatus = (TextView) view.findViewById(R.id.status);
        this.deviceInfos = (TextView) view.findViewById(R.id.device_infos);
        this.statusImageDisconnected = (ImageView) view.findViewById(R.id.status_image_discconnected);
        this.statusImageConnected = (ImageView) view.findViewById(R.id.status_image_connected);

        String deviceInfos = getDefaultDeviceInfos(view);

        this.deviceInfos.setText(deviceInfos);
        deviceStatus.setText(R.string.midi_device_status_disconnected);

        statusImageConnected.setVisibility(View.GONE);
        statusImageDisconnected.setVisibility(View.VISIBLE);
    }

    @NonNull
    private String getDefaultDeviceInfos(View view) {
        return view.getContext().getString(
                R.string.midi_device_infos,
                0,
                "N/A",
                0,
                0);
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
                statusImageConnected.setVisibility(View.VISIBLE);
                statusImageDisconnected.setVisibility(View.GONE);
            }

            @Override
            public void onDeviceRemoved(MidiDeviceInfo device) {
                deviceInfos.setText(getDefaultDeviceInfos(view));
                deviceStatus.setText(R.string.midi_device_status_disconnected);

                removeDevice();

                statusImageConnected.setVisibility(View.GONE);
                statusImageDisconnected.setVisibility(View.VISIBLE);
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
        final TextView deviceInfos = (TextView) view.findViewById(R.id.device_infos);

        Log.i(TAG, "Opening device: ");

        final int deviceId = deviceInfo.getId();
        final String deviceManufacturer = deviceInfo.getProperties().getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);

        Log.i(TAG, "Device: " + deviceId + " " + deviceManufacturer);

        midiManager.openDevice(deviceInfo, new MidiManager.OnDeviceOpenedListener() {

                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            statusText.setText(R.string.midi_device_status_error);
                        } else {
                            parentDevice = device;
                            statusText.setText(R.string.midi_device_status_connected);

                            MidiDeviceInfo.PortInfo[] portInfos = device.getInfo().getPorts();


                            for (MidiDeviceInfo.PortInfo portInfo : portInfos) {
                                Log.i(TAG, "Cycling port " + portInfo.getPortNumber());
                                if (portInfo.getType() == MidiDeviceInfo.PortInfo.TYPE_OUTPUT) {
                                    Log.i(TAG, "Found OUTPUT port");

                                    deviceInfos.setText(view.getContext().getString(
                                            R.string.midi_device_infos,
                                            deviceId,
                                            deviceManufacturer,
                                            portInfo.getType(),
                                            portInfo.getPortNumber()
                                    ));

                                    final MidiOutputPort outputPort = device.openOutputPort(portInfo.getPortNumber());
                                    statusImageConnected.setVisibility(View.VISIBLE);
                                    statusImageDisconnected.setVisibility(View.GONE);

                                    if (midiAware != null) {
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
