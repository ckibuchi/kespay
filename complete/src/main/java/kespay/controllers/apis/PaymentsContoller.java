package kespay.controllers.apis;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import kespay.enums.PaymentStatus;
import kespay.models.Payment;
import kespay.repositories.PaymentRepository;
import kespay.utils.Functions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/payments")
public class PaymentsContoller {
    private Logger logger = LoggerFactory.getLogger(PaymentsContoller.class);

    @PostMapping("/getparkings")
    //@Produces({MediaType.APPLICATION_JSON})
    public String getparkingsbyRegandDate(@RequestParam(name = "carRegNo", required = true) String carRegNo)
    {
        List<JSONObject> response = new ArrayList<JSONObject>();
        Date utilDate= new Date();
        java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());
        List<Payment> payments= paymentRepository.findPaymentsByCarRegNoAndPaymentDate(carRegNo.replaceAll("[^a-zA-Z0-9]", "").trim(),sqlDate);
        if(!payments.isEmpty())
        {
            Gson gson = new Gson();
            for (Payment payment : payments) {
                String jsonString = gson.toJson(payment);
                try {
                    JSONObject paymentobject = new JSONObject(jsonString);
                    response.add(paymentobject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
return  response.toString() ;

    }
    @RequestMapping("/search-payments")
    //@Produces({MediaType.APPLICATION_JSON})
    public String searchPayments(@RequestParam(name = "carRegNo", required = false,defaultValue = "") String carRegNo,
                                   @RequestParam(name = "county", required = false,defaultValue = "") String county,
                                   @RequestParam(name = "subCounty", required = false,defaultValue = "") String subCounty,
                                   @RequestParam(name = "dateFrom", required = false,defaultValue = "") String dateFrom,
                                   @RequestParam(name = "dateTo", required = false,defaultValue = "") String dateTo,
                                   @RequestParam(name = "status", required = false,defaultValue = "") String status) {


        List<JSONObject> response = new ArrayList<JSONObject>();
        Date utilDate= new Date();
        java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());
        String carRegno2=carRegNo.replaceAll("[^a-zA-Z0-9]", "").trim();
        List<Payment> payments=null;

        if(carRegno2.isEmpty() && county.isEmpty() && subCounty.isEmpty() && dateFrom.isEmpty() && dateTo.isEmpty()) {
            logger.info ("No filters.... Show all transactions for today ");
            payments=  paymentRepository.findPaymentsByPaymentDate(sqlDate);
        }
        else if(dateFrom.isEmpty() || dateTo.isEmpty())
        {
            logger.info ("No Dates...");
            payments=  paymentRepository.findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDate(carRegno2,county,subCounty,sqlDate);
        }
        else
        {
            try {
                java.util.Date datef = Functions.sdf1.parse(dateFrom);
                java.sql.Date sqlDateFrom = new java.sql.Date(datef.getTime());

                java.util.Date datet = Functions.sdf1.parse(dateTo);
                java.sql.Date sqlDateTo = new java.sql.Date(datet.getTime());
               if(status.isEmpty())
               {payments=  paymentRepository.findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDateGreaterThanEqualAndPaymentDateIsLessThanEqual(carRegno2,county,subCounty,sqlDateFrom,sqlDateTo);}
               else
               {

                   payments=  paymentRepository.findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDateGreaterThanEqualAndPaymentDateIsLessThanEqualAndStatusEquals(carRegno2,county,subCounty,sqlDateFrom,sqlDateTo,status.equals("COMPLETED")?PaymentStatus.COMPLETED:PaymentStatus.FAILED);

               }
                logger.info ("dateFrom >>"+dateFrom +" sqlDateFrom >> "+sqlDateFrom);
                logger.info ("sqlDateTo >> "+sqlDateTo+" sqlDateTo >> "+sqlDateTo);
            }
            catch(Exception e)
            {
                logger.info ("Date conversion Failed Badly!!!!!!!!!!!!!!!");
                e.printStackTrace();

                payments=  paymentRepository.findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDate(carRegno2,county,subCounty,sqlDate);


            }


        }



        if(!payments.isEmpty())
        {
            Gson gson = new Gson();
            for (Payment payment : payments) {
                String jsonString = gson.toJson(payment);
                try {
                    JSONObject paymentobject = new JSONObject(jsonString);
                    response.add(paymentobject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return  response.toString() ;
     //   return Response.ok(response.toString()).header("Access-Control-Allow-Origin", "*").header(  "Access-Control-Allow-Headers","Authorization").build();

    }

    @PostMapping("/getTodaysTotal")
    //@Produces({MediaType.APPLICATION_JSON})
    public String getTodaysTotal()
    {
        String response="0.00";
        try{

            Date utilDate= new Date();
            java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());
            response=""+paymentRepository.getTotalToday(sqlDate);
        }
        catch(Exception e)
        {e.printStackTrace();}

        return  response ;

    }

    @PostMapping("/getTodaysCount")
    //@Produces({MediaType.APPLICATION_JSON})
    public String getTodaysCount( )
    {
        String response="0";
        try{
            Date utilDate= new Date();
            java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());
            response=""+paymentRepository.countByPaymentDateAndStatus(sqlDate,PaymentStatus.COMPLETED);

        }
        catch(Exception e)
        {e.printStackTrace();}

        return  response ;

    }



    @Autowired
    PaymentRepository paymentRepository;
}
