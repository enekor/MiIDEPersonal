import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IdeTriste extends JFrame {
    private JPanel principal;
    private JSplitPane SplitVertical;
    private JTextPane Terminal;
    private JTree carpetas;
    private JTextArea texto;
    private JSplitPane SplitHorizontal;
    private JPanel panelTop;
    private JPanel botones;

    private JButton play;
    private JButton build;
    private JButton save;
    private JMenuBar menu;
    private JMenuItem file;
    private JMenuItem view;
    private JMenuItem help;

    private Path opened;

    IdeTriste(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }


    public void init(){
        play = new JButton();
        build = new JButton();
        save = new JButton();
        menu = new JMenuBar();
        file = new JMenuItem("file");
        view = new JMenuItem("view");
        help = new JMenuItem("help");

        botones.add(save);
        botones.add(build);
        botones.add(play);

        menu.add(file);
        menu.add(view);
        menu.add(help);

        this.pack();

        addListeners();

    }

    private void addListeners(){

    }

    private void save(){
        try {
            Files.write(opened, texto.getText().getBytes());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"no se ha podido guardar el archivo");
            e.printStackTrace();
        }
    }
}
