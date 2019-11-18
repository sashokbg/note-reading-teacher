package bg.alex.notereadingteacher.guesser;

public class NoteGuessResult {
    private boolean isCorrect;
    private boolean isLastNote;

    public NoteGuessResult(boolean isCorrect, boolean isLastNote) {
        this.isCorrect = isCorrect;
        this.isLastNote = isLastNote;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public boolean isLastNote() {
        return isLastNote;
    }
}
