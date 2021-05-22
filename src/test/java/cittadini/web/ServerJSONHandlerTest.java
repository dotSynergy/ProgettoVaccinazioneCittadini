package cittadini.web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * The type Server json handler test.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue -
 *       @author Andrea Pini - mat.740675
 *
 */
class ServerJSONHandlerTest {

    /**
     * Make request.
     */
    @DisplayName("Test makeRequest")
    @Test
     void makeRequest() {
        ServerJSONHandler s = new ServerJSONHandler();

        try {
            JSONObject jsonobj = new JSONObject("{\'userName\': \'test\', \'password\': \'test\'}");

            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("/rpc/login_cittadino")
                    .setData(jsonobj)
                    .makeRequest();

            System.out.println("started: " + json);
            System.out.println("completed: " + json.join());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}