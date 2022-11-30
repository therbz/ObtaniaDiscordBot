package me.therbz.obtaniadiscordbot.suggestions;

import com.google.gson.annotations.Expose;

public class Suggestion {
    @Expose
    private final int id;
    @Expose
    private final String messageId;
    @Expose
    private final String authorId;
    @Expose
    private String content;
    @Expose
    private SuggestionStatus suggestionStatus;

    public Suggestion(int id, String messageId, String authorId, String content, SuggestionStatus suggestionStatus) {
        this.id = id;
        this.messageId = messageId;
        this.authorId = authorId;
        this.content = content;
        this.suggestionStatus = suggestionStatus;
    }

    public int getId() {
        return this.id;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    public SuggestionStatus getSuggestionStatus() {
        return this.suggestionStatus;
    }

    public void setSuggestionStatus(SuggestionStatus newSuggestionStatus) {
        this.suggestionStatus = newSuggestionStatus;
    }
}
