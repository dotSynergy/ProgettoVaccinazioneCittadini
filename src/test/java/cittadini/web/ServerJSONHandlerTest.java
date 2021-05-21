package cittadini.web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

class ServerJSONHandlerTest {

    @DisplayName("Test makeGETRequest")
    @Test
     void makeGETRequest() {
        ServerJSONHandler s = new ServerJSONHandler("/test");

        try {
            JSONObject jsonobj = new JSONObject("{\'test\': \'test\'}");
            CompletableFuture<JSONObject> json = s.makeGETRequest(jsonobj);
            json.join();
            System.out.println(json.toString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}