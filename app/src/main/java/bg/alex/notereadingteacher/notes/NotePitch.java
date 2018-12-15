package bg.alex.notereadingteacher.notes;

public enum NotePitch {
    C(0,"C"),
    C_SHARP(1,"#C"),
    D(2,"D"),
    D_SHARP(3,"#D"),
    E(4,"E"),
    F(5,"F"),
    F_SHARP(6,"#F"),
    G(7,"G"),
    G_SHARP(8,"#G"),
    A(9,"A"),
    A_SHARP(10,"#A"),
    B(11,"B");

    private int pitchCode;
    private String label;

    NotePitch(int pitchCode, String label) {
        this.pitchCode = pitchCode;
        this.label = label;
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
}
