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
    private JMenu file;
    private JMenu view;
    private JMenu help;
    private JMenuItem open;
    private JMenuItem newFile;
    private JMenuItem saveas;

    private String opened;

    IdeTriste(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();

    }


    public void init(){
        play = new JButton();
        build = new JButton();
        save = new JButton("save");
        menu = new JMenuBar();
        file = new JMenu("file");
        view = new JMenu("view");
        help = new JMenu("help");
        open = new JMenuItem("Open");
        newFile = new JMenuItem("New");
        saveas = new JMenuItem("save as");

        file.add(newFile);
        file.add(open);
        file.add(saveas);

        botones.add(save);
        botones.add(build);
        botones.add(play);

        menu.add(file);
        menu.add(view);
        menu.add(help);

        this.setJMenuBar(menu);
        this.add(principal);
        this.pack();

        addListeners();

    }

    private void addListeners(){
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(opened);
                save(file);
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });

        saveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComo();
            }
        });

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pruebaprint();
            }
        });
    }

    private void save(File archivo){
       try{
           BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
           bw.write(texto.getText());
           bw.flush();
           bw.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
       JOptionPane.showMessageDialog(null,"Guardado");
    }

    private void open(){

        try {

            JFileChooser fc = new JFileChooser();
            File archivo=null;

            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int selectVal = fc.showOpenDialog(this);

            if(selectVal==JFileChooser.APPROVE_OPTION){
                archivo = fc.getSelectedFile();
                opened = archivo.getAbsolutePath();
                texto.setText(null);
            }


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

    private void guardarComo(){
        JFileChooser fc = new JFileChooser();
        int selectVal = fc.showSaveDialog(this);
        File newFile = fc.getSelectedFile();;
        save(newFile);
    }

    private void pruebaprint(){
        System.out.println(texto.getText());
    }
}
