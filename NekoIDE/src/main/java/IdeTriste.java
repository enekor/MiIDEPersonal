import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
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
    private JMenu help;
    private JMenu edit;
    private JMenuItem open;
    private JMenuItem newFile;
    private JMenuItem saveas;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem erase;
    private JMenuItem about;
    private JMenuItem cut;
    private UndoManager um;
    private JMenuItem documents;
    private JMenuItem print;
    private JList lista;

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
        help = new JMenu("help");
        open = new JMenuItem("Open");
        newFile = new JMenuItem("New");
        saveas = new JMenuItem("save as");
        edit = new JMenu("edit");
        copy = new JMenuItem("copy");
        paste = new JMenuItem("paste");
        erase = new JMenuItem("Erase");
        about = new JMenuItem("about IDETriste");
        cut = new JMenuItem("cut");
        um = new UndoManager();
        documents = new JMenuItem("documents");
        print = new JMenuItem("print");

        file.add(newFile);
        file.add(open);
        file.add(saveas);
        file.add(print);

        help.add(about);

        edit.add(copy);
        edit.add(paste);
        edit.add(erase);
        edit.add(cut);

        botones.add(save);
        botones.add(build);
        botones.add(play);

        menu.add(file);
        menu.add(help);
        menu.add(edit);
        menu.add(documents);

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

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                texto.copy();
            }
        });

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                texto.paste();
            }
        });

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop d = Desktop.getDesktop();
                try {
                    d.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        erase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                texto.cut();
            }
        });

        texto.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                um.addEdit(e.getEdit());
            }
        });

        erase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(um.canUndo()){
                    um.undo();
                }
            }
        });

        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    texto.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });
        documents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(documentos.isEmpty()){
                    JOptionPane.showMessageDialog(null,"no hay documentos abiertos");
                }else {
                    JFrame panel = new JFrame();
                    panel.add(lista);
                    panel.pack();
                    panel.setVisible(true);
                    lista.addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            abrir(lista.getSelectedIndex());
                        }
                    });
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
            documentoAbierto=documentos.size()-1;
            texto.setText(documentos.get(documentoAbierto-1).getContenido());
        }
        nuevoDocumento();
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
            documentoAbierto=documentos.size()-1;
            texto.setText(documentos.get(documentoAbierto).getContenido());
        }
        nuevoDocumento();
    }

    private void nuevoDocumento(){
        Documentos d = Documentos.Documents(documentos.size(),documentos);
        lista = new JList(d.getDocumentos());

    }

    private void abrir (int id){
        documentos.get(documentoAbierto).setContenido(texto.getText());
        texto.setText(documentos.get(id).getContenido());
        documentoAbierto = id;
    }

}
