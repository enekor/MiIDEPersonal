import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        new IdeTriste().setVisible(true);
    }
}
