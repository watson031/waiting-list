package com.isi.myapplication.managers;
import com.google.gson.Gson;
import com.isi.myapplication.entities.User;
import com.isi.myapplication.entities.WaitingUserList;
import com.isi.myapplication.httprequest.RequestGetObject;

import java.util.concurrent.ExecutionException;
public class WaitingUserListManager {
    /**
     * check if user is in WaitingList
     * */
    public static WaitingUserList getUserInfoWaitingList(int idUser){
        WaitingUserList waitingUserList = null;
        RequestGetObject requestGetObject = new RequestGetObject();
        try {
            String stringUserWaitingInfo = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/waitingList/"+idUser).get();
            if(!stringUserWaitingInfo.equals("null")){
                Gson gson = new Gson();
                waitingUserList = gson.fromJson(stringUserWaitingInfo,WaitingUserList[].class)[0];
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return waitingUserList;
    }

    /**
     * Ajouter un  utilisateur dans le waiting list en recuperant ses infos
     * */
    public static WaitingUserList addUserToWaitingList(int idUser){
        WaitingUserList waitingUserList = null;
        RequestGetObject requestGetObject  = new RequestGetObject();
        try {
            String stringUserWaitingInfo = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/addToWaitingList/"+idUser).get();
            Gson gson = new Gson() ;
            waitingUserList = gson.fromJson(stringUserWaitingInfo,WaitingUserList[].class)[0];
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return waitingUserList;
    }
    /**
     * supprimer un userDans la liste
     * */
    public static boolean deleteFromWaitingList(int idUser){
        RequestGetObject requestGetObject  = new RequestGetObject();
        String stringUserWaitingInfo ="";
        try {
            stringUserWaitingInfo = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/deleteToWaitingList/"+idUser).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stringUserWaitingInfo.equals("null");
    }
    /**
     * Mettre a jour la file d'attente
     * */
    public  static WaitingUserList updateWaitingList(int idUser){
        WaitingUserList waitingUserList = null;
        RequestGetObject requestGetObject = new RequestGetObject();
        String userString = "";
        try {
            userString = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/waitingList/"+idUser).get();
            if(!userString.equals("null")){
                Gson gson = new Gson();
                waitingUserList = gson.fromJson(userString, WaitingUserList[].class)[0];
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return waitingUserList;
    }
    /**
     *Recuperer le nombre de personne dans la liste d'attente
     * */
    public static int getNumberWaitingUser(){
        int number = 1;
        RequestGetObject requestGetObject = new RequestGetObject();
        try {
            String stringUserWaitingInfo = requestGetObject.execute("https://waiting-list-garage.herokuapp.com/waitingList").get();

                Gson gson = new Gson();
                WaitingUserList[] waitingUserList = gson.fromJson(stringUserWaitingInfo,WaitingUserList[].class);
                number = waitingUserList.length;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return number;
    }
}
