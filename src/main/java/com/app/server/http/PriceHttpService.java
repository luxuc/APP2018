package com.app.server.http;

import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.services.SessionService;
import com.app.server.services.ExternalPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Path("prices")
public class PriceHttpService {

    @OPTIONS
    @PermitAll
    public Response optionsById() {

        return Response.ok().build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public String getPrice(Object request) {

        StringBuffer stringBuffer = new StringBuffer();

        try {
            URL url = new URL("http://localhost:8080/api/mockServer");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject priceQuery = new JSONObject();
            priceQuery.put("productName", request.toString());

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(priceQuery.toString());
            wr.flush();

            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            in.close();
            con.disconnect();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return (stringBuffer.toString());
    }


}
