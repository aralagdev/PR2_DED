package uoc.ds.pr.model;
import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.pr2.Browser;
import uoc.ds.pr.pr2.NavigationHistory;

//Classe de perfil d'usuari
public class UserProfile {

    //Atributs usuari
    private final String userId;
    private String name;
    private String user;
    private String pass;

    //Informació de navegació de l'usuari

    //Marcadors
    private LinkedList<Bookmark> bookmarks;

    //Historial de navegació
    private LinkedList<HistoryWebPage> history;

    //Configuració
    private ConfigurationOption[] configurations;

    //Extensions
    private LinkedList<Extension> extensions;

    //Constructor
    public UserProfile(String userId, String name, String user, String pass){
        this.userId = userId;
        this.name = name;
        this.user = user;
        this.pass = pass;
        this.bookmarks = new LinkedList<>();
        this.history = new LinkedList<>();
        this.configurations = new ConfigurationOption[Browser.MAX_CONFIGURATIONS];
        this.extensions = new LinkedList<>();
    }

    //Getters
    public String getUserId(){
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUser(){
        return user;
    }

    public String getPass() {
        return pass;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
