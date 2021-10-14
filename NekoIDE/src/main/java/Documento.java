import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Data
public class Documento {

    private int id;
    private String contenido="";
    private String path;
    private String nombre;
    private String lenguaje;
    private Path uri;
    File file=null;

    public Documento(File archivo,String name, String language,int pos) throws IOException {
      this.path = archivo.getAbsolutePath();
      this.file = archivo;
      this.nombre = name;
      this.lenguaje = language;
      this.id = pos;
      uri = Path.of(path);

      setContent();
    }

    private void setContent() throws IOException {
        List<String> lineas = Files.readAllLines(uri);
        lineas.forEach(s -> contenido+=s+"\n");
    }
}
