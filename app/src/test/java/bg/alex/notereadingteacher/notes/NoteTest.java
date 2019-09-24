package bg.alex.notereadingteacher.notes;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class NoteTest {

    @Test
    public void firstNote(){
        //given
        int noteCode = 0; //C

        //when
        Note note = new Note(noteCode);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.C);
    }

    @Test
    public void returnA(){
        //given
        int noteCode = 21; //A

        //when
        Note note = new Note(noteCode);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A);
    }

    @Test
    public void returnASharp(){
        //given
        int noteCode = 22; //A#

        //when
        Note note = new Note(noteCode);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A_SHARP);
    }

    @Test
    public void noteInHigherOctave(){
        //given
        int noteCode = 57; //A

        //when
        Note note = new Note(noteCode);

        //then
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A);
        assertThat(note.getOctave()).isEqualTo(3);
    }

    @Test
    public void firstOctave(){
        //given
        int noteCode = 0; //C1

        //when
        Note note = new Note(noteCode);

        //then
        assertThat(note.getOctave()).isEqualTo(-1);
    }

    @Test
    public void middleC(){
        //given
        int noteCode = 60; //C4

        //when
        Note note = new Note(noteCode);

        //then
        assertThat(note.getOctave()).isEqualTo(4);
        assertThat(note.getNotePitch()).isEqualTo(NotePitch.C);
    }

    @Test
    public void compareNotes(){
        Note note1 = new Note(NotePitch.C, 4);
        Note note2 = new Note(NotePitch.D, 3);

        assertThat(note1).isGreaterThan(note2);
    }

    @Test
    public void compareNotesInSameOctave(){
        Note note1 = new Note(NotePitch.C_SHARP, 4);
        Note note2 = new Note(NotePitch.B, 4);

        assertThat(note2).isGreaterThan(note1);
    }

    @Test
    public void compareSharpNotes(){
        Note note1 = new Note(NotePitch.C_SHARP, 4);
        Note note2 = new Note(NotePitch.C, 4);

        assertThat(note1).isGreaterThan(note2);
    }

    @Test
    public void convertNoteToAbsoultePitch() {
        Note note = new Note(NotePitch.A, 3);

        int absolutePitch = note.getAbsolutePitch();

        assertThat(absolutePitch).isEqualTo(57);
    }
}