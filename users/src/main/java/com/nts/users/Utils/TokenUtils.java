package com.nts.users.Utils;

import java.security.SecureRandom;
import java.util.Base64;

public final class TokenUtils {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static  final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateToken(int byteLength){
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String getOneTimePassword(){
        int otp = secureRandom.nextInt(1_000_000);
        return String.format("%06d", otp);
    }
}
