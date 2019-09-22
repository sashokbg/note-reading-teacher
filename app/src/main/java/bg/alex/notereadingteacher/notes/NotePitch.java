package bg.alex.notereadingteacher.notes;

public enum NotePitch {
    C       (0, 0,  "C"),
    C_SHARP (1, 0,  "#C"),
    D       (2, 1,  "D"),
    D_SHARP (3, 1,  "#D"),
    E       (4, 2,  "E"),
    F       (5, 3,  "F"),
    F_SHARP (6, 3,  "#F"),
    G       (7, 4,  "G"),
    G_SHARP (8, 4,  "#G"),
    A       (9, 5,  "A"),
    A_SHARP (10,5,  "#A"),
    B       (11,6,  "B");

    private int pitchCode;
    private String label;
    private int position;

    NotePitch(int pitchCode, int position, String label) {
        this.pitchCode = pitchCode;
        this.label = label;
        this.position = position;
    }

    public static NotePitch fromCode(int pitchCode){
        for(NotePitch notePitch : NotePitch.values()){
            if(notePitch.pitchCode == pitchCode){
                return notePitch;
            }
        }

        return null;
    }

    public int getPitchCode() {
        return pitchCode;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Note position relative to the staff.
     * Ex: C and #C have the same position on the staff so they both have position 0
     */
    public int getPosition() {
        return position;
    }
}
