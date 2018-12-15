package bg.alex.notereadingteacher;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        byte test = -80;

        System.out.println(String.format("0x%08X", (test & 0xFF)));

//        System.out.println(Integer.toBinaryString(test));
//        System.out.println(Integer.toBinaryString(7));
//        int i = test & 7;
//        System.out.println(Integer.toBinaryString(i));
    }
}