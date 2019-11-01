package bg.alex.notereadingteacher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.printer.AdvancedNotesPrinter;
import bg.alex.notereadingteacher.printer.NotesPrinter;

import static bg.alex.notereadingteacher.guesser.NotesGuesser.MAX_NUMBER_OF_NOTES;

public class StaffFragment extends Fragment {

    private NotesPrinter printer;
    private List<NoteGuess> noteGuessList;
    private int currentNoteGuess = 0;
    private TextView debug;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.staff_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        printer = new AdvancedNotesPrinter(Clef.G, getActivity(), (ConstraintLayout) view);
        this.debug = view.findViewById(R.id.note_debug);

        super.onViewCreated(view, savedInstanceState);
    }

    public void advanceToNextNote() {
        currentNoteGuess++;
        if(currentNoteGuess >= MAX_NUMBER_OF_NOTES) {
//            advanceToNextLine();
        } else {
            TransitionManager.endTransitions(getActivity().findViewById(R.id.staff_fragment));
            TransitionManager.beginDelayedTransition(getActivity().findViewById(R.id.staff_fragment));
            debug.setText(noteGuessList.get(currentNoteGuess).getNote().toString());
            printer.printNoteIndicator(currentNoteGuess);
        }
    }

    public void advanceToNextLine(List<NoteGuess> noteGuessList) {
        this.noteGuessList = noteGuessList;
        currentNoteGuess = 0;

        debug.setText(noteGuessList.get(currentNoteGuess).getNote().toString());
        printer.printNoteGuesses(noteGuessList);
        printer.printNoteIndicator(currentNoteGuess);
    }

    public void stopGuessNote(Note note) {
        if (!noteGuessList.get(currentNoteGuess).getNote().equals(note)) {
            printer.removeMistakes();
        }
    }

    public void guessNote(final Note note) {
        getActivity().runOnUiThread(() -> {
            if(currentNoteGuess == noteGuessList.size() - 1) {
//                advanceToNextLine(null);
            } else if (noteGuessList.get(currentNoteGuess).getNote().equals(note)) {
                advanceToNextNote();
            } else {
                printer.printMistake(new NoteGuess(note, null), currentNoteGuess);
            }
        });
    }

}
