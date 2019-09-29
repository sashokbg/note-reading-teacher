package bg.alex.notereadingteacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class HomeActivity extends Activity {


    private static final String TAG = "HomeActivity";
    public static final String GAME_TYPE = "GAME_TYPE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }

    public void startGameInG(View view) {
        Log.d(TAG, "Button clicked !");

        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra(GAME_TYPE, "Game in G");
        startActivity(intent);
    }

    public void startGameInF(View view) {
        Log.d(TAG, "Button clicked !");

        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra(GAME_TYPE, "Game in F");
        startActivity(intent);
    }
}
