import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
    private JMenuItem open;
    private JMenuItem newFile;

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
        open = new JMenuItem("Open");
        newFile = new JMenuItem("New");

        file.add(newFile);
        file.add(open);

        botones.add(save);
        botones.add(build);
        botones.add(play);

        menu.add(file);
        menu.add(view);
        menu.add(help);



        addListeners();

    }

    private void addListeners(){
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    private void save(){
        try {
            Files.write(opened, texto.getText().getBytes());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"no se ha podido guardar el archivo");
            e.printStackTrace();
        }
    }

    private void open(){
        opened=Path.of(new JFileChooser().getSelectedFile().getAbsolutePath());
        try {

            File archivo = new File(String.valueOf(opened));
            String textoString;
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            while((textoString=br.readLine())!=null){
                texto.setText(texto.getText()+"\n"+textoString);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
