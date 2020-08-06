package org.thehellnet.lanparty.manager.utility.logline;

import org.reflections.Reflections;
import org.thehellnet.lanparty.manager.exception.logline.LogLineParserException;
import org.thehellnet.lanparty.manager.model.persistence.Game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class LogLineParserFactory {

    private LogLineParserFactory() {
    }

    public static LogLineParser getLogLineParser(Game game, String rawLogLine) {
        Reflections reflections = new Reflections("org.thehellnet.lanparty.manager.utility.logline.impl");
        Set<Class<?>> logLineParserClasses = reflections.getTypesAnnotatedWith(LineParserGame.class);

        Constructor<?> constructor = null;

        for (Class<?> clazz : logLineParserClasses) {
            LineParserGame lineParserGame = clazz.getAnnotation(LineParserGame.class);
            if (lineParserGame.gameTag().equals(game.getTag())) {

                try {
                    constructor = clazz.getConstructor(String.class);
                } catch (NoSuchMethodException e) {
                    throw new LogLineParserException(String.format("Log Line Parser for game %s not found", game), e);
                }
            }
        }

        if (constructor == null) {
            throw new LogLineParserException(String.format("Log Line Parser for game %s not found", game));
        }

        LogLineParser logLineParser;

        try {
            logLineParser = (LogLineParser) constructor.newInstance(rawLogLine);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new LogLineParserException("Unable to create instance of Log Line Parser", e);
        }

        return logLineParser;
    }
}
