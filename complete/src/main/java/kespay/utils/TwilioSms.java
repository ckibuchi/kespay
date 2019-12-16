package kespay.utils;

public class TwilioSms {

}
/*
import com.twilio.Twilio;
        import com.twilio.rest.api.v2010.account.Message;
        import com.twilio.type.PhoneNumber;

public class TwilioSms {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACf70c4611c05215c0a2a988208e0e9753";
    public static final String AUTH_TOKEN = "577cc4a0ecdb316096bec7d1fabce44c";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+254723851709"),
                new PhoneNumber("+14178153288"),
                "Testing Twilio for AA..?").create();

        System.out.println("Mess: "+message.getErrorMessage()+ " status "+message.getStatus());

        System.out.println(message.getSid());
    }
}*/