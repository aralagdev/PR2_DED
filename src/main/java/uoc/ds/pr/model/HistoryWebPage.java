package uoc.ds.pr.model;

import java.time.LocalDateTime;

public class HistoryWebPage {

    //Atributs
    private WebPage webPage;
    private LocalDateTime dateTime;

    //Constructor
    public HistoryWebPage(WebPage webPage, LocalDateTime dateTime){
        this.webPage = webPage;
        this.dateTime = dateTime;
    }

    //Getters
    public String getUrl(){
        return webPage.getUrl();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public WebPage getWebPage(){
        return webPage;
    }
}
