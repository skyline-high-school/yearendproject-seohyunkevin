import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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

    //generation of secret key for encryption/decryption
    public static SecretKey getKeyFromPassword(String masterPw) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPw.toCharArray(), (masterPw + "salt").getBytes(), 256, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }
    //encrypts the data of the given object
    private static Object encryptObject(Serializable object, String masterPw) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(masterPw));
        SealedObject sealedObject = new SealedObject(object, cipher);
        return sealedObject;
    }
    //decrypts the data of the given object
    private static Object decryptObject(SealedObject sealedObject, String masterPw) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException, InvalidKeySpecException, InvalidAlgorithmParameterException {


        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(masterPw));
        Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
        return unsealObject;
    }



    public boolean encryptAndSave() {
        try {
            FileOutputStream file = new FileOutputStream(dataFileName);
            file.getChannel().truncate(0);
            ObjectOutputStream out = new ObjectOutputStream(file);


            out.writeObject(encryptObject(masterPw, masterPw));
            out.writeObject(encryptObject((ArrayList<PwInfo>) pwInfoList, masterPw));


            out.close();
            file.close();


            return true;
        } catch (IOException ex) {
            return false;
        } catch (InvalidAlgorithmParameterException e) {
            return false;
        } catch (NoSuchPaddingException e) {
            return false;
        } catch (IllegalBlockSizeException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (InvalidKeySpecException e) {
            return false;
        } catch (InvalidKeyException e) {
            return false;
        }
   }



    //load saved data
    public boolean loadAndDecrypt(String masterPw) {
        try {
            if (!new File(dataFileName).exists()) {
                return true;
            }


            FileInputStream file = new FileInputStream(dataFileName);
            ObjectInputStream in = new ObjectInputStream(file);


            this.masterPw = (String) decryptObject((SealedObject) in.readObject(), masterPw);
            this.pwInfoList = (List<PwInfo>) decryptObject((SealedObject) in.readObject(), masterPw);


            in.close();
            file.close();


            return true;
        } catch(IOException ex) {
            return false;
        } catch(ClassNotFoundException ex) {
            return false;
        } catch (NoSuchPaddingException e) {
            return false;
        } catch (IllegalBlockSizeException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (BadPaddingException e) {
            return false;
        } catch (InvalidKeySpecException e) {
            return false;
        } catch (InvalidKeyException e) {
            return false;
        } catch (InvalidAlgorithmParameterException e) {
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
