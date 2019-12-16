package kespay.controllers;

import kespay.enums.PaymentMethod;
import kespay.enums.PaymentStatus;
import kespay.models.Settlement;
import kespay.repositories.PaymentRepository;
import kespay.repositories.SettlementRepository;
import kespay.utils.Functions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static String bearer="";
    public static JSONObject data = new JSONObject();


    @Value("${mpesa.kespay.passkey}")
    private String mpesapasskey;

    @Value("${mpesa.callbackurl}")
    private String callbackurl;

    @Value("${mpesa.shortcode}")
    private String shortcode;
    @Value("${initiator.pass}")
    private String pass;
    @Value("${mpesa.coop.shortcode}")
    private String coopshortcode;

    @Value("${mpesa.authurl}")
    String authurl;

    @Value("${mpesa.appsecret}")
    String app_secret;

    @Value("${mpesa.appkey}")
    String app_key;

    @Value("${mpesa.b2b.endpoint}")
    String b2bendpoint;

    @Value("${county.percent}")
    double county_percent;

    @Value("${county.account}")
    String account;


    @Scheduled(fixedDelay  = 1800000)
    public void renewBearer() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        try {


            String appKeySecret = this.app_key + ":" + this.app_secret;
            System.out.println("this.app_key + this.app_secret "+this.app_key + ":" + this.app_secret);
            byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
            String auth= Base64.getEncoder().encodeToString(bytes);

            final OkHttpClient client = new OkHttpClient();
            log.info("this.authurl "+this.authurl);

            Request request = new Request.Builder()
                    .url(this.authurl)
                    .get()
                    .addHeader("Authorization", "Basic " + auth.trim())
                    //.addHeader("cache-control", "no-cache")
                    .build();
            Response response  =    client.newCall(request).execute();
            // Background Code
            String resp=response.body().string();
            log.info("response "+resp);
            JSONObject results=new JSONObject(resp);
            if(results.has("access_token"))
            {
                try{
                    log.info("token: "+results.getString("access_token"));
                    this.bearer=results.getString("access_token");



                }
                catch(Exception e)
                {e.printStackTrace();
                }
            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }


    }



    @Scheduled(cron = "59 59 23 * * *",zone = "EAT")
    public void settlePayments()
    {
        log.info("**********Settling Payments now *******");

        Date now = new Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        log.info("time now is :>> "+now+" >> "+ now.getTime());
        List<Settlement> settlementList=settlementRepository.findAllBySettlementDateAndStatus(sqlDate,PaymentStatus.COMPLETED);
        log.info("Found settlements? : "+settlementList.size());
        if(settlementList.size()>0)
        {
            log.info("Settlement for "+sqlDate+" exists!");
            return;
        }
        Settlement settlement=new Settlement();
        settlement.setSettlementDate(sqlDate);
       try {
           settlement.setSettlementDate(sqlDate);
           String timestamp = new String(Functions.sdf.format(now)).replaceAll("-", "");
           Float todaysamount = paymentRepository.getTotalToday(sqlDate);
           todaysamount=(todaysamount==null?0:todaysamount);
           settlement.setTotalAmount(""+todaysamount);
           log.info(" Total Amount : "+todaysamount);
           Double amount= (county_percent * todaysamount)/100.0;
           log.info(" County's Share : "+amount);
           settlement.setAmountPaid(""+amount.intValue());
           settlement.setServiceCharge(""+(todaysamount-amount.intValue()));
           if(amount>=1.0) {

               data = Functions.prepareB2B(""+amount.intValue(), account, timestamp, shortcode, coopshortcode, mpesapasskey, callbackurl,pass);
             //  log.info("Request "+data.toString());
               //log.info("b2bendpoint "+b2bendpoint);

               settlement.setPaymentMethod(PaymentMethod.MPESA);
               settlement.setPercent(""+county_percent);
               settlement.setStatus(PaymentStatus.STARTED);
               settlement=settlementRepository.save(settlement);
               String result = webClient.darajaRequest(b2bendpoint, data.toString());
               settlement.setStatus(PaymentStatus.INIT);
               ////
               settlement.setStatus(PaymentStatus.COMPLETED);
               ////
               settlementRepository.save(settlement);
               log.info("LNM REQ RES" + result);

               JSONObject results = new JSONObject(result);
           }
           else
           {
               log.info("Amount to transfer is below 1");
               settlement.setStatus(PaymentStatus.FAILED);
               settlement.setPercent(""+county_percent);
           }
       }
       catch(Exception e)
       {
           settlement.setStatus(PaymentStatus.FAILED);
           settlementRepository.save(settlement);
           log.info("ERROR: " + e.getMessage());
           log.info("ERROR: " + e.getStackTrace());

       }

    }


    @Autowired
    WebClient webClient;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    SettlementRepository settlementRepository;
}