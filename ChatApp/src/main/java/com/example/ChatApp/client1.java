package com.example.ChatApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client1 extends JFrame implements ActionListener {

    String username;
    BufferedReader input;
    PrintWriter output;
    JTextField jf;
    JTextArea ja;
    Socket socket;
    JButton send,exit;


    public client1(String username, String server) throws IOException {
        super(username);
        this.username = username;
        socket = new Socket(server,9090);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(),true);
        output.println(username);
        buildInterface();
        new MessagingThread().start();
    }

    void buildInterface(){

        send = new JButton("Send");
        exit = new JButton("Exit");
        ja = new JTextArea();
        ja.setRows(30);
        ja.setColumns(50);
        ja.setEditable(false);
        jf = new JTextField(50);
        JScrollPane sp = new JScrollPane(ja,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel jp =new JPanel(new FlowLayout());
        jp.add(jf);
        jp.add(send);
        jp.add(exit);
        jp.setBackground(Color.LIGHT_GRAY);
        jp.setName("Instant Messenger");
        add(jp,"North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();


    }

    class MessagingThread extends Thread{

        @Override
        public void run(){
            String line;
            while (true){
                try {
                    line = input.readLine();
                    ja.append(line+"\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static void main(String[] args) throws IOException {
        String username = JOptionPane.showInputDialog(null,"enter your name to continue","Name Input",JOptionPane.PLAIN_MESSAGE);
        String server = "localhost";
        new client1(username,server);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exit){
            output.println("end");
            System.exit(0);
        }else{
            output.println(jf.getText());
            jf.setText(null);
        }
    }
}
