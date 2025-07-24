package in.software.billingSoftware.Service;

import in.software.billingSoftware.io.UserRequest;
import in.software.billingSoftware.io.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readUser();
    void deleteUser(String id);
}
