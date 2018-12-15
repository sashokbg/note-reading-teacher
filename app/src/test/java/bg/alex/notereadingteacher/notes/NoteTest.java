package bg.alex.notereadingteacher.notes;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class NoteTest {

    @Test
    public void firstNote(){
        //given
        int noteCode = 0; //C

        //when
        String note = Note.getNote(noteCode);

        //then
        assertThat(note).isEqualTo("C");
    }

    @Test
    public void returnA(){
        //given
        int noteCode = 21; //A

        //when
        String note = Note.getNote(noteCode);

        //then
        assertThat(note).isEqualTo("A");
    }

    @Test
    public void returnASharp(){
        //given
        int noteCode = 22; //A#

        //when
        String note = Note.getNote(noteCode);

        //then
        assertThat(note).isEqualTo("#A");
    }

    @Test
    public void returnAOtherOctave(){
        //given
        int noteCode = 57; //A

        //when
        String note = Note.getNote(noteCode);

        //then
        assertThat(note).isEqualTo("A");
    }
}