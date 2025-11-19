package com.nts.users.Utils;


import org.springframework.security.crypto.bcrypt.BCrypt;

public final class BcryptUtils {

    private BcryptUtils(){}

    public static String hash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(14));
    }
    public static boolean checkHash(String unhashedPassword, String hashedPassword){
        return BCrypt.checkpw(unhashedPassword, hashedPassword);
    }

}