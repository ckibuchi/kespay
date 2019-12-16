package kespay.utils;

import kespay.models.Payment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Functions {
    public static JSONObject data = new JSONObject();
    public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss");
    public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    public  Functions()
    {}

    public static   JSONObject prepareB2B( String amt, String account,String timestamp,String shortcode,String coopshortCode,String passkey,String callbackurl,String pass)
    {

        try {




            //byte[] message  =(coopshortCode+passkey+timestamp).getBytes("UTF-8"); //base64.encode(Shortcode:Passkey:Timestamp)
          //  String password =  Base64.getEncoder().encodeToString(message);
          //  password=password.replaceAll("\n","");
            String plain=AESenc.decrypt(pass);
           // System.out.println(plain);
            //System.out.println(getEncryptedPasswd(plain));


            data.put("Initiator", "ckibuchi1");
            data.put("SecurityCredential",getEncryptedPasswd(plain));
            data.put("Timestamp", timestamp);
            data.put("CommandID","BusinessPayBill");
            data.put("SenderIdentifierType","4");
            data.put("RecieverIdentifierType","4");

            data.put("Amount",amt);
            data.put("PartyA",shortcode);
            data.put("PartyB", coopshortCode);
            data.put("ResultURL", callbackurl);
            data.put("QueueTimeOutURL", callbackurl);

            data.put("AccountReference",account);
            data.put("Remarks","Settlement for Parking");



        }
        catch(Exception e)
        {

            e.printStackTrace();
        }

        return  data;
    }


    static String getEncryptedPasswd(String plain) throws Exception
    {
        try
        {
            //you can also read certificate from file instead of using string contents copied from certificate as below
            String certStr = "-----BEGIN CERTIFICATE-----\nMIIGkzCCBXugAwIBAgIKXfBp5gAAAD+hNjANBgkqhkiG9w0BAQsFADBbMRMwEQYK CZImiZPyLGQBGRYDbmV0MRkwFwYKCZImiZPyLGQBGRYJc2FmYXJpY29tMSkwJwYD VQQDEyBTYWZhcmljb20gSW50ZXJuYWwgSXNzdWluZyBDQSAwMjAeFw0xNzA0MjUx NjA3MjRaFw0xODAzMjExMzIwMTNaMIGNMQswCQYDVQQGEwJLRTEQMA4GA1UECBMH TmFpcm9iaTEQMA4GA1UEBxMHTmFpcm9iaTEaMBgGA1UEChMRU2FmYXJpY29tIExp bWl0ZWQxEzARBgNVBAsTClRlY2hub2xvZ3kxKTAnBgNVBAMTIGFwaWdlZS5hcGlj YWxsZXIuc2FmYXJpY29tLmNvLmtlMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB CgKCAQEAoknIb5Tm1hxOVdFsOejAs6veAai32Zv442BLuOGkFKUeCUM2s0K8XEsU t6BP25rQGNlTCTEqfdtRrym6bt5k0fTDscf0yMCoYzaxTh1mejg8rPO6bD8MJB0c FWRUeLEyWjMeEPsYVSJFv7T58IdAn7/RhkrpBl1dT7SmIZfNVkIlD35+Cxgab+u7 +c7dHh6mWguEEoE3NbV7Xjl60zbD/Buvmu6i9EYz+27jNVPI6pRXHvp+ajIzTSsi eD8Ztz1eoC9mphErasAGpMbR1sba9bM6hjw4tyTWnJDz7RdQQmnsW1NfFdYdK0qD RKUX7SG6rQkBqVhndFve4SDFRq6wvQIDAQABo4IDJDCCAyAwHQYDVR0OBBYEFG2w ycrgEBPFzPUZVjh8KoJ3EpuyMB8GA1UdIwQYMBaAFOsy1E9+YJo6mCBjug1evuh5 TtUkMIIBOwYDVR0fBIIBMjCCAS4wggEqoIIBJqCCASKGgdZsZGFwOi8vL0NOPVNh ZmFyaWNvbSUyMEludGVybmFsJTIwSXNzdWluZyUyMENBJTIwMDIsQ049U1ZEVDNJ U1NDQTAxLENOPUNEUCxDTj1QdWJsaWMlMjBLZXklMjBTZXJ2aWNlcyxDTj1TZXJ2 aWNlcyxDTj1Db25maWd1cmF0aW9uLERDPXNhZmFyaWNvbSxEQz1uZXQ/Y2VydGlm aWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdENsYXNzPWNSTERpc3RyaWJ1 dGlvblBvaW50hkdodHRwOi8vY3JsLnNhZmFyaWNvbS5jby5rZS9TYWZhcmljb20l MjBJbnRlcm5hbCUyMElzc3VpbmclMjBDQSUyMDAyLmNybDCCAQkGCCsGAQUFBwEB BIH8MIH5MIHJBggrBgEFBQcwAoaBvGxkYXA6Ly8vQ049U2FmYXJpY29tJTIwSW50 ZXJuYWwlMjBJc3N1aW5nJTIwQ0ElMjAwMixDTj1BSUEsQ049UHVibGljJTIwS2V5 JTIwU2VydmljZXMsQ049U2VydmljZXMsQ049Q29uZmlndXJhdGlvbixEQz1zYWZh cmljb20sREM9bmV0P2NBQ2VydGlmaWNhdGU/YmFzZT9vYmplY3RDbGFzcz1jZXJ0 aWZpY2F0aW9uQXV0aG9yaXR5MCsGCCsGAQUFBzABhh9odHRwOi8vY3JsLnNhZmFy aWNvbS5jby5rZS9vY3NwMAsGA1UdDwQEAwIFoDA9BgkrBgEEAYI3FQcEMDAuBiYr BgEEAYI3FQiHz4xWhMLEA4XphTaE3tENhqCICGeGwcdsg7m5awIBZAIBDDAdBgNV HSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwEwJwYJKwYBBAGCNxUKBBowGDAKBggr BgEFBQcDAjAKBggrBgEFBQcDATANBgkqhkiG9w0BAQsFAAOCAQEAC/hWx7KTwSYr x2SOyyHNLTRmCnCJmqxA/Q+IzpW1mGtw4Sb/8jdsoWrDiYLxoKGkgkvmQmB2J3zU ngzJIM2EeU921vbjLqX9sLWStZbNC2Udk5HEecdpe1AN/ltIoE09ntglUNINyCmf zChs2maF0Rd/y5hGnMM9bX9ub0sqrkzL3ihfmv4vkXNxYR8k246ZZ8tjQEVsKehE dqAmj8WYkYdWIHQlkKFP9ba0RJv7aBKb8/KP+qZ5hJip0I5Ey6JJ3wlEWRWUYUKh gYoPHrJ92ToadnFCCpOlLKWc0xVxANofy6fqreOVboPO0qTAYpoXakmgeRNLUiar 0ah6M/q/KA==\n-----END CERTIFICATE-----";

            InputStream is = new ByteArrayInputStream(certStr.getBytes());

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);

            PublicKey pubKey = cert.getPublicKey();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte cipherBytes[] =  cipher.doFinal(plain.getBytes());

            String encrypted = java.util.Base64.getEncoder().encodeToString(cipherBytes);

            return encrypted;
        }
        catch (CertificateException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public JSONObject prepareLNMRequest(String msisdn,String amt, String account,String timestamp,String shortcode,String kespayshortCode,String passkey,String callbackurl)
    {

        try {



            System.out.println("Phone: "+msisdn);
            System.out.println("Phone2: 254"+msisdn.substring(msisdn.length() - 9));
            byte[] message  =(kespayshortCode+passkey+timestamp).getBytes("UTF-8"); //base64.encode(Shortcode:Passkey:Timestamp)
            String password =  java.util.Base64.getEncoder().encodeToString(message);
            password=password.replaceAll("\n","");
            data.put("BusinessShortCode", kespayshortCode);
            data.put("Password",password);
            data.put("Timestamp", timestamp);
            data.put("TransactionType", "CustomerPayBillOnline");
            data.put("Amount",amt);
            data.put("PartyA","254"+msisdn.substring(msisdn.length() - 9));
            data.put("PartyB", kespayshortCode);
            data.put("PhoneNumber", "254"+msisdn.substring(msisdn.length() - 9));
            data.put("CallBackURL", callbackurl);
            data.put("AccountReference",account);
            data.put("TransactionDesc",account);



        }
        catch(Exception e)
        {

            e.printStackTrace();
        }

        return  data;
    }

    public void sendSMS(Payment payment,String username,String apikey)
    {



        // Specify the numbers that you want to send to in a comma-separated list
        // Please ensure you include the country code (+254 for Kenya in this case)
        String recipients = payment.getMsisdn();
        String message="";

        if(payment.getError_code2().equalsIgnoreCase("0"))
        // And of course we want our recipients to know what we really do
        {
            message = "Dear Customer,\n" +
                    "You have successfully paid for Parking in " + payment.getCounty() + " " + payment.getSubCounty() + " For "+payment.getCarRegNo()+". Thanks for using kespay";
        }
        else if(payment.getError_code2().equalsIgnoreCase("1032"))
        {
            message = "Dear Customer,\n" +
                     "Please note that you have cancelled the transaction. Thanks for trying kespay";

        }
        else
        {
            message = "Dear Customer,\n" +
                    "We are sorry that an error occured. Please try again later..";
        }
        // Create a new instance of our awesome gateway class
        AfricasTalkingGateway gateway  = new AfricasTalkingGateway(username,apikey);

        /*************************************************************************************
         NOTE: If connecting to the sandbox:

         1. Use "sandbox" as the username
         2. Use the apiKey generated from your sandbox application
         https://account.africastalking.com/apps/sandbox/settings/key
         3. Add the "sandbox" flag to the constructor

         AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey, "sandbox");
         **************************************************************************************/

        // Thats it, hit send and we'll take care of the rest. Any errors will
        // be captured in the Exception class below
        try {
            JSONArray results = gateway.sendMessage(recipients, message);
System.out.println("SMS Resp: "+results);
            for( int i = 0; i < results.length(); ++i ) {
                JSONObject result = results.getJSONObject(i);
                System.out.print(result.getString("status") + ","); // status is either "Success" or "error message"
                System.out.print(result.getLong("statusCode") + ",");
                System.out.print(result.getString("number") + ",");
                System.out.print(result.getString("messageId") + ",");
                System.out.println(result.getString("cost"));
            }
        } catch (Exception e) {
            System.out.println("Encountered an error while sending " + e.getMessage());
        }
    }


}
