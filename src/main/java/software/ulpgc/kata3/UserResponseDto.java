package software.ulpgc.kata3;

import software.ulpgc.kata3.domain.model.User;

import java.time.Instant;
import java.util.List;

public class UserResponseDto {
    public Metadata metadata;
    public List<User>  users;

    public static class Metadata {
        public int totalCount;
        public String generatedAt;

        public Metadata(int totalCount) {
            this.totalCount = totalCount;
            this.generatedAt = Instant.now().toString();
        }
    }

    public UserResponseDto(List<User> users) {
        this.users = users;
        this.metadata = new Metadata(users.size());
    }
}
