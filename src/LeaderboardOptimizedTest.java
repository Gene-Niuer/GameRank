import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LeaderboardOptimizedTest {
    private LeaderboardOptimized leaderboard;

    @BeforeEach
    void setUp() {
        leaderboard = new LeaderboardOptimized();
    }

    //---------------------- updateScore() 测试 ----------------------
    @Test
    void updateScore_shouldAddNewPlayer() {
        leaderboard.updateScore("player1", 100, 1);
        assertEquals(1, leaderboard.getPlayerRank("player1"));
    }

    @Test
    void updateScore_shouldUpdateExistingPlayer() {
        leaderboard.updateScore("player1", 100, 1);
        leaderboard.updateScore("player1", 200, 2);
        assertEquals(1, leaderboard.getPlayerRank("player1"));
    }

    @Test
    void updateScore_shouldHandleSameScoreWithDifferentTimestamp() {
        leaderboard.updateScore("playerA", 100, 1); // 时间早，排名更高
        leaderboard.updateScore("playerB", 100, 2);
        assertEquals(1, leaderboard.getPlayerRank("playerA"));
        assertEquals(2, leaderboard.getPlayerRank("playerB"));
    }

    //---------------------- getPlayerRank() 测试 ----------------------
    @Test
    void getPlayerRank_shouldReturnMinusOneForNonExistingPlayer() {
        assertEquals(-1, leaderboard.getPlayerRank("ghost"));
    }

    @Test
    void getPlayerRank_shouldReturnCorrectRankAfterUpdates() {
        leaderboard.updateScore("player1", 100, 1);
        leaderboard.updateScore("player2", 200, 2);
        assertEquals(2, leaderboard.getPlayerRank("player1"));
        assertEquals(1, leaderboard.getPlayerRank("player2"));
    }

    //---------------------- getTopN() 测试 ----------------------
    @Test
    void getTopN_shouldReturnEmptyListForZeroOrNegativeN() {
        assertTrue(leaderboard.getTopN(0).isEmpty());
        assertTrue(leaderboard.getTopN(-1).isEmpty());
    }

    @Test
    void getTopN_shouldReturnAllPlayersWhenNExceedsSize() {
        leaderboard.updateScore("player1", 100, 1);
        leaderboard.updateScore("player2", 200, 2);
        List<RankInfo> top = leaderboard.getTopN(5);
        assertEquals(2, top.size());
        assertEquals("player2", top.get(0).playerId);
    }

    @Test
    void getTopN_shouldMaintainCorrectOrder() {
        leaderboard.updateScore("playerA", 100, 1);
        leaderboard.updateScore("playerB", 100, 2); // 同分但时间晚，排名更低
        leaderboard.updateScore("playerC", 200, 3);
        List<RankInfo> top = leaderboard.getTopN(3);
        assertEquals("playerC", top.get(0).playerId);
        assertEquals("playerA", top.get(1).playerId);
        assertEquals("playerB", top.get(2).playerId);
    }

    //---------------------- getPlayerRankRange() 测试 ----------------------
    @Test
    void getPlayerRankRange_shouldHandlePlayerNotExists() {
        assertTrue(leaderboard.getPlayerRankRange("ghost", 2).isEmpty());
    }

    @Test
    void getPlayerRankRange_shouldClampToBoundaries() {
        // 填充10个玩家，分数递减
        for (int i = 0; i < 10; i++) {
            leaderboard.updateScore("player" + i, 100 - i, i);
        }
        // 测试中间玩家（排名5）的周边范围
        List<RankInfo> range = leaderboard.getPlayerRankRange("player4", 2);
        assertEquals(5, range.size()); // 排名3-7
        assertEquals(3, range.get(0).rank);
        assertEquals(7, range.get(4).rank);
    }

    @Test
    void getPlayerRankRange_shouldReturnFullRangeIfPossible() {
        leaderboard.updateScore("player1", 300, 1);
        leaderboard.updateScore("player2", 200, 2);
        leaderboard.updateScore("player3", 100, 3);
        List<RankInfo> range = leaderboard.getPlayerRankRange("player2", 1);
        assertEquals(3, range.size()); // 排名1-3
        assertEquals("player1", range.get(0).playerId);
        assertEquals("player2", range.get(1).playerId);
        assertEquals("player3", range.get(2).playerId);
    }
}