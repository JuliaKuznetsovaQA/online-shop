package com.kuznetsova.online_shop.dao;

import com.kuznetsova.online_shop.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {
    private static int USER_COUNT;
    private List<User> users;

    {
        users = new ArrayList<>();
        users.add(new User(++USER_COUNT, "Tom", "tom@mail.ru", "1234"));
        users.add(new User(++USER_COUNT, "Bob", "bob@gmail.com", "abc"));
        users.add(new User(++USER_COUNT, "Mike", "mike@yahoo.com", "asdf"));
        users.add(new User(++USER_COUNT, "Katy", "katy@yandex.ru", "4321"));
    }

    public List<User> index() {
        return users;
    }

    public User show(int id) {
        return users.stream().filter(user -> user.getId() == id).findAny().orElse(null);
    }

    public void save(User user) {
        user.setId(++USER_COUNT);
        users.add(user);
    }

    public void update(int id, User updatedUser) {
        User userToBeUpdated = show(id);
        userToBeUpdated.setName(updatedUser.getName());
        userToBeUpdated.setEmail(updatedUser.getEmail());
        userToBeUpdated.setPassword(updatedUser.getPassword());
    }

    public void delete(int id) {
        users.removeIf(user -> user.getId() == id);
    }
}
