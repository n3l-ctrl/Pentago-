import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        // Uncomment this section and comment the other one to create a Solo Game
        /* Solo Game */
        // SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets the player
        // gameRunner.setAgent(Player1.class);

        // Sets a test case
        // gameRunner.setTestCase("test1.json");

        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        java.util.Properties params = new java.util.Properties();
        params.setProperty("board_size", "6");
        params.setProperty("swaps_allowed", "false");
        gameRunner.setGameParameters(params);
        gameRunner.addAgent(Boss2.class);
        gameRunner.addAgent(Boss2.class);

        // Another way to add a player
        // gameRunner.addAgent("python3 /home/user/player.py");
        

        gameRunner.start();
    }
}
