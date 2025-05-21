public class PwInfo {
   private String title;
   private String username;
   private String password;
   private String siteURL;

   //constructor
   public PwInfo(String title, String username, String password, String siteURL) {
       this.title = title;
       this.username = username;
       this.password = password;
       this.siteURL = siteURL;
   }

   //getter methods
   public String getTitle() {
       return title;
   }
   public String getUsername() {
       return username;
   }
   public String getPassword() {
       return password;
   }
   public String getSiteURL() {
       return siteURL;
   }

   //setter methods
   public void setTitle(String title) {
       this.title = title;
   }
   public void setUsername(String username) {
       this.username = username;
   }
   public void setPassword(String password) {
       this.password = password;
   }
   public void setSiteURL(String siteURL) {
       this.siteURL = siteURL;
   }

   //toString
   public String toString() {
       return "Title: " + this.getTitle() +
               "\nUsername: " + this.getUsername() +
               "\nPassword: " + this.getPassword() +
               "\nWebsite URL: " + this.getSiteURL();
   }
}
