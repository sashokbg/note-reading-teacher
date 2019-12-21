package bg.alex.notereadingteacher;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class CorrectGuessesModel extends BaseObservable {

    private int correctGuesses = 0;

    @Bindable
    public String getCorrectGuesses() {
        return Integer.toString(correctGuesses);
    }

    public void correctGuess() {
        this.correctGuesses ++;
        notifyPropertyChanged(BR._all);
    }
}
