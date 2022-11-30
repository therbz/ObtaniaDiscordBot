package me.therbz.obtaniadiscordbot.suggestions;

import java.awt.*;

public enum SuggestionStatus {
    ONGOING {
        public Color getColor() { return new Color(255, 212, 0); }
    },
    DENIED {
        public Color getColor() { return new Color(138, 38, 32); }
    },
    ACCEPTED {
        public Color getColor() { return new Color(78, 161, 0); }
    },
    DELETED {
        public Color getColor() { return new Color(58, 58, 58); }
    };

    public abstract Color getColor();
}
