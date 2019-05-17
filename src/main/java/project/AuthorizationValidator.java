package project;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class AuthorizationValidator {
    public static String cryptPassword(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    public static String checkCreds(String login, String password) throws FileNotFoundException {

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/AutorizationCredentials")))) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.contains(login) && line.contains(cryptPassword(password))) {
                    return UUID.randomUUID().toString();
                }
            }
        } catch (IOException ioex) {
            System.out.println(ioex.toString());
        }
        return null;
    }
}
