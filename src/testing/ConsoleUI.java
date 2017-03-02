package testing;

import java.util.Scanner;

import createdGameClasses.*;
import sharedClasses.*;

public class ConsoleUI {
    
    public static void main(String[] args) {
        GameController gc = SetupGameController.createGameController();
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Starting the game...");
        displayFrame(gc);
        
        while(scanner.hasNext()) {
            String nextInput = scanner.nextLine();
            
            if (nextInput.equals("f")) {
                gc.nextFrame();
                displayFrame(gc);
            }
        }        
    }
    
    public static void displayFrame(GameController gc) {
        Frame f = gc.getCurFrame();
        
        System.out.println("Frame:");
        System.out.println("Dialog: " + f.getDialog());
        System.out.println("Background: " + gc.getBG(f.getBG()));
        System.out.print("Chars: ");
        if (f.getChars().size() == 0) 
            System.out.println("None");
        else 
            for (DisplayChar dc : f.getChars())
                System.out.print(dc.getCharName() + ", ");
    }

}
