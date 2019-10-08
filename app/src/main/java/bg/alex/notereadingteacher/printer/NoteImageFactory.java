package bg.alex.notereadingteacher.printer;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class NoteImageFactory {

    private Activity activity;

    public NoteImageFactory(Activity activity) {
        this.activity = activity;
    }

    public ImageView createImageView() {
        ImageView noteView = new ImageView(activity);
        noteView.setId(View.generateViewId());
        noteView.setTag("note");
        return noteView;
    }
}
