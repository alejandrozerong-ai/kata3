package software.ulpgc.kata3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.ulpgc.kata3.adapters.RandomUserStore;
import software.ulpgc.kata3.adapters.UserResponseDto;
import software.ulpgc.kata3.domain.model.User;
import software.ulpgc.kata3.ports.UserStore;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserStore userStore = new RandomUserStore(5);
        List<User> users = userStore.load();

        UserResponseDto response = new UserResponseDto(users);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(response));
    }
}
