public class Main {
   public static void main(String[] args) {
       System.out.println("Hello and welcome to the password manager.\n" +
               "This program is a password manager, which means that it stores passwords inside a single package.\n" +
               "Passwords are entered into the manager by creating entries, including other pieces of data: title, username, and website URL.\n" +
               "Each entry is stored in an ArrayList."); //welcome message
       PwConsole console = new PwConsole();
       console.run(); //runs the contents of PwConsole
   }
}

