package bg.alex.notereadingteacher.notes;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class NoteTest {

    @Test
    public void firstNote(){
        //given
        int noteCode = 0; //C

        //when
        Note note = new Note(noteCode, 0);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.C);
    }

    @Test
    public void returnA(){
        //given
        int noteCode = 21; //A

        //when
        Note note = new Note(noteCode, 0);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A);
    }

    @Test
    public void returnASharp(){
        //given
        int noteCode = 22; //A#

        //when
        Note note = new Note(noteCode, 0);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A_SHARP);
    }

    @Test
    public void returnAOtherOctave(){
        //given
        int noteCode = 57; //A

        //when
        Note note = new Note(noteCode, 0);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A);
    }
}