package kespay.controllers;
import kespay.enums.PaymentMethod;
import kespay.enums.PaymentStatus;
import kespay.models.Payment;
import kespay.repositories.PaymentRepository;
import kespay.utils.Functions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class Ussd {


    Functions fn=new Functions();
    public static JSONObject data = new JSONObject();

    @Value("${mpesa.kespay.passkey}")
    private String mpesapasskey;

    @Value("${mpesa.callbackurl}")
    private String callbackurl;

    @Value("${mpesa.shortcode}")
    private String shortcode;

    @Value("${mpesa.kespay.shortcode}")
    private String kespayshortcode;

    @Value("${nairobi.rate}")
    private String nairobirate;

    @Value("${masaku.rate}")
    private String masakurate;

    @Value("${mpesa.endpoint}")
    String mpesaendpoint;

    @Value("${mpesa.authurl}")
    String authurl;

    @Value("${mpesa.appsecret}")
    String app_secret;

    @Value("${mpesa.appkey}")
    String app_key;





    @PostMapping("/ussd")
        public String ussd (@RequestParam(name = "sessionId", required = false)  String sessionId,@RequestParam(name = "serviceCode", required = false) String serviceCode,@RequestParam(name = "phoneNumber", required = false) String phoneNumber,@RequestParam(name = "text", required = false) String text) {
        String response = "";
        String balance="--.--";
        String parkingareas="--";
        System.out.println("text is |"+text+"|");
        System.out.println("phoneNumber is |"+phoneNumber+"|");
        if ( text.equalsIgnoreCase("")) {

            // This is the first request. Note how we start the response with CON
            response  = "CON Please make a choice \n";
            response += "1. Pay for Parking \n";
            response += "2. Check My Parking";

        }

        else if ( text.trim().equalsIgnoreCase("1") ) {
            // Business logic for first level response
            response = "CON Choose County \n";
            response += "1. Nairobi \n";
            response += "2. Machakos";

        }

        else if(text.trim().equalsIgnoreCase("2")) {

            // Business logic for first level response
            Date utilDate= new Date();
            java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());
            List<Payment> payments= paymentRepository.findPaymentsByMsisdnAndPaymentDateAndStatus(phoneNumber,sqlDate,PaymentStatus.COMPLETED);
            String parkings="Dear Customer,\n" +
                    "You have paid for below parkings:\n";
            if(payments.isEmpty())
            {
                parkings="Dear Customer,\n" +
                        "We are sorry there are no payments found for you today.";
            }
            else
            {
                int i=1;
                for (Payment payment : payments) {
                    parkings+=i+". "+payment.getCounty()+" "+payment.getSubCounty()+" "+ payment.getCarRegNo()+"\n";
                    i+=1;

                }
            }

            // This is a terminal request. Note how we start the response with END
            response = "END "+parkings;
        }


        else if(text.trim().equalsIgnoreCase("1*1")) {

            // This is a second level response where the user selected 1 in the first instance
            parkingareas  = "1. CBD\n" +
                            "2. WESTLANDS";
            // This is a terminal request. Note how we start the response with END
            response = "CON Please choose area\n"+parkingareas;
        }

        else if ( text.trim().equalsIgnoreCase("1*2" )) {

            // This is a second level response where the user selected 1 in the first instance
            parkingareas  = "1. CBD";
            // This is a terminal request. Note how we start the response with END
            response = "CON Please choose area\n"+parkingareas;

        }
        else if ( text.trim().equalsIgnoreCase("1*1*1" )|| text.trim().equalsIgnoreCase("1*2*1" ) || text.trim().equalsIgnoreCase("1*2*2" ) || text.trim().equalsIgnoreCase("1*1*2" )) {

            // This is a terminal request. Note how we start the response with END
            response = "CON Enter Car Reg No.\n";



        }
        else {
            System.out.println("array length: "+text.split("\\*").length);
            String[] request=text.split("\\*");
            if (request.length == 4) {
                String county="";
                String amount="";
                String subcounty="";
                 if(request[1].equalsIgnoreCase("1"))
                 {
                     county="NRB";
                     amount=nairobirate;
                     if(request[2].equalsIgnoreCase("1"))
                     {
                         subcounty="CBD";
                     }
                     if(request[2].equalsIgnoreCase("2"))
                     {
                         subcounty="WESTLANDS";
                     }
                 }
                if(request[1].equalsIgnoreCase("2"))
                {
                    amount=masakurate;
                    county="MACHAKOS";
                    if(request[2].equalsIgnoreCase("1"))
                    {
                        subcounty="CBD";
                    }
                   /* if(request[2].equalsIgnoreCase("2"))
                    {
                        subcounty="MTWAPA";
                    }*/
                }

                 String regno=request[3];
                 System.out.println("regno before: "+regno);
                 regno=regno.replaceAll("[^a-zA-Z0-9]", "").trim();
                System.out.println("regno After: "+regno);
                /*System.out.println("Mombasa Rate  "+masakurate);
                System.out.println("Nai Rate  "+nairobirate);
                System.out.println("amount  "+amount);*/
                Date utilDate= new Date();
                java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());

                System.out.println("regno:"+regno+" county: "+county+" subcounty: "+subcounty+" sqlDate: "+sqlDate);

                List<Payment> payments= paymentRepository.findPaymentsByCarRegNoAndCountyAndSubCountyAndPaymentDateAndStatus(regno,county,subcounty,sqlDate,PaymentStatus.COMPLETED);
                System.out.println("payments: "+payments);
                System.out.println(" payments.isEmpty(): "+payments.isEmpty());
                System.out.println("payments.size(): "+payments.size());

                if(payments.isEmpty()) {
                    String stkpushresp = stkPush(Double.parseDouble(amount), phoneNumber, regno, county, subcounty);
                    response = "END " + stkpushresp;
                }
                else
                {
                    response = "END Sorry, You have already paid for " +regno+" in "+subcounty+",  "+county;
                }
            }
            else
            {

                response = "END Sorry, we did not understand your request";

            }

        }

        System.out.println(response);
