package uoc.ds.pr.pr2;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DNSResolver;

import java.time.LocalDateTime;

public class BrowserPR2Impl implements Browser, BrowserHelper{

    //Perfils d'usuaris
    private UserProfile[] userProfiles;
    private int totalUsers;

    //Total d'extensions
    private int totalExtensions;

    //Pàgina web més visitada
    private WebPage mostVisitedWebPage;

    //Pàgines web
    private LinkedList<WebPage> webPages;

    //Eina DNS
    private DNSResolver dnsResolver;

    public BrowserPR2Impl(){
        userProfiles = new UserProfile[MAX_USERS_ACCOUNTS];
        totalUsers = 0;
        totalExtensions = 0;
        dnsResolver = new DNSResolver();
        mostVisitedWebPage = null;
        webPages = new LinkedList<>();
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
            if (totalUsers < MAX_USERS_ACCOUNTS) {
                userProfiles[totalUsers] = new UserProfile(userId, name, user, pass);
                totalUsers++;
            }
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

        //Obtenim el marcador
        Bookmark bookmark = getBookmark(userId, url);

        //Si el trobem l'actualitzem, sinó l'afegim
        if (bookmark != null) {
            bookmark.setTitle(title);
            bookmark.setPath(path);
        } else {
            bookmark = new Bookmark(title, url, path);
            userProfile.getBookmarks().insertEnd(bookmark);
        }

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

        //Cerquem la configuració
        ConfigurationOption confOpt = getConfigurationOption(userId, coId);

        if (confOpt == null) {
            // Si no existeix, la creem
            confOpt = new ConfigurationOption(coId, property, value);

            ConfigurationOption[] configurations = userProfile.getConfigurations();

            //L'afegim a un espai disponible
            for (int i = 0; i < configurations.length; i++) {
                if (configurations[i] == null) {
                    configurations[i] = confOpt;
                    return;
                }
            }
        } else {
            // Si existeix, l'actualitzem
            confOpt.setProperty(property);
            confOpt.setValue(value);
        }
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
            totalExtensions++;
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
    @Override
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



    //Visita una pàgina web
    @Override
    public void visitWeb(String userId, String url, LocalDateTime dateTime) throws UserProfileNotFoundException, DNSResolverException {
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Extraiem el host de la URL
        String host = DNSResolver.extractHost(url);

        //Comprovem l'existència al DNS
        DNSEntry dnsEntry = dnsResolver.getDNSEntry(host);

        //Si no la trovem llançem una excepció
        if(dnsEntry == null){
            throw new DNSResolverException("DNS entry " + host + " not found");
        }

        //Busquem la pàgina a la llista global
        WebPage webPage = getWebPage(url);

        //Si no existeix, la creem i l'afegim
        if (webPage == null) {
            webPage = new WebPage(url);
            webPages.insertEnd(webPage);
        }

        //Incrementem el nombre global de visites
        webPage.incrementVisits();

        //Creem una entrada individual a l'historial
        HistoryWebPage historyWebPage = new HistoryWebPage(url, dateTime);

        //Afegim la visita a l'historial de l'usuari
        userProfile.getHistory().insertEnd(historyWebPage);

        // Actualitzem l'historial de navegació
        userProfile.getNavigationHistory().visit(historyWebPage);

        // Actualitzem la pàgina més visitada de l'usuari
        int userVisits = numVisitsUser(userProfile, url);

        if (userProfile.getMostVisitedWebPage() == null ||
                userVisits > numVisitsUser(
                        userProfile,
                        userProfile.getMostVisitedWebPage().getUrl())) {

            userProfile.setMostVisitedWebPage(historyWebPage);
        }

        // Actualitzem la pàgina més visitada globalment
        if (mostVisitedWebPage == null ||
                webPage.getVisits() > mostVisitedWebPage.getVisits()) {

            mostVisitedWebPage = webPage;
        }

    }

    //Navegació cap enrere
    @Override
    public WebPage backWeb(String userId) throws UserProfileNotFoundException{
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Retrocedim a l'historial de navegació
        return userProfile.getNavigationHistory().back();
    }

    //Navegació cap endevant
    @Override
    public WebPage forwardWeb(String userId) throws UserProfileNotFoundException{
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Retrocedim a l'historial de navegació
        return userProfile.getNavigationHistory().forward();
    }

    //Retora la pàgina més visitada globalment
    @Override
    public WebPage getMostVisitedWeb() throws NoWebPagesFoundException {
        if(mostVisitedWebPage == null){
            throw new NoWebPagesFoundException();
        }

        return mostVisitedWebPage;
    }

    //Retora la pàgina més visitada per l'usuari
    @Override
    public WebPage getMostVisitedWebByUser(String userId) throws UserProfileNotFoundException, NoWebPagesFoundException {
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        if(userProfile.getMostVisitedWebPage() == null){
            throw new NoWebPagesFoundException();
        }

        return userProfile.getMostVisitedWebPage();
    }

    //Retorna l'historial d'un usuari concret
    @Override
    public Iterator<HistoryWebPage> getHistoryWebsByUser(String userId) throws UserProfileNotFoundException, NoHistoryFoundException{
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Si no té historial, retornem una excepció
        if(userProfile.getHistory().isEmpty()){
            throw new NoHistoryFoundException();
        }

        return userProfile.getHistory().values();
    }

    //Retorna els marcadors d'un usuari concret
    @Override
    public Iterator<Bookmark> getAllBookmarksByUser(String userId) throws UserProfileNotFoundException, NoBookmarksFoundException{
        //Obtenim el perfil d'usuari
        UserProfile userProfile = getUserProfile(userId);

        //Si no existeix retornem una excepció
        if(userProfile == null){
            throw new UserProfileNotFoundException("User profile " + userId + " not found");
        }

        //Si no té historial, retornem una excepció
        if(userProfile.getBookmarks().isEmpty()){
            throw new NoBookmarksFoundException();
        }

        return userProfile.getBookmarks().values();
    }

    //Retorna el nombre total de comptes d'usuari
    @Override
    public int getTotalUsersAccounts() {
        return totalUsers;
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
        return totalExtensions;
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

    //Retorna el nombre d'entrada a l'historial de l'usuari segons id
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


    //MÈTODES AUXILIARS

    //Comptar les visites de l'usuari
    private int numVisitsUser(UserProfile userProfile, String url){
        int visits = 0;
        Iterator<HistoryWebPage> it = userProfile.getHistory().values();

        while (it.hasNext()) {
            HistoryWebPage historyWebPage = it.next();

            if (historyWebPage.getUrl().equals(url)) {
                visits++;
            }
        }

        return visits;
    }

    private WebPage getWebPage(String url) {
        Iterator<WebPage> it = webPages.values();

        while (it.hasNext()) {
            WebPage webPage = it.next();

            if (webPage.getUrl().equals(url)) {
                return webPage;
            }
        }

        return null;
    }


}
