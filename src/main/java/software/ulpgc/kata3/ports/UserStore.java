package software.ulpgc.kata3.ports;

import software.ulpgc.kata3.domain.model.User;

import java.util.List;

public interface UserStore {
    List<User> load();
}
