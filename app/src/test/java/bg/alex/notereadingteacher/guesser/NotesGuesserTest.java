package bg.alex.notereadingteacher.guesser;

import org.junit.Test;

import bg.alex.notereadingteacher.notes.Note;

import static com.google.common.truth.Truth.assertThat;

public class NotesGuesserTest {
    @Test
    public void shoul_generate_a_random_note(){
        NotesGuesser notesGuesser = new NotesGuesser();

        NoteGuess noteGuess = notesGuesser.randomNote();

        assertThat(noteGuess).isNotNull();
    }
}
