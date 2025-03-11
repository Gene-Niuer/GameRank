import java.util.*;

class PlayerScore implements Comparable<PlayerScore> {
    private final String playerId;
    private final int score;
    private final long timestamp;

    public PlayerScore(String playerId, int score, long timestamp) {
        this.playerId = playerId;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getPlayerId() { return playerId; }
    public int getScore() { return score; }
    public long getTimestamp() { return timestamp; }

    @Override
    public int compareTo(PlayerScore other) {
        if (this.score != other.score) {
            // 分数降序
            return Integer.compare(other.score, this.score);
        } else if (this.timestamp != other.timestamp) {
            // 时间戳升序
            return Long.compare(this.timestamp, other.timestamp);
        } else {
            // PlayerId升序
            return this.playerId.compareTo(other.playerId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerScore that = (PlayerScore) o;
        return playerId.equals(that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }

    // 测试输出接口
    public String print() {
        return "Player: " + playerId + " Score: " + score + " Timestamp: " + timestamp;
    }
}