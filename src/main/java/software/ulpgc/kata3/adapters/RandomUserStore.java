package software.ulpgc.kata3.adapters;

import com.google.gson.*;
import software.ulpgc.kata3.UserDto;
import software.ulpgc.kata3.domain.model.User;
import software.ulpgc.kata3.ports.UserStore;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongUnaryOperator;

public class RandomUserStore implements UserStore {
    private final int size;

    public RandomUserStore(int size) {
        this.size = size;
    }

    @Override
    public List<User> load() {
        try {
            return loadFrom(new URI("https://randomuser.me/api?results=" + size));
        } catch (Exception e) {
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
        if (results == null || ! results.isJsonArray()) {
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

        String rawDate =dto.dob.date;
        String formattedDate = rawDate.length() > 10 ? rawDate.substring(0, 10) : rawDate;

        return new User(
                dto.name.first + " " + dto.name.last,
                formattedDate,
                dto.dob.age,
                dto.gender
        );
    }

    private HttpRequest createRequestFor(URI uri) {
        return HttpRequest.newBuilder(uri).GET().build();
    }
}
