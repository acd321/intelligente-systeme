package com.nerapplication.core;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class Pipeline {

    private static Properties properties;
    private static String propertiesName = "tokenize, ssplit, pos, lemma, ner";
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline() {

    }

    static {
        // Einrichten von Pipeline-Eigenschaften
        properties = new Properties();
        // setze die Liste der Annotatoren, die ausgeführt werden sollen
        properties.setProperty("annotators", propertiesName);
    }

    @Bean(name = "stanfordCoreNLP")
    public static StanfordCoreNLP getInstance() {
        if(stanfordCoreNLP == null) {
            // Pipeline erstellen
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}