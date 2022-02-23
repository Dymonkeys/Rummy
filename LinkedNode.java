/*
 * File: LinkedNode.java
 * Author: David Neufeld
 * Created Date: Wed Oct 27 2021 at 4:02:51 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
public class LinkedNode<T> {
    public int sortValue;
    public T element;
    public LinkedNode<T> next;
    public LinkedNode<T> last;
    public LinkedNode(T element){
        this.element=element;
    }
    public void recursivePrint(){
        System.out.println(element.toString());
        //System.out.println(sortValue);
        if(next!=null) next.recursivePrint();
    }
}
