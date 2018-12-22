package bg.alex.notereadingteacher.notes;

import java.util.Objects;

public class Note {
    private NotePitch notePitch;
    private int octave;

    public Note(int pitchCode) {
        int relativePitchCode = pitchCode % 12;

        this.notePitch = NotePitch.fromCode(relativePitchCode);
        this.octave = (pitchCode/12)-1; //scientifically octaves start from -1
    }

    public Note(NotePitch relativePitch, int octave) {
        this.notePitch = relativePitch;
        this.octave = octave;
    }

    public NotePitch getNotePitch() {
        return notePitch;
    }

    public void setNotePitch(NotePitch notePitch) {
        this.notePitch = notePitch;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    @Override
    public String toString() {
        return this.notePitch.getLabel()+this.getOctave();
    }

    public int getPosition() {
        return notePitch.getPosition();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return octave == note.octave &&
                notePitch == note.notePitch;
    }

    @Override
    public int hashCode() {

        return Objects.hash(notePitch, octave);
    }
}
