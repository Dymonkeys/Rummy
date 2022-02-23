
/*
 * File: LinkedList.java
 * Author: David Neufeld
 * Created Date: Wed Oct 27 2021 at 4:01:26 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
public class LinkedList<T> {
    public LinkedNode<T> head;
    public LinkedNode<T> tail;
    public int size=0;
    public void add(T element){
        LinkedNode<T> addition = new LinkedNode<>(element);
        try {
            addition.sortValue=Integer.parseInt(element.toString().split(",")[0]);
        } catch (Exception e) {
            addition.sortValue=0;
        }
        if(size==0){//Empty List
            head=addition;
            tail=addition;
            size=1;
            return;
        }
        //size is at least 1
        tail.next=addition;
        addition.last=tail;
        tail=addition;
        size++;
    }
    public void addWithValue(T element, int value){
        LinkedNode<T> addition = new LinkedNode<>(element);
        addition.sortValue=value;
        if(size==0){//Empty List
            head=addition;
            tail=addition;
            size=1;
            return;
        }
        //size is at least 1
        tail.next=addition;
        addition.last=tail;
        tail=addition;
        size++;
    }
    public void mergeSort(){
        if(head==null||size<=1) return;
        LinkedList<T> front = split(0,size/2);
        LinkedList<T> back = split(size/2,size);
        //System.out.println(toString()+" -> "+front.toString()+" and "+back.toString());
        front.mergeSort();
        back.mergeSort();
        head=null;
        tail=null;
        size=0;
        //System.out.println(front.head.sortValue+"<"+back.head.sortValue);
        LinkedNode<T> f = front.head;
        LinkedNode<T> b = back.head;
        while(f!=null||b!=null){
            if(f==null){
                addWithValue(b.element, b.sortValue);
                b=b.next;
            }
            else if(b==null){
                addWithValue(f.element, f.sortValue);
                f=f.next;
            }
            else{
                if(f.sortValue<b.sortValue){
                    addWithValue(f.element, f.sortValue);
                    f=f.next;
                } else {
                    addWithValue(b.element, b.sortValue);
                    b=b.next;
                }
            }
        }
    }
    public LinkedList<T> split(int start, int end){
        LinkedList<T> out = new LinkedList<T>();
        int index=0;
        for(LinkedNode<T> n = head; n!=null; n=n.next){
            if(index>=start&&index<end){
                out.addWithValue(n.element, n.sortValue);
            }
            index++;
        }
        return out;
    }
    public T get(int index){
        if(index>=size||index<0) return null; //Node index out of bounds
        if(index==size-1) return tail.element;
        LinkedNode<T> node = head;
        for(int i=0;i<index;i++){
            node=node.next;
        }
        return node.element;
    }
    public void removeNode(LinkedNode<T> node){
        if(size==0) return;
        if(size==1&&node==head){
            head=null;
            tail=null;
            size=0;
            return;
        }
        if(node==head){
            head.next.last=null;
            head=head.next;
            size--;
            return;
        }
        if(node==tail){
            tail.last.next=null;
            tail=tail.last;
            size--;
            return;
        }
        for(LinkedNode<T> n=head.next;n!=null;n=n.next){
            if(n==node){
                n.next.last=n.last;
                n.last.next=n.next;
                size--;
                return;
            }
        }
    }
    public void removeElement(T inp){
        if(head.element==inp&&tail.element==inp){
            head=null;
            tail=null;
            size=0;
            return;
        }
        if(head.element==inp){
            head=head.next;
            head.last=null;
            size--;
            return;
        }
        if(tail.element==inp){
            tail=tail.last;
            tail.next=null;
            size--;
            return;
        }
        for(LinkedNode<T> n=head;n!=null;n=n.next){
            if(n.element==inp){
                n.next.last=n.last;
                n.last.next=n.next;
                size--;
                return;
            }
        }
    }
    public T remove(int index){
        if(index>=size||index<0) return null; //Node index out of bounds
        if(size==1){ //Selected only element of list
            T out = head.element;
            head=null;
            tail=null;
            size=0;
            return out;
        }
        if(index==size-1){ //Tail is chosen and size > 1
            T out = tail.element;
            tail=tail.last;//Re-assigns the tail
            tail.next=null; //removes the next of tail
            size--;
            return out;
        }
        if(index==0){ //Head is chosen and size > 1
            T out = head.element;
            head=head.next; //re-assigns the head;
            head.last=null; //removes the last of head.
            size--;
            return out;
        }
        //Now we know that it is neither the head or tail, so it is in the middle
        LinkedNode<T> node = head;
        for(int i=0;i<index;i++){
            node=node.next;
        }
        node.next.last=node.last;
        node.last.next=node.next;
        size--;
        return node.element;
    }
    public int size(){
        return size;
    }
    public void recursivePrint(){
        if(head==null) return;
        head.recursivePrint();
    }
    public String toString(){
        if(size==0) return "[]";
        LinkedNode<T> node = head;
        String out="["+node.element.toString();
        while(node.next!=null){
            node=node.next;
            out+=","+node.element.toString();
        }
        out+="]";
        return out;
    }
    public boolean contains(T check){
        for(LinkedNode<T> n=head;n!=null;n=n.next){
            if(n.element==check) return true;
        }
        return false;
    }
}
