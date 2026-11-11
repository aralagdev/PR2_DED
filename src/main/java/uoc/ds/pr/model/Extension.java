package uoc.ds.pr.model;
import edu.uoc.ds.adt.sequential.LinkedList;

public class Extension {

    //Atributs
    private final String extensionId;
    private String name;
    private String packagePath;
    private String version;

    //Llista enllaçada de comentaris
    private LinkedList<String> comments;

    //Constructor
    public Extension(String extensionId, String name, String packagePath, String version){
        this.extensionId = extensionId;
        this.name = name;
        this.packagePath = packagePath;
        this.version = version;
        this.comments = new LinkedList<>();
    }

    //Getters
    public String getExtensionId(){
        return extensionId;
    }

    public String getName(){
        return name;
    }

    public String getPackagePath(){
        return packagePath;
    }

    public String getVersion(){
        return version;
    }

    public LinkedList<String> getComments(){
        return comments;
    }

    //Actualitzar extensió amb noves dades
    public void updateExtension(String name, String packagePath, String version){
        this.name = name;
        this.packagePath = packagePath;
        this.version = version;
    }

    //Gestionar els comentaris

    //Afegir un comentari
    public void addComment(String comment){
        comments.insertEnd(comment);
    }

    //Comptar els comentaris de l'extensió
    public int countComments(){
        return comments.size();
    }







}
