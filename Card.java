/*
 * File: Card.java
 * Author: David Neufeld
 * Created Date: Tue Nov 30 2021 at 4:26:31 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Card {
    int number;//1=ace 11=jack 12=queen 13=king
    String suit;//Hearts, Diamonds, Clubs and Spades
    int value;//numbers are worth what they are, ace=15, faces=10
    int owner = -1;//player number who last drew the card (-1 if no owner)
    int width=60;
    int height=84;
    int drawX=100;
    int drawY=100;
    BufferedImage cardImage;
    int sortValue;
    public Card(int number, String suit){
        this.number=number;
        this.suit=suit;
        if(suit.equals("Clubs")) sortValue = 0+number;
        if(suit.equals("Spades")) sortValue = 13+number;
        if(suit.equals("Hearts")) sortValue = 26+number;
        if(suit.equals("Diamonds")) sortValue = 39+number;
        if(number==1) value = 15; //Ace
        else if(number>=10) value = 10; //Face Cards
        else value = 5; //Num card
        try {
            cardImage = ImageIO.read(new File("./Card Images/"+suit.toLowerCase()+"_"+number+".png"));
        } catch (IOException e) {
            System.out.print("Card Image Not Found - FilePath: ");
            System.out.println("./Card Images/"+suit.toLowerCase()+"_"+number+".png");
            cardImage=null;
        }
    }
    public boolean testForClick(int mouseX, int mouseY){
        //Test if mouse is in bounds
        if(mouseX>drawX&&mouseX<drawX+width && mouseY>drawY&&mouseY<drawY+height){
            click();
            return true;
        }
        return false;
    }
    public void click(){

    }
    public String toString(){
        return sortValue+", "+number+" of "+suit;
    }
    public void place(int x, int y){//Places card on screen/pane
        drawX=x;
        drawY=y;
    }
    public void drawOld(Graphics g){
        Color oldColor=g.getColor();
        g.setColor(Color.white);
        g.fillRect(drawX, drawY, width, height);
        g.setColor(Color.black);
        g.drawRect(drawX, drawY, width, height);
        g.drawString(Integer.toString(number), drawX+width/2-2, drawY+height/2-15);
        g.drawString("of", drawX+width/2-5, drawY+height/2);
        g.drawString(suit, drawX+width/2-16, drawY+height/2+15);
        //Corner Text
        g.drawString(Integer.toString(number), drawX+5, drawY+15);
        g.drawString(suit.substring(0,1), drawX+5, drawY+30);
        g.setColor(oldColor);
    }
    public void draw(Graphics g){
        if(cardImage==null) {
            drawOld(g);
            return;
        }
        Color oldColor=g.getColor();
        g.drawImage(cardImage, drawX, drawY,width,height, null);
        g.setColor(Color.black);
        g.drawRect(drawX, drawY, width, height);
        g.setColor(oldColor);
    }
}
