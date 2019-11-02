package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.widget.ImageView;

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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChoseImageTest {

    @Mock
    private Activity activity;

    @Mock
    private ImageView currentNoteView;

    private AdvancedNotesPrinter printer;

    @Before
    public void setup() {
        printer = new AdvancedNotesPrinter(null, activity, null);
    }

    @Test
    public void should_print_note_1_when_A1_in_keyF() {
        Note note = new Note(NotePitch.A, 1);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_01);
    }

    @Test
    public void should_print_note_2_when_B1_in_keyF() {
        Note note = new Note(NotePitch.B, 1);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_02);
    }

    @Test
    public void should_print_note_5_when_E2_in_keyF() {
        Note note = new Note(NotePitch.E, 2);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_05);
    }

    @Test
    public void should_print_note_18_when_D4_in_keyF() {
        Note note = new Note(NotePitch.D, 4);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_18);
    }

    @Test
    public void should_print_note_22_when_A4_in_keyF() {
        Note note = new Note(NotePitch.A, 4);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.F));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_22);
    }

    @Test
    public void should_print_note_1_when_F3_in_keyG() {
        Note note = new Note(NotePitch.F, 3);
        printer.setClef(Clef.G);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_01);
    }

    @Test
    public void should_print_note_5_when_C4_in_keyG() {
        Note note = new Note(NotePitch.C, 4);
        printer.setClef(Clef.G);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_05);
    }

    @Test
    public void should_print_note_22_when_F6_in_keyG() {
        Note note = new Note(NotePitch.F, 6);
        printer.setClef(Clef.G);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(note, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_22);
    }

    @Test
    public void should_print_empty_note_when_above_range_in_G() {
        Note noteAboveMax = new Note(NotePitch.G, 6);
        printer.setClef(Clef.G);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(noteAboveMax, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_empty);
    }

    @Test
    public void should_print_empty_note_when_below_range_in_G() {
        Note noteBelowMin = new Note(NotePitch.E, 3);
        printer.setClef(Clef.G);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(noteBelowMin, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_empty);
    }

    @Test
    public void should_print_empty_note_when_above_range_in_F() {
        Note noteAboveMax = new Note(NotePitch.B, 4);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(noteAboveMax, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_empty);
    }

    @Test
    public void should_print_empty_note_when_below_range_in_F() {
        Note noteBelowMin = new Note(NotePitch.G, 1);
        printer.setClef(Clef.F);

        // when
        printer.applyNoteImageTo(currentNoteView, new NoteGuess(noteBelowMin, Clef.G));

        // then
        verify(currentNoteView, times(1)).setImageResource(R.drawable.note_empty);
    }
}