package uoc.ds.pr.model;

public class ConfigurationOption {

    //Atributs
    private String coId;
    private String property;
    private String value;

    //Constructor
    public ConfigurationOption(String coId, String property, String value){
        this.coId = coId;
        this.property = property;
        this.value = value;

    }

    //Getters
    public String getCoId(){
        return coId;
    }

    public String getProperty(){
        return property;
    }

    public String getValue(){
        return value;
    }

    //Setters
    public void setProperty(String property){
        this.property = property;
    }

    public void setValue(String value){
        this.value = value;
    }
}
