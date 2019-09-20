package com.pvptowerdefense.server.users.services;

import com.pvptowerdefense.server.cards.CardsController;
import com.pvptowerdefense.server.cards.models.Card;
import com.pvptowerdefense.server.users.dao.UsersDao;
import com.pvptowerdefense.server.users.models.Deck;
import com.pvptowerdefense.server.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Service
public class UsersService {
    private UsersDao usersDao;

    @Autowired
    public UsersService(UsersDao usersDao){
        this.usersDao = usersDao;
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        usersDao.findAll().forEach(users::add);

        return users;
    }

    public void loadPresetUsersToDatabase(){
        List<User> users = Arrays.asList(
                new User("user1", "phoneId1", "firstName1", "lastName1", "email1", 1, 1, "User", Arrays.asList(new Card()), Arrays.asList(new Deck())),
                new User("user2", "phoneId2", "firstName2", "lastName2", "email2", 2, 2, "User", Arrays.asList(new Card()), Arrays.asList(new Deck())),
                new User("user3", "phoneId3", "firstName3", "lastName3", "email3", 3, 3, "User", Arrays.asList(new Card()), Arrays.asList(new Deck())),
                new User("user4", "phoneId4", "firstName4", "lastName4", "email4", 4, 4, "User", Arrays.asList(new Card()), Arrays.asList(new Deck()))
        );

        usersDao.saveAll(users);
    }

    public User findUserById(String phoneId){
        return usersDao.findUserByPhoneId(phoneId);
    }

    public void deleteUserById(String id){
        usersDao.deleteById(id);
    }

    public void addUserToDb(User id){
        usersDao.save(id);
    }

    public void addEmptyDeck(List<Card> deck, String deckName){
        new Deck(deck, deckName);
    }

}
