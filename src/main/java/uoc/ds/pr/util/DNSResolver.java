package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.Set;
import edu.uoc.ds.adt.sequential.SetLinkedListImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.DNSEntry;

import java.net.URI;

public class DNSResolver {

    //Atributs
    private Set<DNSEntry> dnsEntries;

    //Constructor
    public DNSResolver(){
        this.dnsEntries = new SetLinkedListImpl<>();
    }

    //Afegir una entrada
    public void add(DNSEntry dnsEntry){
        dnsEntries.add(dnsEntry);
    }

    //Cercar una entrada segons el paràmetre host
    public DNSEntry getDNSEntry(String host){

        //Entrades DNS
        Iterator<DNSEntry> iteratorDNSEntry = dnsEntries.values();

        //Cerquem segons el host
        while (iteratorDNSEntry.hasNext()){
            DNSEntry entry = iteratorDNSEntry.next();

            if(entry.getHost().equals(host)){
                return entry;
            }
        }

        return null;
    }

    //Afegir entrades
    public void addAllDNSEntry(Set<DNSEntry> entries) {
        //Entrades
        Iterator<DNSEntry> iteratorDNSEntry = entries.values();

        while(iteratorDNSEntry.hasNext()){
            dnsEntries.add(iteratorDNSEntry.next());
        }
    }

    //Obtenir el host d'una URL
    public static String extractHost(String url){
        //Comprovem la url
        if(url == null | url.isEmpty()){
            return null;
        }

        try{
            String entireUrl = url;

            //Afegim el protocol http si és necessari
            if(!url.startsWith("http://") && !url.startsWith("https://")) {
                entireUrl = "https://" + url;
            }

            return URI.create(entireUrl).getHost();

        } catch(IllegalArgumentException e){
            return null;
        }
    }

}
