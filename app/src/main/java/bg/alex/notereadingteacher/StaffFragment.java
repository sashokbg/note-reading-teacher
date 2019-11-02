package bg.alex.notereadingteacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ViewGroup viewGroup;
    private Clef clef;

    @Override
    public void setArguments(@Nullable Bundle args) {
        this.clef = (Clef) args.getSerializable("KEY");
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewGroup = container;
        return inflater.inflate(R.layout.staff_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        printer = new AdvancedNotesPrinter(clef, getActivity(), (ConstraintLayout) view);
        this.debug = view.findViewById(R.id.note_debug);
        ImageView key = view.findViewById(R.id.key);

        if(clef == Clef.G) {
            key.setImageResource(R.drawable.sol_key);
        } else {
            key.setImageResource(R.drawable.fa_key);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    public void advanceToNextNote() {
        currentNoteGuess++;
        if(currentNoteGuess >= MAX_NUMBER_OF_NOTES) {
//            advanceToNextLine();
        } else {
            TransitionManager.endTransitions(viewGroup);
            TransitionManager.beginDelayedTransition(viewGroup);
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
