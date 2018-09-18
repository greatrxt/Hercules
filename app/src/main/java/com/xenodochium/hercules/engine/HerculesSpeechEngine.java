package com.xenodochium.hercules.engine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;

import com.xenodochium.hercules.application.Hercules;

import java.util.Iterator;
import java.util.Locale;

public class HerculesSpeechEngine {
    private static TextToSpeech tts;
    private static Voice tempVoice;
    private static Thread speechHandler;

    /**
     * Find a male voice. Going for male voice since name of app is Hercules.
     */
    public static void findVoice() {
        speechHandler = new Thread();
        final boolean[] voiceFound = {false};
        tts = new TextToSpeech(Hercules.getInstance(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    Iterator<Voice> voices = tts.getVoices().iterator();
                    //find male voice if exists
                    while (voices.hasNext()) {
                        Voice voice = voices.next();
                        String voiceName = voice.getName().toString();

                        if (voiceName == "en-gb-x-gbd-local") {
                            tempVoice = voice;
                        }

                        try {
                            if ((voiceName.substring(voiceName.split("").length - 6, voiceName.split("").length - 1)).equals("local")) {
                                if ((voiceName.substring(0, 2)).equals("en")) {
                                    if (!voiceName.contains("female")) {
                                        if (voiceName.contains("male")) {
                                            tts.setVoice(voice);
                                            voiceFound[0] = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
        });

        if (!voiceFound[0]) {
            if (tempVoice != null) {
                tts.setVoice(tempVoice);
            }
        }
    }

    /**
     * Run in background thread
     *
     * @param text
     */
    public synchronized static void speak(final String text) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                while (tts.isSpeaking()) {
                    //wait
                }
            }
        };

        AsyncTask.execute(runnable); //running in background so that app does not freeze when while loop for tts.isSpeaking is going on
    }

    /**
     * Pause or stop TTS
     */
    public synchronized static void stopSpeaking() {
        if (tts != null)
            tts.stop();
    }

    /**
     * @param text
     * @param utteranceProgressListener
     */
    public synchronized static void speak(String text, UtteranceProgressListener utteranceProgressListener) {
        tts.setOnUtteranceProgressListener(utteranceProgressListener);
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "UniqueId");
    }
}
