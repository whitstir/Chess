package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.username() == null || registerRequest.username().isEmpty() ||
            registerRequest.password() == null || registerRequest.password().isEmpty() ||
            registerRequest.email() == null || registerRequest.email().isEmpty()) {
            throw new DataAccessException("Invalid input");
        }
        if (dao.getUser(registerRequest.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dao.createUser(newUser);

        String token = generateToken();
        dao.createAuth(new AuthData(token, newUser.username()));

        return new RegisterResult(newUser.username(), token);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if (loginRequest.username() == null || loginRequest.username().isEmpty() ||
            loginRequest.password() == null || loginRequest.password().isEmpty()) {
            throw new DataAccessException("Invalid input");
        }
        UserData newUser = dao.getUser(loginRequest.username());
        if (newUser == null) {
            throw new DataAccessException("User does not exist");
        }
        if (!Objects.equals(newUser.password(), loginRequest.password())) {
            throw new DataAccessException("Incorrect password");
        }

        String token = generateToken();
        dao.createAuth(new AuthData(token, newUser.username()));

        return new LoginResult(newUser.username(), token);
    }
}
