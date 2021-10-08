import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class IdeTriste extends JFrame {
    private JPanel principal;
    private JSplitPane SplitVertical;
    private JTextArea Terminal;
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

    private String opened=null;

    IdeTriste(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();

    }


    public void init(){
        play = new JButton("play");
        build = new JButton("build");
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

        this.setPreferredSize(new Dimension(getMaximumSize()));

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
                save(file,false);
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
                guardarComo(false);
            }
        });

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });
        build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                build();
            }
        });

        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFile();
            }
        });
    }

    private void save(File archivo,boolean nuevo){
        if(!nuevo) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));

                bw.write(texto.getText());
                bw.flush();
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       JOptionPane.showMessageDialog(null,"Guardado");
       opened = archivo.getAbsolutePath();
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

            opened = archivo.getAbsolutePath();

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void guardarComo(boolean nuevo){
        JFileChooser fc = new JFileChooser();
        int selectVal = fc.showSaveDialog(this);
        File newFile = fc.getSelectedFile();
        save(newFile,nuevo);
    }

    private void build(){
        try {
            String comando = "cmd.exe /C javac "+opened;
            Process proceso = Runtime.getRuntime().exec(comando);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play(){
        if(opened==null){
            guardarComo(false);
        }
        try {
            String comando = "cmd.exe /C java "+opened;
            Terminal.setText("ejecutando");
            Process proceso = Runtime.getRuntime().exec(comando);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream()));
            String line;
            while((line=br.readLine())!=null){
                Terminal.setText("\n"+Terminal.getText()+"\n"+line+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nuevo(){
        guardarComo(true);
        texto.setText(null);
    }

    private void newFile(){
        if(texto.getText()!=null){
            int ans = JOptionPane.showOptionDialog(
                    null,"quieres guardar el archivo antes de crear uno nuevo?","Crear nuevo archivo",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,
                    null,null,null);
            if (ans==JOptionPane.YES_OPTION){
                if(opened!=null){
                    save(new File(opened),false);
                }else{
                    guardarComo(false);
                }
            }
        }
        JOptionPane.showMessageDialog(null,"donde quieres guardarlo");
        nuevo();
    }


}
