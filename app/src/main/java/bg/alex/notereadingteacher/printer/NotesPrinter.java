package bg.alex.notereadingteacher.printer;

import java.util.List;

import bg.alex.notereadingteacher.guesser.NoteGuess;

public interface NotesPrinter {
    void printNoteGuesses(final List<NoteGuess> noteGuess);

    void printNoteIndicator(int noteGuess);

    void removeMistakes();

    void printMistake(NoteGuess noteGuess, int currentNoteGuess);
}
