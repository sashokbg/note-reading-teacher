package bg.alex.notereadingteacher.notes;

public class Note {
    private NotePitch notePitch;
    private int octave;

    public Note(int pitchCode, int octave) {
        int relativePitchCode = pitchCode % 12;

        this.notePitch = NotePitch.fromCode(relativePitchCode);
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
}
