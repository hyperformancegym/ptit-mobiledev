package com.ptithcm.lexigo.models;

import java.util.List;

/**
 * Model class cho từ điển
 * Khớp với API response từ /api/v1/dictionary/lookup
 */
public class DictionaryEntry {
    private String _id;  // MongoDB ObjectId
    private String word;
    private String phonetic;
    private String partOfSpeech;
    private List<String> definitions;
    private List<String> examples;
    private List<String> synonyms;
    private String translation; // Bản dịch tiếng Việt/Anh
    private String direction;   // "en-vi" hoặc "vi-en"
    private String audio_url;   // Presigned URL to MP3 pronunciation (expires in 1 hour)

    public DictionaryEntry() {
    }

    public DictionaryEntry(String word, String phonetic, String partOfSpeech,
                          List<String> definitions, List<String> examples,
                          List<String> synonyms, String translation, String direction,
                          String audio_url) {
        this.word = word;
        this.phonetic = phonetic;
        this.partOfSpeech = partOfSpeech;
        this.definitions = definitions;
        this.examples = examples;
        this.synonyms = synonyms;
        this.translation = translation;
        this.direction = direction;
        this.audio_url = audio_url;
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAudioUrl() {
        return audio_url;
    }

    public void setAudioUrl(String audio_url) {
        this.audio_url = audio_url;
    }
}

