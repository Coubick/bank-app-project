package org.example.services;

import org.example.enums.EventTypes;
import org.example.enums.AddFindDeleteResult;
import org.example.enums.Colors;
import org.example.enums.Gender;
import org.example.producers.UserEventProducer;
import org.example.user_dao.User;
import org.example.user_dao.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserEventProducer userEventProducer;
    private final String NOT_FOUND_USER_ERROR = "User not found";
    private final UserRepository userRepository;

    public UserServiceImpl(UserEventProducer userEventProducer, UserRepository userRepository) {
        this.userEventProducer = userEventProducer;
        this.userRepository = userRepository;
    }

    @Override
    public String getInformationAboutUser(String login) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            return String.format(
                    "Name: %s\n" +
                            "Login: %s\n" +
                            "Age: %s\n" +
                            "Gender: %s\n" +
                            "Hair color: %s\n",
                    user.getName(), user.getLogin(), user.getAge(), user.getGender(), user.getHairColor()
            );
        }
        return NOT_FOUND_USER_ERROR;
    }

    @Override
    public AddFindDeleteResult addUser(User user) {
        if (userRepository.findByLogin(user.getLogin()) == null) {
            userRepository.save(user);
            userEventProducer.sendUserEvent(user.getLogin(), user, String.valueOf(EventTypes.CREATED));
            return AddFindDeleteResult.Success;
        }
        return AddFindDeleteResult.AlreadyExists;
    }

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> filterUsers(Gender gender, Colors color) {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> gender == null || user.getGender() == gender)
                .filter(user -> color == null || user.getHairColor() == color)
                .toList();
    }

    @Override
    public AddFindDeleteResult addFriend(String login1, String login2) {
        if (login1.equals(login2)) return AddFindDeleteResult.Error;

        User user1 = userRepository.findByLogin(login1);
        User user2 = userRepository.findByLogin(login2);

        if (user1 == null || user2 == null) return AddFindDeleteResult.UserNotFound;

        if (user1.getFriends().contains(user2)) return AddFindDeleteResult.AlreadyExists;

        user1.getFriends().add(user2);
        user2.getFriends().add(user1);

        userRepository.save(user1);
        userRepository.save(user2);

        return AddFindDeleteResult.Success;
    }

    @Override
    public AddFindDeleteResult removeFriend(String login1, String login2) {
        User user1 = userRepository.findByLogin(login1);
        User user2 = userRepository.findByLogin(login2);

        if (user1 == null || user2 == null) return AddFindDeleteResult.UserNotFound;

        if (!user1.getFriends().contains(user2)) return AddFindDeleteResult.UserNotFound;

        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);

        userRepository.save(user1);
        userRepository.save(user2);

        return AddFindDeleteResult.Success;
    }

    @Override
    public List<User> findFriends(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) return new ArrayList<>();
        return user.getFriends();
    }
}