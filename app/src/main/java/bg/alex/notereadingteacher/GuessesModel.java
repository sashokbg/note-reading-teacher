package bg.alex.notereadingteacher;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class GuessesModel extends BaseObservable {

    private int correctGuesses = 0;
    private int wrongGuesses = 0;

    @Bindable
    public String getCorrectGuesses() {
        return Integer.toString(correctGuesses);
    }

    @Bindable
    public int getTotalGuesses() {
        return wrongGuesses + correctGuesses;
    }

    public void wrongGuess() {
        this.wrongGuesses ++;
        notifyPropertyChanged(BR._all);
    }

    public void correctGuess() {
        this.correctGuesses ++;
        notifyPropertyChanged(BR._all);
    }
}
