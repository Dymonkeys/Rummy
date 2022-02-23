/*
 * File: Meld.java
 * Author: David Neufeld
 * Created Date: Tue Nov 30 2021 at 8:57:19 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * Contains a small group of cards
 * Collaboration: 
 * 
 */

import java.awt.*;
public class Meld {
    LinkedList<Card> cards = new LinkedList<Card>();
    int x=600;
    int y=600;
    int width;
    int height;
    public Meld(LinkedList<Card> cards){
        this.cards=cards;
        place(600,600);
    }
    public Meld(int x, int y){
        this.x=x;
        this.y=y;
        place(x, y);
    }
    //hitbox for meld
    Button button = new Button(0,0,0,0) {
        @Override
        public void click(){
            
        }
    };
    public boolean testForClick(int mouseX, int mouseY){
        return button.testForClick(mouseX, mouseY);
    }
    public boolean addCards(LinkedList<Card> inp){//return if it fits
        //Create hypothetical
        LinkedList<Card> hypothetical = new LinkedList<Card>();
        for(LinkedNode<Card> n=cards.head;n!=null;n=n.next){
            hypothetical.addWithValue(n.element,n.sortValue);
        }
        for(LinkedNode<Card> n=inp.head;n!=null;n=n.next){
            hypothetical.addWithValue(n.element,n.sortValue);
        }
        hypothetical.mergeSort();
        //minimum size
        if(hypothetical.size<3) return false;
        //System.out.println(hypothetical.toString());
        //check for pair
        boolean fits = true;
        int pairNumber = hypothetical.head.element.number;
        for(LinkedNode<Card> n=hypothetical.head;n!=null;n=n.next){
            if(n.element.number!=pairNumber){
                fits=false;
                break;
            }
        }
        //Check for run
        if(fits==false){
            fits=true;
            String runSuit=hypothetical.head.element.suit;
            int runNumber=hypothetical.head.element.number;
            for(LinkedNode<Card> n=hypothetical.head.next;n!=null;n=n.next){
                runNumber+=1;
                if(n.element.number!=runNumber){
                    fits=false;
                    break;
                }
                if(!n.element.suit.equals(runSuit)){
                    fits=false;
                    break;
                }
            }
        }
        //fits=true;//TODO Remove
        if(!fits) return false;
        for(LinkedNode<Card> n=inp.head;n!=null;n=n.next){
            cards.addWithValue(n.element, n.sortValue);
        }
        cards.mergeSort();
        place(x, y);//rearange cards
        return true;
    }
    public void place(int x,int y){
        this.x=x;
        this.y=y;
        int cardWidth=60;
        int cardHeight=84;
        int increment=15;
        this.width=(cardWidth+increment*(cards.size()-1));
        this.height=cardHeight;
        int placeX=x;
        int placeY=y;
        //Move button
        button.setDimentions(placeX, placeY, width, height);
        //place cards
        for(LinkedNode<Card> n=cards.head;n!=null;n=n.next){
            n.element.place(placeX,placeY);
            placeX+=increment;
        }
    }
    public void draw(Graphics g){
        for(LinkedNode<Card> n=cards.head;n!=null;n=n.next){
            n.element.draw(g);
        }
    }
}
