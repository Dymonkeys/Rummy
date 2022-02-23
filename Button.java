/*
 * File: Button.java
 * Author: David Neufeld
 * Created Date: Tue Nov 30 2021 at 6:01:21 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * To be modified for use in player.
 * Collaboration: 
 * 
 */
import java.awt.*;
public abstract class Button {
    int x;
    int y;
    int width;
    int height;
    String text="";
    public Button(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
    public Button(int x, int y, int width, int height,String text){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.text=text;
    }
    public void setDimentions(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
    public boolean testForClick(int mouseX, int mouseY){
        //Test if mouse is in bounds
        if(mouseX>x&&mouseX<x+width && mouseY>y&&mouseY<y+height){
            click();
            return true;
        }
        return false;
    }
    public void click(){
        //To be overriden with button function
        System.out.println("Button not bound!");
    }
    public void draw(Graphics g){
        Color oldColor = g.getColor();
        g.setColor(Color.white);
        g.fillRect(x, y, width, height);
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        g.drawString(text, x+2, y+height/2);
        g.setColor(oldColor);
    }
}