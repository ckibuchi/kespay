package kespay.utils;
import java.security.MessageDigest;
import java.security.Security;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import java.math.BigInteger;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;

public class PasswordUtil
{
    public static String securityCertificate="D:\\My Data\\kespay\\cert.cer";
    public PasswordUtil()
    {}
    public static void main(String args[])
    {
        try {
            //String plain = AESenc.decrypt("R66ZP0v3K+P2D3IRnKDhsw==");
           // System.out.println(plain);
          //  System.out.println(Functions.getEncryptedPasswd(plain));
             System.out.println(encryptInitiatorPassword( securityCertificate, "Chr1s2019"));
        }
        catch(Exception e)
        {e.printStackTrace();}

    }
    public static String encryptInitiatorPassword(String securityCertificate, String password) {
        String encryptedPassword = "YOUR_INITIATOR_PASSWORD";
        try {



            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            byte[] input = password.getBytes();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            FileInputStream fin = new FileInputStream(new File(securityCertificate));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fin);
            PublicKey pk = certificate.getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, pk);

            byte[] cipherText = cipher.doFinal(input);

            // Convert the resulting encrypted byte array into a string using base64 encoding
            encryptedPassword = Base64.encode(cipherText);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedPassword;
    }
}