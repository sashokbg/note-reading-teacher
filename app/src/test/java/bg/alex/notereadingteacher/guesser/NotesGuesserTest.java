package bg.alex.notereadingteacher.guesser;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

import bg.alex.notereadingteacher.notes.NotePitch;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesGuesserTest {
    private static final int C6_PITCH = 84;
    private static final int C3_PITCH = 36;

    @Test
    public void shoul_generate_a_random_note(){
        NotesGuesser notesGuesser = new NotesGuesser();

        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess).isNotNull();
    }

    @Test
    public void should_not_generate_note_below_C3(){
        Random mockRandom = Mockito.mock(Random.class);

        when(mockRandom.nextInt(anyInt())).thenReturn(10, 15, C3_PITCH);

        NotesGuesser notesGuesser = new NotesGuesser(mockRandom);
        notesGuesser.randomNote();

        verify(mockRandom, times(3)).nextInt(anyInt());
    }

    @Test(expected = RuntimeException.class)
    public void should_not_generate_note_above_84(){
        Random mockRandom = Mockito.mock(Random.class);

        when(mockRandom.nextInt(anyInt())).thenReturn(C6_PITCH+1);

        NotesGuesser notesGuesser = new NotesGuesser(mockRandom);
        notesGuesser.randomNote();
    }

    @Test
    public void should_not_generate_sharps(){
        Random mockRandom = Mockito.mock(Random.class);

        when(mockRandom.nextInt(anyInt())).thenReturn(C3_PITCH+1);

        NotesGuesser notesGuesser = new NotesGuesser(mockRandom);
        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess.getNote().getNotePitch()).isNotEqualTo(NotePitch.C_SHARP);
        assertThat(noteGuess.getNote().getNotePitch()).isEqualTo(NotePitch.C);
    }
}
