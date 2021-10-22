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
import java.util.HashMap;
import java.util.Map;

public class IdeTriste extends JFrame {
    private JPanel principal;
    private JSplitPane SplitVertical;
    private JTextArea texto;
    private JTextArea Terminal;
    private JSplitPane SplitHorizontal;
    private JPanel panelTop;
    private JPanel botones;
    private JScrollPane scrollLateral;
    private JList listaScroll;

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

    private int opened;
    private Map<Integer,Documento> documentos = new HashMap<>();

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
        erase = new JMenuItem("Undo");
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
                if(documentos.isEmpty()){
                    try {
                        saveAs();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    save();
                }
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
                try {
                    saveAs();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
                    saveAs();
                    texto.setText("");
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
                    panel.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    panel.setVisible(true);
                    lista.addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            abrir(lista.getSelectedIndex());
                            panel.dispose();
                        }
                    });
                }
            }
        });
        listaScroll.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                abrir(listaScroll.getSelectedIndex());
            }
        });
    }

    /*
     * Metodos funcionales
     */

    private void build(){
        try {
            String comando = "cmd.exe /C javac "+opened;
            Process proceso = Runtime.getRuntime().exec(comando);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play(){
        if(documentos.isEmpty()){
            try {
                saveAs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String comando = "cmd.exe /C java "+documentos.get(opened).getPath();
            Terminal.setText("ejecutando");
            Process proceso = Runtime.getRuntime().exec(comando);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream()));
            String line;
            while((line=br.readLine())!=null){
                Terminal.setText(Terminal.getText()+"\n"+line+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * abrir un documento desde la lista de documentos abiertos
     * @param id del documento seleccionado
     */
    private void abrir (int id){
        documentos.get(opened).setContenido(texto.getText());
        String a = documentos.get(id).getContenido();
        texto.setText(a);
        opened = id;
    }

    /**
     * abrir un documento desde un explorador de archivos
     * @throws IOException
     */
    private void open() throws IOException{
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showOpenDialog(this);

        if (returnValue==JFileChooser.APPROVE_OPTION){
            Documento d = new Documento(fc.getSelectedFile(),fc.getSelectedFile().getName(),"java",documentos.size()+1);
            documentos.put(documentos.size(), d);
            opened =documentos.size()-1;
            createList();
            texto.setText(documentos.get(opened).getContenido());
        }
    }

    private void saveAs() throws IOException{
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showSaveDialog(this);

        if(returnValue==JFileChooser.APPROVE_OPTION){
            File archivo = fc.getSelectedFile();
            saveTextToFile(archivo);
            Documento doc = new Documento(archivo,archivo.getName(),"java",documentos.size()+1);
            documentos.put(documentos.size(), doc);
            opened = documentos.size()-1;
            createList();
            JOptionPane.showMessageDialog(null,"guardado");
        }

    }

    private void save(){
        File archivo = new File(documentos.get(opened-1).getPath());
        saveTextToFile(archivo);
        JOptionPane.showMessageDialog(null, "guardado");
    }

    private void saveTextToFile(File archivo){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(texto.getText());
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createList(){
        String[] list = new String[documentos.size()];
        for(int i = 0; i < documentos.size(); i++){
            list[i]=documentos.get(i).getNombre();
        }
        lista = new JList(list);
        listaScroll.setListData(list);
    }
}
