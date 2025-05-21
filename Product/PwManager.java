import java.util.*;
import java.io.*;

public class PwManager {
   private String masterPw = ""; //the password required to access the password manager
   private List<PwInfo> pwInfoList = new ArrayList<PwInfo>();
   private static String dataFileName = "pwManager.data";

   //master password check against password attempt
   public Boolean checkMasterPw(String pwInput) {
       return pwInput.equals(masterPw);
   }



   //change master password
   public void changeMasterPw(String masterPw) {
       this.masterPw = masterPw;
   }
   //get password info list
   public List<PwInfo> getPwInfoList() {
       return pwInfoList;
   }
   //save data
   public boolean save() {
       try {
           FileOutputStream file = new FileOutputStream(dataFileName);
           file.getChannel().truncate(0); //deletes all contents of pwManager.data
           ObjectOutputStream out = new ObjectOutputStream(file);

           out.writeObject(masterPw);
           out.writeObject((ArrayList<PwInfo>) pwInfoList);

           out.close();
           file.close();

           return true;
       } catch(IOException ex) {
           return false;
       }
   }
   //load saved data
   public boolean load(String masterPw) {
       try {
           if (!new File(dataFileName).exists()) {
               return true;
           }

           FileInputStream file = new FileInputStream(dataFileName);
           ObjectInputStream in = new ObjectInputStream(file);

           this.masterPw = (String) in.readObject();
           this.pwInfoList = (List<PwInfo>) in.readObject();

           in.close();
           file.close();

           return true;
       } catch(IOException | ClassNotFoundException ex) {
           return false;
       }
   }
   //look-up a requested title
   public PwInfo lookUp(String title) {
       for (PwInfo info: pwInfoList) { //traverse through the ArrayList
           if (info.getTitle().equals(title)) { //check whether requested title matches the index
               return info;
           }
       }
       return null;
   }
   //add an entry
   public boolean addPwInfo(String title, String username, String password, String siteURL) {
       if (lookUp(title) == null) {
           pwInfoList.add(new PwInfo(title, username, password, siteURL));
           return true;
       }
       return false;
   }
   //delete an entry
   public boolean deletePwInfo(String title) {
       PwInfo info = lookUp(title);
       if (info != null) {
           pwInfoList.remove(info);
           return true;
       }
       return false;
   }
}
