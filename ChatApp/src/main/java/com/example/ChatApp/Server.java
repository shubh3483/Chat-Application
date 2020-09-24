package com.example.ChatApp;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {

    private static ArrayList<String> users = new ArrayList<>();
    private  static ArrayList<MultiThreaded> threads = new ArrayList<>();


    public static void main(String[] args) throws IOException, SQLException {

        ServerSocket server = new ServerSocket(9090,10);
        System.out.println("Server is running");
        while (true){
            Socket client = server.accept();
            MultiThreaded thread = new MultiThreaded(client);
            threads.add(thread);
            thread.start();
        }
    }

    static class MultiThreaded extends Thread{

        private BufferedReader input;
        private PrintWriter ouput;
        String user="";

        public MultiThreaded(Socket client) throws IOException, SQLException {

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ouput = new PrintWriter(client.getOutputStream(),true);

            user = input.readLine();
            users.add(user);
            DbOperations.addUser(user);
        }
        public String getUser(){

            return user;
        }

        public void sendMessage(String user,String message){

            ouput.println(user+":"+message);
        }

        public void sendToMe(String user,String message){

            ouput.println("me:"+message);
        }

        public static void sendToAll(String user,String line){
            for(MultiThreaded mt:threads){
                if(!mt.getUser().equals(user)){
                    mt.sendMessage(user,line);
                }
                else{
                    mt.sendToMe(user,line);
                }
            }
        }

        public void saveInDb(String user,String message) throws SQLException {
            DbOperations.sendToDb(user,message);
        }
        @Override
        public void run() {
            String line;
            while (true) {
                try {
                    line = input.readLine();
                    if(line.equals("end")){
                        users.remove(user);
                        threads.remove(this);
                        break;
                    }
                    else{
                        sendToAll(user,line);
                        saveInDb(user,line);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
