package uoc.ds.pr.pr2;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.ExtensionNotFoundException;
import uoc.ds.pr.exceptions.UserProfileNotFoundException;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DNSResolver;

public class BrowserPR2Impl implements Browser, BrowserHelper{

    //Perfils d'usuaris
    private UserProfile[] userProfiles;
    private int totalUsers;

    //Eina DNS
    private DNSResolver dnsResolver;

    public BrowserPR2Impl(){
        userProfiles = new UserProfile[MAX_USERS_ACCOUNTS];
        totalUsers = 0;
        dnsResolver = new DNSResolver();
    }

    //Afegim un perfil d'usuari nou
    @Override
    public void addUserProfile(String userId, String name, String user, String pass){
        //Comprovem si el perfil ja existeix i actualitzem les dades
        UserProfile  userprofile = getUserProfile(userId);

        if(userprofile != null){
            //Nom
            userprofile.setName(name);
            //Usuari
            userprofile.setUser(user);
            //Contrasenya
            userprofile.setPass(pass);
        } else {
            //Si no existeix, creem un perfil nou
            userProfiles[totalUsers] = new UserProfile(userId, name, user, pass);
            totalUsers++;
        }

    }

    //Afegir un nou marcador a l'usuari
    @Override
    public void addBookmark(String userId, String title, String url, String path) throws UserProfileNotFoundException {
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix llançem l'excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Creem el marcador i l'afegim als marcadors de l'usuari
        Bookmark bookmark = new Bookmark(title, url, path);
        userProfile.getBookmarks().insertEnd(bookmark);

    }

    //Actualitza una opció de configuració de l'usuari
    @Override
    public void editConfigurationOption(String coId, String userId, String property, String value) throws UserProfileNotFoundException {
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Obtenim la opció segons id
        ConfigurationOption confOpt = getConfigurationOption(userId, coId);

        //Actualitzem la opció
        confOpt.setProperty(property);
        confOpt.setValue(value);
    }

    //Afegeix una extensió i l'assocía amb un usuari
    @Override
    public void addExtension(String extensionId, String userId, String name, String packagePath, String version) throws UserProfileNotFoundException, ExtensionNotFoundException {
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Obtenim l'extensió
        Extension extension = getExtension(extensionId);

        //Si l'extensió existeix, s'actualitza. Sinó, es crea
        if(extension == null){
            extension = new Extension(extensionId, name, packagePath, version);
        } else {
            extension.updateExtension(name, packagePath, version);
        }

        //Comprovem si l'usuari té l'extensió
        Iterator<Extension> it = userProfile.getExtensions().values();
        boolean hasExtension = false;

        while (it.hasNext()) {
            if (it.next().getExtensionId().equals(extensionId)) {
                hasExtension = true;
                break;
            }
        }

        //Si no la té, l'afegim a les seves extensions
        if(!hasExtension){
            userProfile.getExtensions().insertEnd(extension);
        }

    }

    //Afegeix un comentari a l'extensió d'un usuari
    public void addCommentToExtension(String extensionId, String userId, String comment) throws UserProfileNotFoundException, ExtensionNotFoundException{
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Obtenim l'extensió
        Extension extension = getExtension(extensionId);

        //Si no existeix retornem una excepció
        if(extension == null){
            throw new ExtensionNotFoundException("Extension " + extensionId + " not found");
        }

        extension.addComment(comment);

    }


    @Override
    public BrowserHelper getHelper() {
        return this;
    }

    //Busca l'usuari a l'array d'usuaris segons id
    @Override
    public UserProfile getUserProfile(String id){
        for(int i = 0; i < totalUsers; i++){
            if(userProfiles[i].getUserId().equals(id)){
                return userProfiles[i];
            }
        }
        return null;
    }

    //Retorna el nombre de perfils d'usuari
    @Override
    public int numUserProfiles(){
        return totalUsers;
    }

    //Retorna el nombre de marcadors d'un usuaris segons id
    @Override
    public int numBookmarksByUser(String id){
        //Obtenim l'usuari
        UserProfile userProfile = getUserProfile(id);
        //Retornem el nombre de marcadors
        return userProfile.getBookmarks().size();
    }

