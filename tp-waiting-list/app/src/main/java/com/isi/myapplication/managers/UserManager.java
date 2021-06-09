package com.isi.myapplication.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.httprequest.RequestPostObject;
import com.isi.myapplication.httprequest.RequestGetObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserManager {

    /**
     * Recuperer tous les users
     */
    public static User[] getAll() {
        User[] users = null;
        RequestGetObject requestGetObject = new RequestGetObject();
        try {
            String usersString = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/users").get();
            Gson gson = new Gson();
            users = gson.fromJson(usersString, User[].class);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static WaitingUserList employeeAvailable(int id) {
        WaitingUserList userwaiting = null;
        RequestGetObject requestGetObject = new RequestGetObject();
        try {
            String usersString = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/available/" + id).get();
            if (!usersString.equals("null")) {
                Gson gson = new Gson();
                userwaiting = gson.fromJson(usersString, WaitingUserList.class);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userwaiting;
    }


    /**
     * Recuperer user selon id
     */
    public static User getUserByid(int id) {
        User retour = null;
        for (User user : getAll()) {
            if (user.getId() == id) {
                retour = user;
            }
        }
        return retour;
    }

    /**
     * Verifier si user
     */
    public static User checkUser(String email, String password) {
        User userRetour = null;
        for (User user : getAll()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                userRetour = user;
            }
        }
        return userRetour;
    }

    /**
     * ajouter un nouveau user inscription
     */
    public static int addUser(User users) {

        Map<String, String> user = new HashMap<>();
        user.put("firstName", users.getFirstname());
        user.put("email", users.getEmail());
        user.put("password", users.getPassword());
        user.put("level", users.getLevel());
        user.put("phone", users.getPhone());
        user.put("lastName", users.getLastname());
        RequestPostObject task = new RequestPostObject(user);
        String response = "0";
        try {
            response = task.execute("https://waiting-list-garage.herokuapp.com/user").get();
            Log.d("response", "addUser: " + response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(response);
    }

    /**
     * Inscription value saisie verify
     */
    public static String subscribeUserVerify(User user, String messageErreur) {

        if (isFieldEmpty(user) == false) {
            messageErreur = "Please complete all fields";
        }

        if (isEmailUse(user.getEmail()) == true) {
            messageErreur = "Email already in use ";
        }

        if (passwordVerify(user) == false) {
            messageErreur = "Passwords are not identical";
        }

        return messageErreur;
    }

    //autre fonciton pour verifier les champs vide
    public static boolean isFieldEmpty(User user) {
        boolean saisie = true;
        if (user.getFirstname().equals("") || user.getLastname().equals("") || user.getEmail().equals("") || user.getPhone().equals("") || user.getPassword().equals("") ||
            user.getPassword2nd().equals("")) {
            saisie = false;
        }

        return saisie;
    }

    //email deja utilise
    public static boolean isEmailUse(String email) {
        boolean emailUse = false;
        for (User user : getAll()) {
            if (user.getEmail().equals(email)) {
                emailUse = true;
            }
        }
        return emailUse;
    }

    /**
     * Validation des password
     */
    public static boolean passwordVerify(User user) {
        boolean mdp = false;
        if (user.getPassword().equals(user.getPassword2nd())) {
            mdp = true;
        }
        return mdp;

    }

}
