package org.thehellnet.lanparty.manager.utility.logline.impl;

import org.thehellnet.lanparty.manager.exception.logline.LogLineParserException;
import org.thehellnet.lanparty.manager.utility.logline.AbstractLogLineParser;
import org.thehellnet.lanparty.manager.utility.logline.line.LogLine;
import org.thehellnet.utility.StringUtility;

import java.util.List;

public class COD4LogLineParser extends AbstractLogLineParser {

    public COD4LogLineParser(String line) {
        super(line);
    }

    @Override
    protected LogLine parseLine() {
        List<String> mainItems = StringUtility.splitSpaces(line);
        if (mainItems.size() < 2) {
            throw new LogLineParserException("Line not valid");
        }

        String time = mainItems.get(0);
        int lineTime = parseTime(time);

        String data = StringUtility.joinSpaces(mainItems.subList(1, mainItems.size()));
        String[] items = splitItems(data);

//        TODO: Implementare parsing con linea

        return null;
    }

}
