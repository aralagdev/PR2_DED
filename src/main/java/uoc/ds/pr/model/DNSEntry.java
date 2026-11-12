package uoc.ds.pr.model;

public class DNSEntry {

    //Atributs
    private String host;
    private String ip;

    //Constructor
    public DNSEntry(String host, String ip){
        this.host = host;
        this.ip = ip;
    }

    //Getters
    public String getHost(){
        return host;
    }

    public String getIp(){
        return ip;
    }
}
