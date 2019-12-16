package kespay.controllers.apis;

import com.algolia.search.APIClient;
import com.algolia.search.ApacheAPIClientBuilder;
import com.algolia.search.Index;
import kespay.beans.PostBean;
import kespay.enums.PayBillStatus;
import kespay.models.Church;
import kespay.repositories.ChurchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/churches")
public class ChurchesController {

    @PostMapping("/createChurch")
    public String post(@RequestBody PostBean bean) {

        HashMap<String, String> map=bean.getMap();
        Date utilDate= new Date();
        java.sql.Date sqlDate =new java.sql.Date(utilDate.getTime());

       try {
           Church newchurch = new Church();
           newchurch.setName(map.get("name"));
           newchurch.setAltName(map.get("altName"));
           newchurch.setPayBill(map.get("payBill"));
           newchurch.setCreationDate(sqlDate);
           newchurch.setStatus(PayBillStatus.ACTIVE);
           newchurch = churchRepository.save(newchurch);

           //algolia stuff starts here..
           try{

               java.security.Security.setProperty("networkaddress.cache.ttl", "60");
               APIClient client =
                       new ApacheAPIClientBuilder("V6SCF4H8H6", "b294ae630f797e9a055c700753b6f4f4").build();

               Index<Church> index = client.initIndex("Church", Church.class);
               index.addObject(newchurch);


           }
           catch(Exception e)
           {e.printStackTrace();}

           //algolia stuff ends here...
           return "{'status':'success','message':'Completed Successfully'}";
       }
       catch(Exception e)
       {
           return "{'status':'error','message':'"+e.getMessage()+"'}";
       }


    }



    @Autowired
    ChurchRepository churchRepository;
}
