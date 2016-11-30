package com.example.shengdong.hw;

import android.util.Log;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

public class PercussionDetector implements OnsetHandler {
    private final static int sampleRate = 44100;
    private final static int bufferSize = 4096;
    private final static int overlap = 0;
    private ArrayList<Double> beatsList = new ArrayList<>();
    public PercussionDetector(String musicPath) {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(musicPath, sampleRate, bufferSize, overlap);

        ComplexOnsetDetector b = new ComplexOnsetDetector(bufferSize);
        b.setHandler(this);
        dispatcher.addAudioProcessor(b);
        // run the dispatcher (on a new thread).
        Thread audioAnalysis = new Thread(dispatcher,"Audio dispatching");
        audioAnalysis.start();
        try {
            audioAnalysis.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Double> getBeatsList(){
        return beatsList;
    }

    @Override
    public void handleOnset(double time, double salience) {
        //double roundedTime = Math.round(time * 100) / 100.0;
        beatsList.add(time*1000);
    }
}
