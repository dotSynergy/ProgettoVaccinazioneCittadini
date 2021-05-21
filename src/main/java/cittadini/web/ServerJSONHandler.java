package cittadini.web;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
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
    public CompletableFuture<JSONArray> makeRequest() throws IOException, InterruptedException {
        return makeRequest(this.data, this.endpoint, WebMethods.GET);
    }

    /**
     * @return
     */
    public CompletableFuture<JSONArray> makeRequest(JSONObject data) throws IOException, InterruptedException {
        return makeRequest(data, this.endpoint, WebMethods.GET);
    }

    /**
     * @param data
     * @return
     */
    public CompletableFuture<JSONArray> makeRequest(JSONObject data, String endpoint, WebMethods method) throws IOException, InterruptedException {
        this.endpoint = endpoint;
        this.data = data;

        return CompletableFuture.supplyAsync(new Supplier<JSONArray>() {
            @Override
            public JSONArray get() {
                url = URI.create(baseUrl + endpoint);

                var request = HttpRequest.newBuilder()
                        .uri(url)
                        .header("Content-Type", "application/json");
                HttpRequest.BodyPublisher p = HttpRequest.BodyPublishers.ofString(data.toString());
                request.method(method.name(), p);

                var client = HttpClient.newHttpClient();
                try {
                    HttpResponse<String> t = client.send(request.build(), HttpResponse.BodyHandlers.ofString());

                    if(t.statusCode() != 200)
                        throw new ServerStatusException(t.statusCode());
                    return new JSONArray(t.body());

                } catch (IOException | InterruptedException | ServerStatusException e) {
                    e.printStackTrace();
                }
                return new JSONArray("[{}]");
            }
        });
    }

}
