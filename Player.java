/*
 * File: Player.java
 * Author: David Neufeld
 * Created Date: Tue Nov 30 2021 at 3:09:45 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * One player of the game including screen info and conrols.
 * Collaboration: 
 * 
 */
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class Player extends JPanel{
    String name;
    int playerNum;
    int score=0;
    Game game;
    Boolean isTurn=false;
    LinkedList<Card> hand = new LinkedList<Card>();
    LinkedList<Card> selected = new LinkedList<Card>();
    boolean tookCard;//Player must start by taking card from either place
    public Player(String name){
        this.name=name;
        tookCard=false;
        addMouseListener(listener);
    }
    public File saveFile(String fileName) throws IOException{
        File out = new File(fileName);
        FileWriter w = new FileWriter(out);
        w.write(name+" - name\n");
        w.write(playerNum+" - playerNum\n");
        w.write(score+" - score\n");
        w.write(isTurn+" - isTurn\n");
        w.write(tookCard+" - tookCard\n");
        for(LinkedNode<Card> n=hand.head;n!=null;n=n.next){
            w.write(n.element.toString()+"\n");
        }
        w.close();
        return out;
    }
    //Initialize buttons
    Button drawButton = new Button(15, 20, 60, 84,"Draw Card"){
        @Override
        public void click(){
            if(!tookCard){
                Card c = game.drawCard();
                c.owner=playerNum;
                giveCard(c);
                tookCard=true;
            }
        }
    };
    Button discardButton = new Button(15,450,60,40,"Discard") {
        @Override
        public void click(){
            if(tookCard)
            discardSelection();
        }
    };
    Button playButton = new Button(15,500,60,40,"Play Cards") {//card to place melds
        @Override
        public void click(){
            //selected.add(game.drawCard()); //To test the meld arrangement
            if(tookCard&&selected.size!=hand.size&&selected.size>0){
                Meld m = new Meld(100, 300);
                if(m.addCards(selected)){//add selection to new meld
                    //if selected cards are sucsessfully added
                    game.melds.add(m);//add the meld to the play area
                    for(LinkedNode<Card> n=selected.head;n!=null;n=n.next) {
                        score+=n.element.value;
                        hand.removeElement(n.element);
                    }
                    game.arrangeMelds();
                    placeHand();
                    selected = new LinkedList<Card>();//remove selected cards from hand\
                }
            }
        }
    };
    public void discardSelection(){
        //TODO add special case so that you can not discard card you just got from discard
        if(selected.size==1){
            hand.removeElement(selected.get(0));//Remove selected card from hand
            game.addToDiscard(selected.remove(0));//Put selected card in discard pile
            placeHand();
            endTurn();
        }
    }
    public void endTurn(){
        if(isTurn){//Redundant checks
            //Check if game is over
            if(hand.size==0){
                placeHand();
                System.out.println(name+" went out!");
                game.endGame();
                return;
            }
            //reset vars for next turn
            tookCard=false;
            game.nextTurn();
        }
    }
    public void endGame(){
        int deduction = 0;
        for(LinkedNode<Card> n=selected.head;n!=null;n=n.next) deduction+=n.element.value;
        score-=deduction;
    }
    public void nextGame(){
        isTurn=false;
        tookCard=false;
        hand = new LinkedList<Card>();
        selected = new LinkedList<Card>();
    }
    public void click(int x, int y){
        if(!isTurn) return;//player can not click anything below if it is not players turn
        //Test for button clicks
        if (drawButton.testForClick(x, y)) return;
        if (discardButton.testForClick(x, y)) return;
        if (playButton.testForClick(x, y)) return;
        //test for meld click
        if(tookCard&&selected.size>0&&selected.size!=hand.size)
        for(LinkedNode<Meld> n=game.melds.tail;n!=null;n=n.last){
            if(n.element.testForClick(x, y)){
                if(n.element.addCards(selected)){//attempts to add card and if succsessful...
                    //remove selected cards from hand
                    for(LinkedNode<Card> m=selected.head;m!=null;m=m.next) hand.removeElement(m.element);
                    game.arrangeMelds();
                }
            }
        }
        //test for card in hand click
        if(tookCard)
        for(LinkedNode<Card> n=hand.tail;n!=null;n=n.last){
            if(n.element.testForClick(x, y)){
                if(selected.contains(n.element)){
                    selected.removeElement(n.element);
                    n.element.drawY+=12;
                }
                else {
                    selected.add(n.element);
                    n.element.drawY-=12;
                }
                return;
            }
        }
        //If this is reached, player clicked elsewhere (so unselect cards)
        if(selected.size>0){
            placeHand();
            selected = new LinkedList<Card>();
        }
        //Test for discarded card click
        if(!tookCard)
        for(LinkedNode<Card> n=game.discard.tail;n!=null;n=n.last){
            if(n.element.testForClick(x, y)){
                tookCard=true;
                //add selected card and all following cards to hand
                for(LinkedNode<Card>m=n;m!=null;m=m.next){
                    hand.add(m.element);//add card to hand
                    game.discard.removeNode(m);
                }
                //Refresh visuals
                placeHand();

                return;
            }
        }
    }
    public void giveCard(Card inp){
        hand.add(inp);
        placeHand();
    }
    public void placeHand(){//arranges cards in hand where they should be on screen
        int maxHandWidth=400;
        int cardWidth=60;
        int increment=cardWidth+5;
        if(hand.size>maxHandWidth/(cardWidth+5)) increment=maxHandWidth/hand.size;
        int placeX=300-(increment*hand.size)/2;
        int placeY=460;
        for(LinkedNode<Card> n=hand.head;n!=null;n=n.next){
            n.element.place(placeX,placeY);
            placeX+=increment;
        }
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //Draw play area
        game.drawPlayArea(g);
        //draw cards in hand
        for(LinkedNode<Card> n=hand.head;n!=null;n=n.next){
            n.element.draw(g);
        }
        //draw buttons
        drawButton.draw(g);
        discardButton.draw(g);
        playButton.draw(g);
        //Display Name
        g.setColor(Color.black);
        g.drawString("Player "+(playerNum+1)+": "+name, 15, 15);
        //Display turn indicator
        if(isTurn){
            g.setColor(Color.green);
            g.fillOval(550, 10, 40, 40);
        }
        repaint();
    }
    MouseAdapter listener = new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e){

        }
        @Override
        public void mouseReleased(MouseEvent e){

        }
        @Override
        public void mouseClicked(MouseEvent e){
            //System.out.println(name+": "+e.getX()+" "+e.getY());
            //System.out.println(e.getButton());
            if(e.getButton()==1)
            click(e.getX(),e.getY());
        }
        @Override
        public void mousePressed(MouseEvent e){

        }
        @Override
        public void mouseEntered(MouseEvent e){

        }
    };
}
