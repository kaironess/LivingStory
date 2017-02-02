package testing;

import java.util.Scanner;

import createdGameClasses.*;
import sharedClasses.*;

public class ConsoleUI {
    
    public static void main(String[] args) {
        GameController gc = SetupGameController.createGameController();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Starting the game...");
        displayFrame(gc.getCurFrame());
        
        while(scanner.hasNext()) {
            String nextInput = scanner.nextLine();
            
            if (nextInput.equals("f")) {
                gc.nextFrame();
                displayFrame(gc.getCurFrame());
            }
        }        
    }
    
    public static void displayFrame(Frame f) {
        System.out.println("Frame:");
        System.out.println("Dialog: " + f.getDialog());
    }

}
