package bg.alex.notereadingteacher.notes;

import java.util.HashMap;
import java.util.Map;

public class Note {
    static Map<Integer, String> notes = new HashMap<>();

    static{
        notes.put(0,"C");
        notes.put(1,"#C");
        notes.put(2,"D");
        notes.put(3,"#D");
        notes.put(4,"E");
        notes.put(5,"F");
        notes.put(6,"#F");
        notes.put(7,"G");
        notes.put(8,"#G");
        notes.put(9,"A");
        notes.put(10,"#A");
        notes.put(11,"B");
    }

    public static String getNote(int code){
        int relativeCode = (code) % 12;
        return notes.get(relativeCode);
    }
}
