package software.ulpgc.kata3.adapters;

import software.ulpgc.kata3.domain.model.User;
import software.ulpgc.kata3.ports.UserDisplay;

public class ConsoleUserDisplay implements UserDisplay {
    @Override
    public void show(User user) {
        System.out.println(user);
    }
}

