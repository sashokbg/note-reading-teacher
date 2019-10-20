package bg.alex.notereadingteacher.notes;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class NoteTest {

    @Test
    public void firstNote(){
        int noteCode = 0; //C

        Note note = new Note(noteCode);

        assertThat(note.getNotePitch()).isEqualTo(NotePitch.C);
    }

    @Test
    public void returnA(){
        int noteCode = 21; //A

        Note note = new Note(noteCode);

        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A);
    }

    @Test
    public void returnASharp(){
        int noteCode = 22; //A#

        Note note = new Note(noteCode);

        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A_SHARP);
    }

    @Test
    public void noteInHigherOctave(){
        int noteCode = 57; //A

        Note note = new Note(noteCode);

        assertThat(note.getNotePitch()).isEqualTo(NotePitch.A);
        assertThat(note.getOctave()).isEqualTo(3);
    }

    @Test
    public void firstOctave(){
        int noteCode = 0; //C1

        Note note = new Note(noteCode);

        assertThat(note.getOctave()).isEqualTo(-1);
    }

    @Test
    public void middleC(){
        int noteCode = 60; //C4

        Note note = new Note(noteCode);

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

    @Test
    public void should_create_previous_whole_note_from_D_sharp() {
        Note noteDSharp = new Note(NotePitch.D_SHARP, 3);

        Note previousNote = noteDSharp.previousWholeNote();

        assertThat(previousNote).isEqualTo(new Note(NotePitch.D, 3));
    }

    @Test
    public void should_create_previous_whole_note_from_D() {
        Note noteDSharp = new Note(NotePitch.D, 3);

        Note previousNote = noteDSharp.previousWholeNote();

        assertThat(previousNote).isEqualTo(new Note(NotePitch.C, 3));
    }

    @Test
    public void should_create_previous_whole_note_from_F() {
        Note noteDSharp = new Note(NotePitch.F, 3);

        Note previousNote = noteDSharp.previousWholeNote();

        assertThat(previousNote).isEqualTo(new Note(NotePitch.E, 3));
    }

    @Test
    public void should_create_previous_whole_note_from_C() {
        Note noteDSharp = new Note(NotePitch.C, 3);

        Note previousNote = noteDSharp.previousWholeNote();

        assertThat(previousNote).isEqualTo(new Note(NotePitch.B, 2));
    }

    @Test
    public void should_create_next_whole_note_from_E_to_F() {
        Note noteF = new Note(NotePitch.E, 3);

        Note nextNote = noteF.nextWholeNote();

        assertThat(nextNote.getNotePitch()).isEqualTo(NotePitch.F);
        assertThat(nextNote.getOctave()).isEqualTo(3);
    }

    @Test
    public void should_create_next_whole_note_from_FSharp_to_G() {
        Note noteF = new Note(NotePitch.F_SHARP, 3);

        Note nextNote = noteF.nextWholeNote();

        assertThat(nextNote).isEqualTo(new Note(NotePitch.G, 3));
    }

    @Test
    public void should_create_next_whole_note_from_F_to_G() {
        Note noteF = new Note(NotePitch.F, 3);

        Note nextNote = noteF.nextWholeNote();

        assertThat(nextNote.getNotePitch()).isEqualTo(NotePitch.G);
        assertThat(nextNote.getOctave()).isEqualTo(3);
    }

    @Test
    public void should_create_next_whole_note_from_B_to_C_in_upper_octave() {
        Note noteF = new Note(NotePitch.B, 3);

        Note nextNote = noteF.nextWholeNote();

        assertThat(nextNote.getNotePitch()).isEqualTo(NotePitch.C);
        assertThat(nextNote.getOctave()).isEqualTo(4);
    }
}