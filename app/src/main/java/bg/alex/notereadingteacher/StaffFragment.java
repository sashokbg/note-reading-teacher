package bg.alex.notereadingteacher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bg.alex.notereadingteacher.databinding.StaffFragmentBinding;
import bg.alex.notereadingteacher.guesser.NoteGuess;
import bg.alex.notereadingteacher.guesser.NoteGuessResult;
import bg.alex.notereadingteacher.notes.Clef;
import bg.alex.notereadingteacher.notes.Note;
import bg.alex.notereadingteacher.printer.AdvancedNotesPrinter;
import bg.alex.notereadingteacher.printer.NotesPrinter;

import static bg.alex.notereadingteacher.guesser.NotesGuesser.MAX_NUMBER_OF_NOTES;

public class StaffFragment extends Fragment {

    public static final String HIDE_NOTE_INDICATOR = "HIDE_NOTE_INDICATOR";
    private NotesPrinter printer;
    private List<NoteGuess> noteGuessList;
    private int currentNoteGuess = 0;
    private TextView debug;
    private ViewGroup viewGroup;
    private Clef clef;
    private StaffFragmentProperties properties;

    @Override
    public void setArguments(@Nullable Bundle args) {
        properties = new StaffFragmentProperties();
        this.clef = (Clef) args.getSerializable("KEY");
        properties.hideIndicator = args.getBoolean(HIDE_NOTE_INDICATOR);

        super.setArguments(args);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewGroup = container;

        StaffFragmentBinding binding = StaffFragmentBinding.inflate(inflater, container, false);
        binding.setProperties(properties);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        printer = new AdvancedNotesPrinter(clef, getActivity(), (ConstraintLayout) view);
//        this.debug = view.findViewById(R.id.note_debug);

        ImageView key = view.findViewById(R.id.key);

        if(clef == Clef.G) {
            key.setImageResource(R.drawable.sol_key);
        } else {
            key.setImageResource(R.drawable.fa_key);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    public void advanceToNextLine(List<NoteGuess> noteGuessList) {
        this.noteGuessList = noteGuessList;
        currentNoteGuess = 0;

//        debug.setText(noteGuessList.get(currentNoteGuess).getNote().toString());
        printer.printNoteGuesses(noteGuessList);
        if(!properties.hideIndicator) {
            printer.printNoteIndicator(currentNoteGuess);
        }
    }

    public void stopGuessNote(Note note) {
        if (!noteGuessList.get(currentNoteGuess).getNote().equals(note)) {
            printer.removeMistakes();
        }
    }

    public NoteGuessResult guessNote(final Note note, boolean forceRightGuess) {

        if (noteGuessList.get(currentNoteGuess).getNote().equals(note) || forceRightGuess) {
            currentNoteGuess++;
            boolean isLastNote = false;

            if(currentNoteGuess >= MAX_NUMBER_OF_NOTES) {
                isLastNote = true;
            } else {
                printer.printNoteIndicator(currentNoteGuess);
            }

            return new NoteGuessResult(true, isLastNote);
        } else {
            NoteGuess noteGuess = new NoteGuess(note, null);
            noteGuess.setMistake(true);
            printer.printMistake(noteGuess, currentNoteGuess);

            return new NoteGuessResult(false, false);
        }
    }

}