    //Obtenim un marcador concret d'un usuari segons id
    @Override
    public Bookmark getBookmark(String userId, String url){
        //Obtenim l'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Obtenim un iterador dels marcadors de l'usuari
        Iterator<Bookmark> it = userProfile.getBookmarks().values();

        //Cerquem el marcador segons url
        while (it.hasNext()) {
            Bookmark bookmark = it.next();

            if (bookmark.getUrl().equals(url)) {
                return bookmark;
            }
        }

        return null;
    }

    //Obtenim una opció de configuració concreta d'una usuari segons id
    @Override
    public ConfigurationOption getConfigurationOption(String userId, String confId){
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Busquem la configuració segons id
        for(ConfigurationOption confOption : userProfile.getConfigurations()) {
            if(confOption != null && confOption.getCoId().equals(confId)) {
                return confOption;
            }
        }

        return null;
    }

    //Obtenim la web consultada actual per l'usuari
    @Override
    public WebPage getCurrentWeb(String userId){
        //Obtenim l'usuari
        UserProfile userProfile = getUserProfile(userId);

        if (userProfile == null) {
            return null;
        }
        //Retornem el la pàgina web actual
        return userProfile.getNavigationHistory().getCurrentPage();
    }

    //Obtenim el nombre de configuracions de l'usuari
    @Override
    public int numConfigurationOptionsByUser(String userId){
        //Obtenim l'usuari i les seves configuracions
        UserProfile userProfile = getUserProfile(userId);
        int numConf = 0;
        ConfigurationOption[] configurations = userProfile.getConfigurations();

        //Comptem les configuracions i retornem el nombre
        for (ConfigurationOption configuration : configurations) {
            if (configuration != null) {
                numConf++;
            }
        }

        return numConf;
    }

    //Obtenim el nombre d'extensions de l'usuari
    @Override
    public int numExtensionsByUser(String userId) {
        //Obtenim l'usuari
        UserProfile userProfile = getUserProfile(userId);
        //Retornem el nombre d'extensions
        return userProfile.getExtensions().size();
    }

    //Obtenim el nombre d'extensions de tots els usuaris
    public int numExtensions(){
        int numExtTotal = 0;
        int numExUser = 0;
        //Comptem el nombre d'extensions de cada usuari i sumem al total
        for(int i = 0; i < totalUsers; i++){
            numExUser = userProfiles[i].getExtensions().size();
            numExtTotal = numExtTotal + numExUser;
        }

        return numExtTotal;
    }

    //Busquem una extensió segons el seu id
    @Override
    public Extension getExtension(String extensionId){

        for(int i = 0; i < totalUsers; i++){
            //Obtenim l'usuari
            UserProfile userProfile = userProfiles[i];

            //Creem un iterador de les extensions
            Iterator<Extension> it = userProfile.getExtensions().values();

            //Cerquem l'extensió per id i la retornem
            while (it.hasNext()) {
                Extension extension = it.next();

                if (extension.getExtensionId().equals(extensionId)) {
                    return extension;
                }
            }
        }

      return null;
    }

    //Retorna el nombre de comentaris d'una extensió per id
    @Override
    public int numCommentsByExtension(String extensionId){
        //Cerquem l'extensió per id
        Extension extension = getExtension(extensionId);

        //Si l'extensió no existeix retornem 0
        if(extension == null){
            return 0;
        }

        return extension.countComments();
    }

    //Retorna el nombre d'entradade a l'historial de l'usuari segons id
    @Override
    public int numHistoryByUser(String userId){
        //Obtenim l'usuari
        UserProfile userProfile = getUserProfile(userId);
        //Si no existeix retornem 0
        if(userProfile == null){
            return 0;
        }

        return userProfile.getHistory().size();
    }

    //Retorna l'ultima web visitada per l'usuari
    @Override
    public String getLastVisitedWeb(String userId){
        //Obtenim l'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix o la història es buida retornem null
        if(userProfile == null || userProfile.getHistory().isEmpty()){
            return null;
        }

        return userProfile.getHistory().peekLast().getUrl();
    }

    //Afegeix una entrada DNS
    @Override
    public void addDNSEntry(String host, String ip){
        //Creem l'entrada DNS
        DNSEntry dnsEntry = new DNSEntry(host, ip);
        //Afegim l'entrada
        dnsResolver.add(dnsEntry);
    }

    //Obté el número d'entrades DNS
    @Override
    public int numDNSEntries() {
        return dnsResolver.numDNSEntries();
    }



}
