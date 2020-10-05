package app.os.utilities;

public enum  Emoji {
    INTERNET("\uD83C\uDF10"),
    THREADS("\uD83E\uDDF5"),
    RED_SQUARE("\uD83D\uDFE5"),
    TIME("⌚"),
    GRAPH("\uD83D\uDCCA");

    private final String emoji;

    Emoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji + " ";
    }

    public String getEmojiWithoutSpace() {
        return emoji;
    }
}
