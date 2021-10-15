import java.util.List;

public class Documentos {

    private static int numDocumentos;
    private static List<Documento> documentosList;
    private static Documento[] documentos;
    private static Documentos doc =null;

     private Documentos(int capacidad, List<Documento> lista){
         this.numDocumentos = capacidad;
         this.documentosList = lista;
     }

     public static Documentos Documents(int capacidad, List<Documento> lista){
         if (doc==null){
             doc = new Documentos(capacidad,lista);
         }
         else{
             doc.setNumDocumentos(capacidad);
             doc.setDocumentosList(lista);
         };
         createArray();
         return doc;
     }

    private void setDocumentosList(List<Documento> lista) {
         documentosList = lista;
    }

    private void setNumDocumentos(int capacidad) {
         this.numDocumentos = capacidad;
    }

    public Documento[] getDocumentos(){return documentos;}

    private static void createArray(){
         documentos = new Documento[numDocumentos];
         for (int i=0;i<numDocumentos;i++){
             documentos[i]=documentosList.get(i);
         }
     }
}
