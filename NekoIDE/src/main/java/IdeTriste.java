import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

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
    private int documentoAbierto;
    private ArrayList<Documento> documentos = new ArrayList<>();


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
                try {
                    open();
                }catch(IOException g){
                    g.printStackTrace();
                }
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

        newFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    newFile();
                }catch(IOException f){
                    f.printStackTrace();
                }
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

    private void open() throws IOException{
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showOpenDialog(this);

        if (returnValue==JFileChooser.APPROVE_OPTION){
            documentos.add(new Documento(fc.getSelectedFile(),fc.getSelectedFile().getName(),"java",documentos.size()+1));
            documentoAbierto=documentos.size();
            texto.setText(documentos.get(documentoAbierto-1).getContenido());
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
        try {
            String comando = "cmd.exe /C java "+documentos.get(documentoAbierto-1).getPath();
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

    private void newFile() throws IOException {
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showSaveDialog(this);

        if (returnValue==JFileChooser.APPROVE_OPTION){
            documentos.add(new Documento(fc.getSelectedFile(),fc.getSelectedFile().getName(),"java",documentos.size()+1));
            documentoAbierto=documentos.size();
            texto.setText(documentos.get(documentoAbierto).getContenido());
        }
    }


}
