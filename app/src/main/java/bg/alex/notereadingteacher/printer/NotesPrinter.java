package bg.alex.notereadingteacher.printer;

import java.util.List;

import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Note;

public interface NotesPrinter {
    void printNoteGuesses(final List<NoteGuess> noteGuess);

    void printNoteIndicator(int noteGuess);

    void removeMistake(NoteGuess mistakeNoteGuess);

    void printMistake(NoteGuess noteGuess, int currentNoteGuess);
}
