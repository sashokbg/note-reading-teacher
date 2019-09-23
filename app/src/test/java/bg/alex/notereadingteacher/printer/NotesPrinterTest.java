package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.junit.Test;
import org.mockito.Mockito;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

import static org.mockito.Mockito.when;

public class NotesPrinterTest {

    private Context mMockContext = Mockito.mock(Context.class);

    @Test
    public void print_barred_notes_in_G(){
        Activity mockActivity = Mockito.mock(Activity.class);

        FrameLayout staff = new FrameLayout(mMockContext);

        when(mockActivity.findViewById(R.id.staff_container)).thenReturn(staff);
        when(mockActivity.findViewById(R.id.notes)).thenReturn(new TextView(mMockContext));

        BasicNotesPrinter notesPrinter = new BasicNotesPrinter(mockActivity);

        notesPrinter.printNoteGuess(new NoteGuess(new Note(NotePitch.C, 5), Clef.G));
    }
}