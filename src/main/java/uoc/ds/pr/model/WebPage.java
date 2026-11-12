package uoc.ds.pr.model;

public class WebPage {

    //Atirbuts
    private String url;
    private int visits;

    //Constructor
    public WebPage(String url) {
        this.url = url;
        this.visits = 0;
    }

    //Getter
    public String getUrl(){
        return url;
    }

    public int getVisits() {
        return visits;
    }

    //Incrementem les visites
    public void incrementVisits(){
        visits++;
    }
}
