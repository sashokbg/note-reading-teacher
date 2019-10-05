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

    @Test
    public void shoul_generate_a_random_note(){
        NotesGuesser notesGuesser = new NotesGuesser(Clef.G);

        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess).isNotNull();
    }

    @Before
    public void setup() {
        this.mockRandom = Mockito.mock(Random.class);
    }

    @Test
    public void should_not_generate_note_below_F3_when_key_G(){
        Note f3 = new Note(NotePitch.F, 3);
        when(mockRandom.nextInt(anyInt())).thenReturn(1, 2, f3.getAbsolutePitch() - 1, f3.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.G, mockRandom);
        notesGuesser.randomNote();

        verify(mockRandom, times(4)).nextInt(anyInt());
    }

    @Test
    public void should_not_generate_note_above_F6_when_G(){
        Note f7 = new Note(NotePitch.F, 7);

        when(mockRandom.nextInt(anyInt())).thenReturn(f7.getAbsolutePitch());

        NotesGuesser notesGuesser = new NotesGuesser(Clef.G, mockRandom);
        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess.getNote()).isEqualTo(new Note(NotePitch.F, 6));
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
}
