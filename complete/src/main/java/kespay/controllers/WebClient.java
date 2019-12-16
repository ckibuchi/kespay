package kespay.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
@Component
public interface WebClient {
    String darajaRequest(String mpesaendpoint, String data);
    String getBearer();
}
