package uoc.ds.pr.pr2;

import edu.uoc.ds.adt.sequential.Stack;
import uoc.ds.pr.model.WebPage;
import uoc.ds.pr.util.StackLinkedList;


public class NavigationHistory {

    //Piles de navegació
    private Stack<WebPage> backStack;
    private Stack<WebPage> forwardStack;

    //Pàgina actual
    private WebPage currentPage;

    public NavigationHistory(){
        this.backStack = new StackLinkedList<>();
        this.forwardStack = new StackLinkedList<>();
        this.currentPage = null;
    }

    //Getters
    public WebPage getCurrentPage(){
        return currentPage;
    }

    //Visitar una pàgina
    public void visit(String url){

        //La pàgina actual es enviada a la pila de retrocés
        if(currentPage != null){
            backStack.push(currentPage);
        }

        //Es crea la nova pàgina actual
        currentPage = new WebPage(url);

        //Buidem la pila d'avançament
        while(!forwardStack.isEmpty()){
            forwardStack.pop();
        }
    }

    //Navegació de retrocés
    public WebPage back(){

        //Comprovem que la pila no es buida
        if(backStack.isEmpty()){
            return null;
        }

        //La pàgina actual passa a la pila d'avançament
        forwardStack.push(currentPage);

        //La nova pàgina actual s'obté de la pila de retrocés
        currentPage = backStack.pop();

        return currentPage;
    }

    //Navegació d'avançament
    public WebPage forward(){

        //Comprovem que la pila no es buida
        if(forwardStack.isEmpty()){
            return null;
        }

        //La pàgina actual passa a la pila de retrocés
        backStack.push(currentPage);

        //La nova pàgina actual s'obté de la pila d'avançament
        currentPage = forwardStack.pop();

        return currentPage;
    }
}
