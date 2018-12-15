/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bg.alex.notereadingteacher;

import android.media.midi.MidiReceiver;
import android.util.Log;

import java.io.IOException;

/**
 * Convert stream of arbitrary MIDI bytes into discrete messages.
 *
 * Parses the incoming bytes and then posts individual messages to the receiver
 * specified in the constructor. Short messages of 1-3 bytes will be complete.
 * System Exclusive messages may be posted in pieces.
 *
 * Resolves Running Status and interleaved System Real-Time messages.
 */
public class MidiNotesReceiver extends MidiReceiver {
    private byte[] mBuffer = new byte[3];
    private int mCount;
    private byte mRunningStatus;
    private int mNeeded;
    private boolean mInSysEx;

    private static final String TAG = "MidiNotesReceiver";

    /*
     * @see android.midi.MidiReceiver#onSend(byte[], int, int, long)
     */
    @Override
    public void onSend(byte[] data, int offset, int count, long timestamp)
            throws IOException {

        Log.i(TAG,"------ New Message 2 ------");
        Log.i(TAG,"Size: " + count);
        Log.i(TAG,"Offset: " + offset);
        Log.i(TAG,"Type: " + format(data[offset]));

        for (int i = 0; i < count; i++) {
            final int currentByte = data[offset] & 0xFF;

            if(currentByte >= 0x80 && currentByte < 0x90){
                Log.i(TAG,"Note Off: " + format(data[offset]));
                Log.i(TAG,"Note Value: " + format(data[offset+1]));
                Log.i(TAG,"Velocity: " + format(data[offset+2]));
            }

            if(currentByte >= 0x90 && currentByte < 0xA0){
                Log.i(TAG,"Note On: " + format(data[offset]));
                Log.i(TAG,"Note Value: " + format(data[offset+1]));
                Log.i(TAG,"Velocity: " + format(data[offset+2]));
            }
            ++offset;
        }
    }

    private String format(byte b){
        return String.format("0x%02X", (b & 0xFF));
    }
}