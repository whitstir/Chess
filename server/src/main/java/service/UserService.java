package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult registerUser(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.username().isEmpty() ||
            request.password() == null || request.password().isEmpty() ||
            request.email() == null || request.email().isEmpty()) {
            throw new DataAccessException("Invalid input");
        }
        if (dao.getUser(request.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        dao.createUser(newUser);

        String token = generateToken();
        dao.createAuth(new AuthData(token, request.username()));

        return new RegisterResult(request.username(), token);
    }
}
