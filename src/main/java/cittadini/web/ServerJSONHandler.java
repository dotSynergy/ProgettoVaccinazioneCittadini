package cittadini.web;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * This class creates an object tasked with communicating with the server.
 *
 */
public class ServerJSONHandler {

    /*
     * Request parameters
     */
    private final String baseUrl = "http://dev.meliver.it:3000/";
    private String endpoint;
    private JSONObject data;
    private URI url;
    private static String jwt;

    /*
     * Response parameters
     */
    private CompletableFuture<JSONObject> response;
    private Integer responseCode;

    /**
     * Instantiates a new Server json handler.
     */
    public ServerJSONHandler() {

    }

    /**
     * @param endpoint
     */
    public ServerJSONHandler(String endpoint) {
        this.endpoint = endpoint;
    }

    public URI getURI(){
        return URI.create(baseUrl + endpoint);
    }

    public void setEndpoint(String endpoint){
        this.endpoint = endpoint;
    }

    public void setJWT(String jwt){
        this.jwt = jwt;
    }

    /**
     * @return
     */
    public CompletableFuture<JSONObject> makeGETRequest() throws IOException, InterruptedException {
        return makeGETRequest(this.data, this.endpoint);
    }

    /**
     * @return
     */
    public CompletableFuture<JSONObject> makeGETRequest(JSONObject data) throws IOException, InterruptedException {
        return makeGETRequest(data, this.endpoint);
    }

    /**
     * @param data
     * @return
     */
    public CompletableFuture<JSONObject> makeGETRequest(JSONObject data, String endpoint) throws IOException, InterruptedException {
        this.endpoint = endpoint;
        this.data = data;

        return CompletableFuture.supplyAsync(new Supplier<JSONObject>() {
            @Override
            public JSONObject get() {
                url = URI.create(baseUrl + endpoint);

                var request = HttpRequest.newBuilder()
                        .uri(url)
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(data.toString()))
                        .build();

                var client = HttpClient.newHttpClient();
                try {
                    return new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

}
