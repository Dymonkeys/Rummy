/*
 * File: Main.java
 * Author: David Neufeld
 * Created Date: Tue Nov 30 2021 at 3:01:52 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * Set up windows for use in a card game
 * Collaboration: 
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
public class Main {
    public static void main(String [] args) throws IOException{
        boolean debug = false;//just to get into a game faster for testing reasons, also puts 2 players on one screen.
        Player[] players=new Player[0];
        Scanner r = new Scanner(System.in);
        if(!debug){
            System.out.println("Enter 1 to create new game");
            System.out.println("Enter 2 to load game from a file");
            String choice=r.nextLine();
            //set up players
            if(choice.equals("1")){
                players = setUpPlayersFromTerminal(r);
            } else if(choice.equals("2")){
                System.out.println("Enter file name");
                String fileName=r.nextLine();
                try {
                    File file = new File(fileName);
                    players = setUpPlayersFromFile(file);
                } catch (Exception e) {
                    System.out.println("File not found");
                    System.exit(0);
                }
            } else {
                System.out.println("Invalid choice, terminating program.");
                System.exit(0);
            }
        } else{
            players=new Player[2];
            players[0] = new Player("David");
            players[1] = new Player("Jacob");
        }
        Game rummy = new Game(players);
        rummy.startGame();
        
        if(debug) dualWindow(players[0], players[1]);
        else setUpPlayerWindows(rummy.players);
    }
    public static JFrame dualWindow(Player player1, Player player2){
        //Set up window
        JFrame theGUI = new JFrame();
        theGUI.setBackground(Color.green);
        theGUI.setTitle("Rummy");
        theGUI.setSize(1250,600);
        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set gridlayout
        GridLayout grid = new GridLayout(1,0);
        theGUI.setLayout(grid);
        //Add players to pane
        Container pane = theGUI.getContentPane();
        pane.add(player1);
        pane.add(player2);
        
        theGUI.setVisible(true);
        return theGUI;
    }
    public static JFrame[] setUpPlayerWindows(Player[] players){
        JFrame[] frames = new JFrame[players.length];
        for(int i=0;i<players.length;i++){
            frames[i] = new JFrame();
            frames[i].setTitle("Rummy - "+players[i].name);
            frames[i].setSize(600,600);
            frames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frames[i].getContentPane().add(players[i]);
            frames[i].setBackground(Color.green.darker());
            frames[i].setVisible(true);
        }
        return frames;
    }
    public static Player[] setUpPlayersFromTerminal(Scanner r){
        System.out.println("How many players?");
        int totalPlayers=r.nextInt();
        r.nextLine();
        Player[] players = new Player[totalPlayers];
        for(int i=0;i<totalPlayers;i++){
            System.out.println("Enter name of player "+(i+1));
            players[i]=new Player(r.nextLine());
        }
        System.out.println();
        return players;
    }
    public static Player[] setUpPlayersFromFile(File file) throws IOException{
        Scanner r = new Scanner(file);
        int totalPlayers=r.nextInt();
        r.nextLine();
        Player[] players = new Player[totalPlayers];
        for(int i=0;i<totalPlayers;i++){
            r.nextLine(); //states player num
            players[i]=new Player(r.nextLine());
            players[i].score=r.nextInt();
            r.nextLine();
        }
        r.close();
        return players;
    }
}
