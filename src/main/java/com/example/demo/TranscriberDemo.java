package com.example.demo;


import java.io.IOException;
import java.net.URL;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

public class TranscriberDemo {

    private LiveSpeechRecognizer recognizer;

    public static void main(String[] args) throws Exception {


        Configuration configuration = new Configuration();

        URL modelBaseURL = TranscriberDemo.class.getResource("/edu/cmu/sphinx/models/en-us/");
        String modelBasePath = modelBaseURL.toExternalForm();

        configuration.setAcousticModelPath(modelBasePath + "en-us");
        configuration.setDictionaryPath(modelBasePath + "cmudict-en-us.dict");
        configuration.setLanguageModelPath(modelBasePath + "en-us.lm.bin");

        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

        recognizer.startRecognition(true);
        SpeechResult rawResult = recognizer.getResult();

        if (rawResult != null ) {
            String hypothesis = rawResult.getHypothesis();
            if (hypothesis.contains("show")) {
                // Execute the desired action when "Show" is recognized.
                System.out.println("Show recognized!");
            }
            System.out.println("Recognized: " + hypothesis);
            recognizer.stopRecognition();
        }
    }

    public void startVoiceRecognition(gameScreen mineSweeperGame) {
        Configuration configuration = new Configuration();

        URL modelBaseURL = TranscriberDemo.class.getResource("/edu/cmu/sphinx/models/en-us/");
        String modelBasePath = modelBaseURL.toExternalForm();

        configuration.setAcousticModelPath(modelBasePath + "en-us");
        configuration.setDictionaryPath(modelBasePath + "cmudict-en-us.dict");
        configuration.setLanguageModelPath(modelBasePath + "en-us.lm.bin");

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
            recognizer.startRecognition(true);

            while (true) {
                SpeechResult rawResult = recognizer.getResult();

                if (rawResult != null) {
                    String hypothesis = rawResult.getHypothesis();
                    System.out.println("Recognized: " + hypothesis);

                    // Check if the recognized text contains "show"
                    if (hypothesis.toLowerCase().contains("show")) {
                        // Execute the desired action in MineSweeperGame
                        mineSweeperGame.revealBombsFor5Seconds();
                        break; // Exit the loop
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopVoiceRecognition(); // Ensure that recognition is stopped
        }
    }

    public void stopVoiceRecognition() {
        if (recognizer != null) {
            recognizer.stopRecognition();
        }
    }

}

