package bg.alex.notereadingteacher.notes;

import android.support.annotation.NonNull;

import java.util.Objects;

public class Note implements Comparable<Note> {
    private NotePitch notePitch;
    private int octave;

    public Note(int absolutePitch) {
        int relativePitch = absolutePitch % 12;

        this.notePitch = NotePitch.fromCode(relativePitch);
        this.octave = (absolutePitch / 12) - 1; //scientifically octaves start from -1
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
        return this.notePitch.getLabel() + this.getOctave();
    }

    /**
     * Note position relative to the staff.
     * Ex: C and #C have the same position on the staff so they both have position 0
     */
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

    public boolean isSharp() {
        return notePitch.equals(NotePitch.C_SHARP) ||
                notePitch.equals(NotePitch.D_SHARP) ||
                notePitch.equals(NotePitch.F_SHARP) ||
                notePitch.equals(NotePitch.G_SHARP) ||
                notePitch.equals(NotePitch.A_SHARP);
    }

    public boolean isGreaterThan(Note otherNote){
        return compareTo(otherNote) > 0;
    }

    @Override
    public int compareTo(@NonNull Note otherNote) {
        if(otherNote.getOctave() > this.getOctave()){
            return -1;
        }
        else if(otherNote.getOctave() < this.getOctave()){
            return 1;
        }
        else {
            return this.getNotePitch().compareTo(otherNote.getNotePitch());
        }
    }
}
