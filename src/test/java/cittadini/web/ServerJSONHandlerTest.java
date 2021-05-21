package cittadini.web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class ServerJSONHandlerTest {

    @DisplayName("Test makeRequest")
    @Test
     void makeRequest() {
        ServerJSONHandler s = new ServerJSONHandler();

        try {
            JSONObject jsonobj = new JSONObject("{\'userName\': \'test\', \'password\': \'test\'}");
            CompletableFuture<JSONArray> json = s.makeRequest(jsonobj, "rpc/login_cittadino", WebMethods.POST);
            System.out.println("started: " + json);
            System.out.println("completed: " + json.join());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}