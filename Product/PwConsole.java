    import java.util.*;

    public class PwConsole {
    private static final int MAX_PW_ATTEMPTS = 5; //the user cannot exceed this amount of attempts at inputting the correct master password.
    private PwManager pwManager = new PwManager();
    private Scanner scanner = new Scanner(System.in);
    private int newUser = 0;

    //executes all contents of PwConsole
    public void run() {
        System.out.println("Please enter your master password to access the manager.\n" +
                "If it is your first time opening the password manager, just press ENTER to proceed."); //asks for user's password
        if (!checkPW()) { //checks whether the user's master password attempt is incorrect
            return;
        }
        newUser = 0;
        executeUserCommands(); //calls major functions of the password manager
    }

    //checks whether the user's master password attempt is correct
    private boolean checkPW() {
        for (int i = 0; i < MAX_PW_ATTEMPTS; i++) {
            String pwInput = scanner.nextLine();
            if (pwManager.loadAndDecrypt(pwInput) && pwManager.checkMasterPw(pwInput)) {
                newUser = 1;
                return true;
            }
            System.out.println("Wrong master password! Try again.");
            scanner.nextLine();
        }
        System.out.println("Too many failed attempts!\nPassword manager closing...");
        return false;
    }

    //performs all the major functions of the password manager
    private void executeUserCommands() {
        while (true) {
            if (newUser == 0){
                System.out.println("These are the commands that you can use to operate this program:");
                scanner.nextLine();
                System.out.println("Type \"L\" if you want to have all the current entries listed into the UI.");
                scanner.nextLine();
                System.out.println("Type \"A\" if you want to add a new entry to the manager.");
                scanner.nextLine();
                System.out.println("Type \"D\" if you want to delete an entry from the manager."); 
                scanner.nextLine();
                System.out.println("Type \"F\" if you want to look up an entry in the manager." );
                scanner.nextLine();
                System.out.println("Type \"E\" if you want to edit an entry in the manager."); 
                scanner.nextLine();
                System.out.println("Type \"M\" if you want to change the master password."); 
                scanner.nextLine();
                System.out.println("Type \"Q\" if you want to save and close this manager.");
                scanner.nextLine();
                System.out.println("Type \"help\" if you need to see these programs again!");
                scanner.nextLine();
                newUser = 1;
            } 

            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("L")) { //list
                listPwInfo();
            } else if (response.equalsIgnoreCase("help")) { //add
                listCommands();
            } else if (response.equalsIgnoreCase("A")) { //add
                addPwInfo();
            } else if (response.equalsIgnoreCase("D")) { //delete
                deletePwInfo();
            } else if (response.equalsIgnoreCase("F")) { //search/find
                lookUpPwInfo();
            } else if (response.equalsIgnoreCase("E")) { //edit
                editPwInfo();
            } else if (response.equalsIgnoreCase("M")) { //change master password
                changeMasterPw();
            } else if (response.equalsIgnoreCase("Q")) { //save and quit
                saveAllPwInfos();
                System.out.println("Password manager closing...");
                break;
            } else { //invalid input
                System.out.println("Wrong input!");
            }


            scanner.nextLine();

        }
    }
    //lists all entries
    private void listPwInfo() {
        for (PwInfo info: pwManager.getPwInfoList()) { //traverses the ArrayList
            System.out.println("\n\n" + info); //prints the index into the UI
        }
    }
    //searches the title that the user requested
    private void lookUpPwInfo() {
        System.out.println("What is the title of the entry you want to find?");
        String title = scanner.nextLine();

        //checks whether the user's input matches a title of one of the existing entries
        PwInfo info = pwManager.lookUp(title);
        if (info != null) {
            System.out.println("A matching entry was found!");
            System.out.println(info);
        } else {
            System.out.println("An entry with title \"" + title + "\" was NOT found!");
        }
    }
    //adds an entry
    private void addPwInfo() {
        String title; //initializes title
        while (true) {
            System.out.println("What do you want the title to be?");
            title = scanner.nextLine(); //writes title

            if (pwManager.lookUp(title) != null) { //checks whether the title input matches the title of an existing entry
                System.out.println("This title exists already!");
            } else {
                break;
            }
        }
    

        System.out.println("What is the username?");
        String username = scanner.nextLine();

        System.out.println("And the password?");
        String password = scanner.nextLine();

        System.out.println("What is the website's address?");
        String siteURL = scanner.nextLine();

        pwManager.addPwInfo(title, username, password, siteURL); //adding the info
        System.out.println("Entry successfully added!");
    }

    //this lists out all the commands if a user forgot one
    private void listCommands() {
            System.out.println("These are the commands that you can use to operate this program:\n"+
                            "Type \"L\" if you want to have all the current entries listed into the UI.\n"+
                            "Type \"A\" if you want to add a new entry to the manager.\n"+
                            "Type \"D\" if you want to delete an entry from the manager.\n"+ 
                            "Type \"F\" if you want to look up an entry in the manager.\n"+
                            "Type \"E\" if you want to edit an entry in the manager.\n" +
                            "Type \"M\" if you want to change the master password.\n"+ 
                            "Type \"Q\" if you want to save and close this manager.\n"+
                            "Type \"help\" if you need to see these programs again!");
                            return;
        
    }
    //deletes an entry
    private void deletePwInfo() {
        System.out.println("What is the title of the entry you want to delete?");
        String title = scanner.nextLine();

        if (pwManager.deletePwInfo(title)) { //checks whether deletion was successful
            System.out.println("Deletion successful!");
        } else {
            System.out.println("There is no entry with the given title. Deletion failed!");
        }
    }
    //edits an entry
    private void editPwInfo() {
        System.out.println("What is the title of the entry you want to edit?");
        String title = scanner.nextLine();
        PwInfo info = pwManager.lookUp(title);
        if (info == null) {
            System.out.println("An entry with the given title does not exist!");
            return;
        }

        System.out.println("Choose one of the following options:");
        System.out.println(
                "Type \"T\" if you want to edit the title.\n" +
                        "Type \"U\" if you want to edit the username.\n" +
                        "Type \"P\" if you want to edit the password.\n" +
                        "Type \"W\" if you want to edit the website URL."
        );
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("T")) { //edit title
            System.out.println("What do you want the new title to be?");
            String newTitle = scanner.nextLine();
            if (pwManager.lookUp(newTitle) != null) { //checks whether the new title matches the title of one of the existing entries
                System.out.println("The same title already exists!"); //if they do match, the new title is rejected
            } else {
                info.setTitle(newTitle);
                System.out.println("Title changed!");
            }
        } else if (response.equalsIgnoreCase("U")) { //edit username
            System.out.println("What do you want the new username to be?");
            String newUsername = scanner.nextLine();
            info.setUsername(newUsername);
            System.out.println("Username changed!");
        } else if (response.equalsIgnoreCase("P")) { //edit password
            System.out.println("What do you want the new password to be?");
            String newPassword = scanner.nextLine();
            info.setPassword(newPassword);
            System.out.println("Password changed!");
        } else if (response.equalsIgnoreCase("W")) { //edit website URL
            System.out.println("What do you want the new website URL to be?");
            String newSiteURL = scanner.nextLine();
            info.setSiteURL(newSiteURL);
            System.out.println("Website URL changed!");
        } else { //invalid input
            System.out.println("Wrong input!");
        }
    }
    //saves all entries to pwManager.data
    private void saveAllPwInfos() {
        if (pwManager.encryptAndSave()) {
            System.out.println("Saved successfully.");
            return;
        }
        System.out.println("Saving failed. Try again.");
    }
    //changes the master password
    private void changeMasterPw() {
        System.out.println("What would you like the new master password to be?");
        pwManager.changeMasterPw(scanner.nextLine());
        System.out.println("Master password changed!");
    }
    }
