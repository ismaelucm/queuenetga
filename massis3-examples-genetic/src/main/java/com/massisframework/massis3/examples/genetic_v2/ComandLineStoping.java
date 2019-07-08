package com.massisframework.massis3.examples.genetic_v2;

import java.io.BufferedReader;
import java.io.IOException;

public class ComandLineStoping {
    private Thread _backgroundReaderThread = null;


    public ComandLineStoping(final BufferedReader bufferedReader, final String stopCommand, Interruptible interruptible) {
        _backgroundReaderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        } else {
                            if (line.compareToIgnoreCase(stopCommand) == 0) {
                                interruptible.interrupt();
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    close();
                }
            }
        });
        _backgroundReaderThread.setDaemon(true);
        _backgroundReaderThread.start();
    }


    public void close() {
        if (_backgroundReaderThread != null) {
            _backgroundReaderThread.interrupt();
            _backgroundReaderThread = null;
        }
    }
}
