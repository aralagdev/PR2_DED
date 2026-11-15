package uoc.ds.pr.model;

import java.time.LocalDateTime;

public class HistoryWebPage extends WebPage {

    //Atributs
    private LocalDateTime dateTime;

    //Constructor
    public HistoryWebPage(String url, LocalDateTime dateTime){
        super(url);
        this.dateTime = dateTime;
    }

    //Getters
    public LocalDateTime getDateTime() {
        return dateTime;
    }

}
