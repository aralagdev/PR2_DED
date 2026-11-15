package uoc.ds.pr.model;

public class Bookmark extends WebPage{

    //Atributs
    private String title;
    private String path;

    //Constructor
    public Bookmark(String title, String url, String path){
        super(url);
        this.title = title;
        this.path = path;
    }

    //Getters
    public String getTitle(){
        return title;
    }

    public String getPath(){
        return path;
    }

    //Setters
    public void setTitle(String title){
        this.title = title;
    }

    public void setPath(String path){
        this.path = path;
    }

}
