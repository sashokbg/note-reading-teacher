package bg.alex.notereadingteacher.guesser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.notes.NotePitch;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesGuesserTest {
    private Random mockRandom;

    @Before
    public void setup() {
        this.mockRandom = Mockito.mock(Random.class);
    }


    @Test
    public void should_generate_a_random_note_in_G(){
        NotesGuesser notesGuesser = new NotesGuesser(Clef.G);

        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess).isNotNull();
    }

    @Test
    public void should_generate_a_random_note_in_F(){
        NotesGuesser notesGuesser = new NotesGuesser(Clef.F);

        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess).isNotNull();
    }

    @Test
    public void should_not_generate_note_below_E2_when_key_F(){
        Note e2 = new Note(NotePitch.E, 2);
        when(mockRandom.nextInt(anyInt())).thenReturn(1, 2, e2.getAbsolutePitch() - 1, e2.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.F, mockRandom);
        notesGuesser.randomNote();

        verify(mockRandom, times(4)).nextInt(anyInt());
    }

    @Test
    public void should_not_generate_note_above_E4_when_key_F(){
        Note E6 = new Note(NotePitch.E, 6);

        when(mockRandom.nextInt(anyInt())).thenReturn(E6.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.F, mockRandom);
        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess.getNote()).isEqualTo(new Note(NotePitch.E, 4));
    }

    @Test
    public void should_not_generate_note_below_A3_when_key_G(){
        Note A3 = new Note(NotePitch.A, 3);
        when(mockRandom.nextInt(anyInt())).thenReturn(1, 2, A3.getAbsolutePitch() - 1, A3.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.G, mockRandom);
        notesGuesser.randomNote();

        verify(mockRandom, times(4)).nextInt(anyInt());
    }

    @Test
    public void should_not_generate_note_above_C6_when_G(){
        Note C6 = new Note(NotePitch.C, 6);

        when(mockRandom.nextInt(anyInt())).thenReturn(C6.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.G, mockRandom);
        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess.getNote()).isEqualTo(new Note(NotePitch.C, 6));
    }

    @Test
    public void should_not_generate_sharps(){
        Note cSharp = new Note(NotePitch.C_SHARP, 5);

        when(mockRandom.nextInt(anyInt())).thenReturn(cSharp.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.G, mockRandom);
        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess.getNote().getNotePitch()).isNotEqualTo(NotePitch.C_SHARP);
        assertThat(noteGuess.getNote().getNotePitch()).isEqualTo(NotePitch.D);
    }

    @Test
    public void should_generate_all_notes_when_no_key_specified() {
        NotesGuesser notesGuesser = new NotesGuesser(mockRandom);
        Note G4 = new Note(NotePitch.G, 4);

        when(mockRandom.nextInt(anyInt())).thenReturn(G4.getAbsolutePitch());

        notesGuesser.randomNote();

        verify(mockRandom, times(1)).nextInt(NotesGuesser.MAX_NOTE_G.getAbsolutePitch());
    }

    @Test
    public void should_generate_alternating_notes_when_no_key_specified() {
        NotesGuesser noteGuesser = new NotesGuesser(mockRandom);

        Note C4 = new Note(NotePitch.C, 4);
        Note C3 = new Note(NotePitch.C, 3);

        when(mockRandom.nextInt(anyInt())).thenReturn(C4.getAbsolutePitch(), C3.getAbsolutePitch());

        NoteGuess noteGuess1 = noteGuesser.randomNote();
        NoteGuess noteGuess2 = noteGuesser.randomNote();

        verify(mockRandom, times(1)).nextInt(NotesGuesser.MAX_NOTE_G.getAbsolutePitch());
        verify(mockRandom, times(1)).nextInt(NotesGuesser.MAX_NOTE_F.getAbsolutePitch());

        assertThat(noteGuess1.getClef()).isEqualTo(Clef.G);
        assertThat(noteGuess2.getClef()).isEqualTo(Clef.F);
    }
}
