package org.example.services;

import org.example.enums.AddFindDeleteResult;
import org.example.user_dao.User;
import org.example.enums.*;

import java.util.List;

/**
 * Класс Сервис для пользователей.
 * Методы:
 * Просмотр информации о пользователе
 * Добавление пользователя в БД
 * Писк пользователя по логину
 * Фильтрация пользователей по полу, цвету волос, либо по обоим параметрам
 */

public interface UserService {

    /**
     * Получение информации о пользователе
     * @param login - логин пользователя, о котором нужно узнать информацию
     * @return форматированные данные пользователя
     */
    String getInformationAboutUser(String login);


    /**
     * Добавление пользователя в БД
     * @param user - пользователь, которого надо добавить
     * @return результат добавления
     */
    AddFindDeleteResult addUser(User user);

    /**
     * Поиск по логину
     *
     * @param login - логин пользователя, которого надо найти
     * @return пользователь или null
     */
    User findUserByLogin(String login);

    /**
     * Отфильтровать пользователй по цвету волос
     * @param gender пол, по которому нужно фильтровать
     * @param color цвет волос, по которому нужно фильтровать
     * @return список пользователей с заданными параметрами
     */
    List<User> filterUsers(Gender gender, Colors color);

    AddFindDeleteResult addFriend(String login1, String login2);

    AddFindDeleteResult removeFriend(String login1, String login2);

    List<User> findFriends(String login);
}