package next.dao;

import next.model.User;

import java.util.List;

public interface UserDao {

    void insert(User user);

    void update(User user);

    List<User> findAll();

    User findByUserId(String userId);
}
