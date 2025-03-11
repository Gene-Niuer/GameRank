import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 手动测试
        System.out.println("--------------- Test Start ---------------");
        LeaderboardOptimized leaderboardOptimized = new LeaderboardOptimized();

        leaderboardOptimized.updateScore("three", 20, 1741598369);
        leaderboardOptimized.updateScore("two", 21, 1741598350);
        leaderboardOptimized.updateScore("one", 21, 1741598350);
        leaderboardOptimized.updateScore("four", 90, 1741598369);

        System.out.println("--------------- Insert End ---------------");

        System.out.println("One Rank is " + leaderboardOptimized.getPlayerRank("one"));
        System.out.println("Two Rank is " + leaderboardOptimized.getPlayerRank("two"));
        System.out.println("Three Rank is " + leaderboardOptimized.getPlayerRank("three"));
        System.out.println("Four Rank is " + leaderboardOptimized.getPlayerRank("four"));

        System.out.println("--------------- Print Rank End ---------------");

        List<RankInfo> topN = leaderboardOptimized.getTopN(2);
        for (RankInfo rankInfo : topN) {
            System.out.println(rankInfo.playerId + ": " + rankInfo.rank + ": " + rankInfo.score);
        }
    }
}