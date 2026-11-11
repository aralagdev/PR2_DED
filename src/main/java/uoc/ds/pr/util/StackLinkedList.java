package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.Stack;

//Un nou TAD que implementa una pila no fitada com a extensió d’una LinkedList i implementa la interfície Stack: StackLinkedList.
public class StackLinkedList<E> extends LinkedList<E> implements Stack<E> {

    //Insertar un element al cim de la pila
    @Override
    public void push(E element){
        insertEnd(element);
    }

    //Eliminar l'element del cim de la pila i obtenir-lo
    @Override
    public E pop(){
        return deleteLast();
    }

    //Obtenir l'últim element de la pila sense eliminar-lo
    @Override
    public E peek(){
        return peekLast();
    }
}
