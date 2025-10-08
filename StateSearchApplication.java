import java.util.Scanner;

/**
 * State Search Application using Boyer-Moore Algorithm (Bad Character Rule)
 * 
 * This application allows users to search for patterns in a collection of US state names
 * using the bad character rule from the Boyer-Moore string searching algorithm.
 * 
 * @author Greg
 * @version 1.0
 */
public class StateSearchApplication {
    
    // Array of all US state names as the text data
    private static final String[] STATES = {
        "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"        
    };
    
    // Combined text string for searching
    private static String text;
    
    static {
        // Initialize the text by combining all state names with spaces
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STATES.length; i++) {
            sb.append(STATES[i]);
            if (i < STATES.length - 1) {
                sb.append(" ");
            }
        }
        text = sb.toString();
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("=== State Search Application ===");
        System.out.println("Using Boyer-Moore Algorithm (Bad Character Rule)");
        System.out.println();
        
        while (running) {
            displayMenu();
            int choice = getChoice(scanner);
            
            switch (choice) {
                case 1:
                    displayText();
                    break;
                case 2:
                    searchPattern(scanner);
                    break;
                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
            System.out.println();
        }
        
        scanner.close();
    }
    
    /**
     * Displays the main menu options
     */
    private static void displayMenu() {
        System.out.println("Please select an option:");
        System.out.println("1) Display the text");
        System.out.println("2) Search");
        System.out.println("3) Exit program");
        System.out.print("Enter your choice (1-3): ");
    }
    
    /**
     * Gets user's menu choice with input validation
     */
    private static int getChoice(Scanner scanner) {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1; // Invalid choice
        }
    }
    
    /**
     * Displays the text content (all state names)
     */
    private static void displayText() {
        System.out.println("\n=== TEXT CONTENT ===");
        System.out.println("States: " + text);
        System.out.println("Total characters: " + text.length());
    }
    
    /**
     * Handles pattern search functionality
     */
    private static void searchPattern(Scanner scanner) {
        System.out.print("Enter a pattern to search for: ");
        String pattern = scanner.nextLine();
        
        if (pattern.isEmpty()) {
            System.out.println("Pattern cannot be empty.");
            return;
        }
        
        System.out.println("\nSearching for pattern: \"" + pattern + "\"");
        System.out.println("In text: \"" + text + "\"");
        
        // Search using Boyer-Moore bad character rule
        int[] matches = boyerMooreSearch(text, pattern);
        
        if (matches.length == 0) {
            System.out.println("No matches found.");
        } else {
            System.out.println("Pattern found at indices: ");
            for (int i = 0; i < matches.length; i++) {
                System.out.print(matches[i]);
                if (i < matches.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
            System.out.println("Total matches: " + matches.length);
        }
    }
    
    /**
     * Implements Boyer-Moore algorithm using the bad character rule
     * 
     * @param text The text to search in
     * @param pattern The pattern to search for
     * @return Array of indices where pattern matches are found
     */
    private static int[] boyerMooreSearch(String text, String pattern) {
        if (text == null || pattern == null || pattern.length() == 0) {
            return new int[0];
        }
        
        int textLength = text.length();
        int patternLength = pattern.length();
        
        if (patternLength > textLength) {
            return new int[0];
        }
        
        // Build bad character table
        int[] badChar = buildBadCharacterTable(pattern);
        
        // List to store match indices
        java.util.List<Integer> matches = new java.util.ArrayList<>();
        
        int shift = 0; // Position in text
        
        while (shift <= textLength - patternLength) {
            int j = patternLength - 1; // Start from rightmost character of pattern
            
            // Keep reducing j while characters match
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }
            
            // If pattern is found (j becomes -1)
            if (j < 0) {
                matches.add(shift);
                
                // Move to next possible position
                // Use bad character rule for the character after the pattern
                shift += (shift + patternLength < textLength) ? 
                         patternLength - badChar[text.charAt(shift + patternLength)] : 1;
            } else {
                // Mismatch occurred, use bad character rule
                int badCharShift = Math.max(1, j - badChar[text.charAt(shift + j)]);
                shift += badCharShift;
            }
        }
        
        // Convert list to array
        return matches.stream().mapToInt(i -> i).toArray();
    }
    
    /**
     * Builds the bad character table for Boyer-Moore algorithm
     * 
     * @param pattern The pattern to build table for
     * @return Bad character table array
     */
    private static int[] buildBadCharacterTable(String pattern) {
        final int ALPHABET_SIZE = 256; // ASCII characters
        int[] badChar = new int[ALPHABET_SIZE];
        
        // Initialize all characters with -1
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            badChar[i] = -1;
        }
        
        // Fill the actual positions of characters in pattern
        for (int i = 0; i < pattern.length(); i++) {
            badChar[(int) pattern.charAt(i)] = i;
        }
        
        return badChar;
    }
}