// Print the response onto the page so that our gateway can read it
  return response;

        //return plainTextResponseEntity(response);


    }

    public String stkPush(Double the_amount,String msisdn,String regNo,String county,String subcounty)
    {
        String amount=new String(""+String.format("%.0f", the_amount));

        Date now=new Date();

        String  timestamp=new String(Functions.sdf.format(now)).replaceAll("-","");
        Payment payment = new Payment();
        try {

            payment.setPaymentMethod(PaymentMethod.MPESA);
            payment.setAmount(amount);
            payment.setCounty(county);
            payment.setSubCounty(subcounty);
            //payment.setParkingLot(parking);
            payment.setCarRegNo(regNo);
            payment.setStartTime(timestamp);
            payment.setMsisdn(msisdn);
            payment.setStatus(PaymentStatus.INIT);
            payment=paymentRepository.save(payment);
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        data=fn.prepareLNMRequest(msisdn,amount,regNo,timestamp,shortcode,kespayshortcode,mpesapasskey,callbackurl);
        System.out.println("Request "+data.toString());
        System.out.println("authurl "+authurl);
      //  WebClient client=new WebClient( mpesaendpoint, authurl, app_secret, app_key);
      //  String result = client.darajaRequest(data.toString());
        String result = webClient.darajaRequest(mpesaendpoint,data.toString());

        System.out.println("LNM REQ RES"+ result);

        JSONObject results=new JSONObject(result);

        if(results.has("fault"))
        {
            payment.setDesc1("fault");
            payment.setError_code1("001");
            payment.setStatus(PaymentStatus.FIRST_FAILED);
            paymentRepository.save(payment);
           return "Sorry, an error occured. Try again";
        }
        else if(results.has("errorMessage"))
        {
            if(results.has("errorCode"))
            {
                if(results.getString("errorCode").equalsIgnoreCase("404.001.03"))
                {
                    try {

                        Date now2=new Date();

                        String  timestamp2=new String(Functions.sdf.format(now2)).replaceAll("-","");

                        payment.setDesc1("Sorry, auth error occured. Try again");
                        payment.setError_code1(results.getString("errorCode"));
                        payment.setStatus(PaymentStatus.FIRST_FAILED);
                        paymentRepository.save(payment);
                        return "Sorry, auth error occured. Try again";

                    }
                    catch(Exception e)
                    {e.printStackTrace();}

                }

            }
            return results.getString("errorMessage");

        }
        else if(results.has("ResponseCode"))
        {
            if(results.has("CustomerMessage")) {
                payment.setDesc1(results.getString("CustomerMessage"));
                payment.setError_code1(results.getString("ResponseCode"));
                payment.setRefID(results.getString("CheckoutRequestID"));
                payment.setStatus(PaymentStatus.RECEIVED);
                paymentRepository.save(payment);
                return results.getString("CustomerMessage");
            }
            else
            {
                payment.setDesc1("Unknown response");
                payment.setError_code1("4001");
                payment.setStatus(PaymentStatus.FIRST_FAILED);
                paymentRepository.save(payment);
                return "Unknown response";}

        }

        else
        {
            payment.setDesc1("Unknown Error");
            payment.setError_code1("4001");
            payment.setStatus(PaymentStatus.FIRST_FAILED);
            paymentRepository.save(payment);
            return "Unknown Error";
        }
    }

    public String plainTextResponseEntity(String response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
        return new ResponseEntity(response, httpHeaders, HttpStatus.OK).toString();
    }
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    WebClient webClient;

}
