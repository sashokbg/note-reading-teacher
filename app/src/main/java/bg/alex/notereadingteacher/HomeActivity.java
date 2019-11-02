package bg.alex.notereadingteacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import static bg.alex.notereadingteacher.NotesActivity.KEY_BOTH;
import static bg.alex.notereadingteacher.NotesActivity.KEY_F;
import static bg.alex.notereadingteacher.NotesActivity.KEY_G;

public class HomeActivity extends Activity {


    private static final String TAG = "HomeActivity";
    public static final String GAME_TYPE = "GAME_TYPE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }

    public void startGameInG(View view) {
        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra(GAME_TYPE, KEY_G);
        startActivity(intent);
    }

    public void startGameInF(View view) {
        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra(GAME_TYPE, KEY_F);
        startActivity(intent);
    }

    public void startGameInBoth(View view) {
        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra(GAME_TYPE, KEY_BOTH);
        startActivity(intent);
    }
}
