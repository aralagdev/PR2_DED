package uoc.ds.pr.model;

public class Bookmark {

    //Atributs
    private String title;
    private String url;
    private String path;

    //Constructor
    public Bookmark(String title, String url, String path){
        this.title = title;
        this.url = url;
        this.path = path;
    }

    //Getters
    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
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
