package com.eikholm.websocketprep.controller;

import com.eikholm.websocketprep.model.Greeting;
import com.eikholm.websocketprep.model.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class GreetingController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Set<String> connectedUsers = new HashSet<>();
    public char[] answer = new char[3];
//    public int totalMistakes = 0;

    public GreetingController(SimpMessagingTemplate s) {
        simpMessagingTemplate = s;
    }

    @MessageMapping("/register")
    public void registerUser(String userName){
        System.out.println("register called with "+ userName);
        if(!connectedUsers.contains(userName)){ // only allow unique usernames
            String word = getRandomWord();
            answer[0] = word.charAt(0);
            answer[1] = word.charAt(1);
            answer[2] = word.charAt(2);
            connectedUsers.add(userName);
        }
    }

    @MessageMapping("/hello")
    public void processMessageFromClient(WebSocketMessage message){
        System.out.println("controller kaldet til: " + message.toWhom + " fra: " + message.fromWho + " message: " + message.message );
        Greeting greeting = new Greeting();
        int userGuess = find(answer, message.message.charAt(0));
        System.out.println("User guess index: " + userGuess);
        if (userGuess >= 0) {
            userGuess++;
            greeting.setContent("Yay! " + message.message + " is at: " + userGuess + " was guessed correctly by: "+ message.fromWho);
        } else {
            greeting.setContent("What?! " + message.message + " was wrongly guessed by: " + message.fromWho);
//            greeting.setLivesLeft("__".repeat(8-totalMistakes));
//            totalMistakes++;
        }

        connectedUsers.forEach(user -> {
            if (message.toWhom.equals("all")) {
                simpMessagingTemplate.convertAndSendToUser(user, "/msg", greeting); // genial metode !
            } else if (user.equals(message.toWhom) || user.equals(message.fromWho)) {
                simpMessagingTemplate.convertAndSendToUser(user, "/msg", greeting); // genial metode !
            }
        });
    }

    public String getRandomWord() {
        ArrayList<String> wordAnswers = new ArrayList<>();
        wordAnswers.add("CAT");
        wordAnswers.add("DOG");
        wordAnswers.add("COW");
        wordAnswers.add("FLY");
        wordAnswers.add("BUG");
        wordAnswers.add("PIE");
        Random r = new Random();
        return wordAnswers.get(r.nextInt(wordAnswers.size()));
    }

    public static int find(char[] a, char target) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == target) {
                return i;
            }
        return -1;
    }
}