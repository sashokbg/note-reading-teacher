package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import bg.alex.notereadingteacher.R;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdvancedNotesPrinterTest {

    @Mock
    private Activity activity;

    @Mock
    private ImageView currentNoteView;

    @Mock
    private TextView debugView;

    @Before
    public void setup() {
        when(activity.findViewById(R.id.current_note)).thenReturn(currentNoteView);
        when( activity.findViewById(R.id.note_debug)).thenReturn(debugView);

        doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(activity).runOnUiThread(any(Runnable.class));
    }

    @Test
    public void should_print_note_1_when_A1_in_keyF() {
        Note note = new Note(NotePitch.A, 1);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.F, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_01);
    }

    @Test
    public void should_print_note_2_when_B1_in_keyF() {
        Note note = new Note(NotePitch.B, 1);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.F, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_02);
    }

    @Test
    public void should_print_note_5_when_E2_in_keyF() {
        Note note = new Note(NotePitch.E, 2);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.F, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_05);
    }

    @Test
    public void should_print_note_18_when_D4_in_keyF() {
        Note note = new Note(NotePitch.D, 4);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.F, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_18);
    }

    @Test
    public void should_print_note_22_when_A4_in_keyF() {
        Note note = new Note(NotePitch.A, 4);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.F, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_22);
    }

    @Test
    public void should_print_note_1_when_F3_in_keyG() {
        Note note = new Note(NotePitch.F, 3);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.G, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_01);
    }

    @Test
    public void should_print_note_5_when_C4_in_keyG() {
        Note note = new Note(NotePitch.C, 4);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.G, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_05);
    }

    @Test
    public void should_print_note_22_when_F6_in_keyG() {
        Note note = new Note(NotePitch.F, 6);
        NotesPrinter printer = new AdvancedNotesPrinter(Clef.G, activity);

        // when
        printer.printNoteGuesses(new NoteGuess(note, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_22);
    }
}