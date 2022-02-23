/*
 * File: Game.java
 * Author: David Neufeld
 * Created Date: Tue Nov 30 2021 at 3:14:43 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
public class Game {
    Player[] players;
    int totalPlayers;
    int turn;
    LinkedList<Card> stock = new LinkedList<Card>();
    LinkedList<Card> discard = new LinkedList<Card>();
    LinkedList<Meld> melds = new LinkedList<Meld>();
    //Card upCard;
    public Game(Player[] players){
        this.players=players;
        totalPlayers=players.length;
        //Initialize game object in each player.
        for(int i=0;i<players.length;i++){
            players[i].game=this;
            players[i].playerNum=i;
        }
        //startGame();
    }
    public void startGame(){
        //Generate Deck
        for(int i=1;i<=13;i++) stock.addWithValue(new Card(i,"Clubs"),0+i);
        for(int i=1;i<=13;i++) stock.addWithValue(new Card(i,"Spades"),13+i);
        for(int i=1;i<=13;i++) stock.addWithValue(new Card(i,"Hearts"),26+i);
        for(int i=1;i<=13;i++) stock.addWithValue(new Card(i,"Diamonds"),39+i);
        //Shuffle Deck
        shuffle(stock);
        //Deal cards
        int handSize;
        //hand size depends on player number
        if(totalPlayers<=2) handSize=10;
        else if (totalPlayers<=4) handSize = 7;
        else if (totalPlayers<=6) handSize = 6;
        else handSize=5; //shouldn't really play with more than 6 
        for(int i=0;i<handSize;i++){
            for(int j=0;j<totalPlayers;j++){
                players[j].giveCard(stock.remove(0));
            }
        }
        //Discard top of stock
        addToDiscard(stock.remove(0)); 
        //Set player turn
        turn=0;
        players[0].isTurn=true;
    }
    public void endGame(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Scores:");
        for(int i=0;i<totalPlayers;i++){
            players[i].endGame();
            System.out.println(players[i].name+": "+players[i].score);
        }
        System.out.println("Play again?");
        String response = reader.nextLine();
        if(response.equalsIgnoreCase("yes")){
            for(int i=0;i<totalPlayers;i++){
                players[i].nextGame();
            }
            stock = new LinkedList<Card>();
            discard = new LinkedList<Card>();
            melds = new LinkedList<Meld>();
            startGame();
            reader.close();
            return;
        } else {
            System.out.println("Save Game?");
            response = reader.next();
            if(response.equalsIgnoreCase("yes")){
                System.out.println("Input save name");
                response = reader.next();
                try {
                    saveGame(response);  
                    System.out.println("Saved as "+response);
                } catch (IOException e) {
                    System.out.println("Error Saving.");
                }
            }
            System.exit(1);
        }
        
        reader.close();
    }
    public void saveGame(String fileName) throws IOException{
        File out = new File(fileName);
        FileWriter w = new FileWriter(out);
        w.write(totalPlayers+" - total players \n");
        for(int i=0;i<totalPlayers;i++){
            w.write("player "+players[i].playerNum+"\n");
            w.write(players[i].name+"\n");
            w.write(players[i].score+"\n");
        }
        w.close();
    }
    public void setTurn(int inp){
        players[turn].isTurn=false;
        this.turn=inp;
        players[turn].isTurn=true;
    }
    public void nextTurn(){
        players[turn].isTurn=false;
        turn+=1;
        if(turn==totalPlayers) turn=0;
        players[turn].isTurn=true;
    }
    public Card drawCard(){
        return stock.remove(0);
    }
    public void addToDiscard(Card inp){
        discard.add(inp);
        //Position in correct location
        int increment=15;
        inp.place(100+discard.size*(increment), 20);
    }
    public void shuffle(LinkedList<Card> inp){
        LinkedList<Card> shuffled = new LinkedList<Card>();
        Random r = new Random();
        while(inp.size>0){
            int index = r.nextInt(inp.size);
            shuffled.add(inp.remove(index));
        }
        while(shuffled.size>0){
            inp.add(shuffled.remove(0));
        }
    }
    public void arrangeMelds(){
        int totalWidth=-10;
        LinkedNode<Meld> node=melds.head;
        int count=0;
        while(totalWidth<500){
            totalWidth+=node.element.width+10;
            count++;
            node=node.next;
            if(node==null) break;
        }
        int placeX=300-totalWidth/2;
        int placeY=150;
        int spacing=10;
        node=melds.head;
        int i;
        for(i=0;i<count;i++){
            node.element.place(placeX,placeY);
            if(node.next!=null)
            placeX+=node.element.width+spacing;
            node=node.next;
        }
        if(node==null) return; //stop here if all melds are placed
        //Row 2
        totalWidth=-10;
        while(totalWidth<500){
            totalWidth+=node.element.width+10;
            count++;
            node=node.next;
            if(node==null) break;
        }
        placeX=300-totalWidth/2;
        placeY+=90;
        for(i=i+0;i<count;i++){
            melds.get(i).place(placeX,placeY);
            placeX+=melds.get(i).width+spacing;
        }
        if(node==null) return; //stop here if all melds are placed
        totalWidth=-10;
        while(node!=null){
            totalWidth+=node.element.width+spacing;
            count++;
            node=node.next;
        }
        placeX=300-totalWidth/2;
        placeY+=90;
        for(i=i+0;i<count;i++){
            melds.get(i).place(placeX,placeY);
            placeX+=melds.get(i).width+spacing;
        }
    }
    public void drawPlayArea(Graphics g){
        g.setColor(Color.black);
        //draw melds
        for(LinkedNode<Meld> n=melds.head;n!=null;n=n.next){
            n.element.draw(g);
        }
        //draw stock (Will be done by players)
        g.drawRect(15, 20, 60, 80);
        g.drawString("Stock", 30, 115);
        //Draw the discard pile
        for(LinkedNode<Card> n=discard.head;n!=null;n=n.next){
            n.element.draw(g);;
        }
        g.drawString("Discard", 125, 1);
    }
}
