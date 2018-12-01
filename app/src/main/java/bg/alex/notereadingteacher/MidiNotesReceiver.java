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

        for (int i = 0; i < count; i++) {
            final byte currentByte = data[offset];

            if (currentByte >= 0xF) {
                Log.i(TAG,"MIDI: Status byte detected");

                byte channelByte = data[offset+1];
                int channel = channelByte&7;

                Log.i(TAG,"MIDI: Status byte channel " + channel);
            }
            else {
                Log.i(TAG,"MIDI: Data byte detected");

            }
            ++offset;
        }
    }

}