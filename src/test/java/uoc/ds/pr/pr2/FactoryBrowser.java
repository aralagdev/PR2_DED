package uoc.ds.pr.pr2;

import uoc.ds.pr.exceptions.DSException;

public class FactoryBrowser {

    public static Browser getComputerProjects() throws DSException {
        Browser browser;
        browser = new BrowserPR2Impl();
        return browser;
    }



}