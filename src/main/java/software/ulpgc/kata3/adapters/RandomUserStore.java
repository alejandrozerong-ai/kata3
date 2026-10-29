package software.ulpgc.kata3.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.kata3.UserDto;
import software.ulpgc.kata3.domain.model.User;
import software.ulpgc.kata3.ports.UserDisplay;
import software.ulpgc.kata3.ports.UserStore;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RandomUserStore implements UserStore {
    private final int size;

    public RandomUserStore(int size) {
        this.size = size;
    }

    @Override
    public List<User> load() {
        try {
            return loadFrom(new URI("http://randomuser.me/api?results=" + size));

        } catch (Exception e)  {
            throw new RuntimeException(e);
        }
    }

    private List<User> loadFrom(URI uri) throws IOException, InterruptedException {
        return loadFrom(createRequestFor(uri));
    }

    private List<User> loadFrom(HttpRequest request) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            return loadFrom(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        }

    }

    private List<User> loadFrom(String json) {
        JsonElement results = new Gson().fromJson(json, JsonObject.class).get("results");
        if (results == null || !results.isJsonArray()) {
            return new ArrayList<>();
        }
        return loadFrom(results.getAsJsonArray());
    }

    private List<User> loadFrom(JsonArray array) {
        return loadFrom(array.asList());
    }

    private List<User> loadFrom(List<JsonElement> list) {
        List<User> users = new ArrayList<>();
        for (JsonElement element : list) {
            try {
                User user = toUser(element);
                if (user != null) {
                    users.add(user);
                }
            } catch (Exception e) {}
        }
        return users;
    }

    private User toUser(JsonElement element) {
        UserDto dto = new Gson().fromJson(element, UserDto.class);
        if (dto.name == null || dto.dob == null || dto.dob.date == null) {
            return null;
        }
        return new User(
                dto.name.first + " " + dto.name.last,
                Instant.parse(dto.dob.date)
        );
    }

    private HttpRequest createRequestFor(URI uri) {
        return HttpRequest.newBuilder(uri).GET().build();
    }
}
