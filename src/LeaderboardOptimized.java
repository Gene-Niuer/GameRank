import java.util.*;
import java.util.concurrent.*;

public class LeaderboardOptimized {
    // 占位符对象
    private static final Object DUMMY = new Object();
    private final ConcurrentSkipListMap<PlayerScore, Object> skipList =
            new ConcurrentSkipListMap<>();
    private final ConcurrentHashMap<String, PlayerScore> playerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<PlayerScore, Integer> rankCache = new ConcurrentHashMap<>();

    // 更新玩家分数
    public void updateScore(String playerId, int score, long timestamp) {
        PlayerScore old = playerMap.get(playerId);
        PlayerScore newScore = new PlayerScore(playerId, score, timestamp);

        synchronized (this) {
            if (old != null) {
                skipList.remove(old);
                rankCache.remove(old);
            }
            skipList.put(newScore, DUMMY);
            playerMap.put(playerId, newScore);
            recalculateRanks(); // 同步更新缓存
        }
    }

    // 获取玩家当前排名（接口完整覆盖）
    public int getPlayerRank(String playerId) {
        PlayerScore score = playerMap.get(playerId);
        return score != null ? rankCache.getOrDefault(score, -1) : -1;
    }

    // 获取前N名玩家
    public List<RankInfo> getTopN(int n) {
        List<RankInfo> result = new ArrayList<>();
        Iterator<PlayerScore> iterator = skipList.keySet().iterator();
        for (int i = 0; i < n && iterator.hasNext(); i++) {
            PlayerScore ps = iterator.next();
            result.add(new RankInfo(ps.getPlayerId(), rankCache.get(ps), ps.getScore()));
        }
        return result;
    }

    // 获取玩家周边排名
    public List<RankInfo> getPlayerRankRange(String playerId, int range) {
        PlayerScore target = playerMap.get(playerId);
        if (target == null)
            return Collections.emptyList();

        int targetRank = rankCache.get(target);
        int start = Math.max(1, targetRank - range);
        int end = targetRank + range;

        List<RankInfo> result = new ArrayList<>();
        int currentRank = 0;
        for (PlayerScore ps : skipList.keySet()) {
            currentRank++;
            if (currentRank > end) break;
            if (currentRank >= start) {
                result.add(new RankInfo(ps.getPlayerId(), currentRank, ps.getScore()));
            }
        }
        return result;
    }

    // 异步预计算排名缓存
    private void recalculateRanks() {
        int rank = 0;
        for (PlayerScore ps : skipList.keySet()) {
            rankCache.put(ps, ++rank);
        }
    }
}