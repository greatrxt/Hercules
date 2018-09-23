package com.xenodochium.hercules.engine;

public interface RoutineOrchestrator {
    void previous();

    void next();

    void pause();

    void play();

    boolean isPlaying();
}
