import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateStatements {
    public static void main(String[] args) throws Exception {

        String topAlertPart1 = 
"<div id=\"statement_back\" class=\"statement_back\" style=\"display: none\"></div>\n" +
"<div class=\"statement-body\">\n" +
"    <!-- LEAGUE ALERT -->\n" +
"    <div style=\"color:#7cc576;background-color:rgba( 124 , 197 , 118 , 0.1 );padding:20px;margin-right:15px;margin-left:15px;margin-bottom:10px;text-align:left\">\n" +
"        <div style=\"text-align:center;margin-bottom:6px\">\n" +
"            <img src=\"//cdn.codingame.com/smash-the-code/statement/league_wood_04.png\">\n" +
"        </div>\n" +
"        <p style=\"text-align:center;font-weight:700;margin-bottom:6px\">\n" +
"            This is a <b>league based</b> challenge.\n" +
"        </p>\n" +
"        <div class=\"statement-league-alert-content\">\n" +
"            For this challenge, multiple leagues for the same game are available. Once you have proven your worth against the Boss, you will access the higher league.<br>\n" +
"            <br>\n";

        String topAlertPart2 = 
"        </div>\n" +
"    </div>\n\n";

        String goalEn = 
"    <div class=\"statement-section statement-goal\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Goal</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Be the first player to form a line of 5 of your marbles either horizontally, vertically, or diagonally.<br>\n" +
"                This puzzle is based on the board game <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n\n";

        String vicDefEn = 
"      <br />\n" +
"      <div class=\"statement-victory-conditions\">\n" +
"        <div class=\"icon victory\"></div>\n" +
"        <div class=\"blk\">\n" +
"          <div class=\"title\">Victory Conditions</div>\n" +
"          <div class=\"text\">\n" +
"          <ul style=\"padding-bottom: 0;\">\n" +
"            <li>Form a line of 5 marbles of your color horizontally, vertically, or diagonally.</li>\n" +
"          </ul>\n" +
"          </div>\n" +
"        </div>\n" +
"      </div>\n" +
"      <div class=\"statement-lose-conditions\">\n" +
"        <div class=\"icon lose\"></div>\n" +
"        <div class=\"blk\">\n" +
"          <div class=\"title\">Defeat Conditions</div>\n" +
"          <div class=\"text\">\n" +
"          <ul style=\"padding-bottom: 0;\">\n" +
"            <li>Your program times out, crashes, or outputs an invalid command.</li>\n" +
"          </ul>\n" +
"          </div>\n" +
"        </div>\n" +
"      </div>\n" +
"      <br />\n";

        String expertRules = 
"    <div class=\"statement-section statement-expertrules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-expertrules\">&nbsp;</span>\n" +
"            <span>Technical Details</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-expert-rules-content\">\n" +
"            <ul style=\"padding-left: 20px; padding-bottom: 0\">\n" +
"                <li>You can inspect the Referee source code on <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">GitHub</a>.</li>\n" +
"            </ul>\n" +
"        </div>\n" +
"    </div>\n";

        String initInputEn = 
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Initialization Input</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>playerCount</var>, the number of players in the game.</p>\n" +
"                <p><span class=\"statement-lineno\">Line 2: </span><var>myId</var>, your player ID (<const>0</const> to <const>3</const>).</p>\n" +
"            </div>\n" +
"        </div>\n";

        String constraintsEn = 
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Constraints</div>\n" +
"            <div class=\"text\">\n" +
"                Response time for first turn ≤ <const>1000</const> ms<br>\n" +
"                Response time for one turn ≤ <const>50</const> ms<br>\n" +
"            </div>\n" +
"        </div>\n";

        String baseL1 = 
topAlertPart1 +
"            <b>Wood 4 League:</b> Welcome! The game is simplified for you to learn the basics. You play in a 1v1 duel on a 6x6 board.\n" +
topAlertPart2 + goalEn +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the board.</li>\n" +
"                <li><strong>Rotate a 3x3 block</strong> 90 degrees either left (counter-clockwise) or right (clockwise).</li>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>3</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>5</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level1/coords_6x6.png\" style=\"width: 100%; max-width: 400px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the board is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
vicDefEn + expertRules +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h2>\n" +
initInputEn +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>6</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the board state. A <const>.</const> means empty. A number <const>0</const>-<const>1</const> represents a player's marble.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Output for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">A single line containing 4 space-separated values: </span>\n" +
"                <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var>: Coordinates to place your marble.\n" +
"                <br>\n" +
"                <var>block</var>: The ID of the block you wish to rotate (<const>0</const> to <const>3</const>).\n" +
"                <br>\n" +
"                <var>dir</var>: The direction to rotate (<action>L</action> or <action>R</action>).\n" +
"                <br><br>\n" +
"                <strong>Example:</strong> <code>2 1 0 R</code> (Places marble at 2,1 and rotates block 0 right)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsEn +
"    </div>\n" +
"    <div style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-top: 10px; text-align: left;\">\n" +
"        <div style=\"text-align: center; margin-bottom: 6px\"><img src=\"//cdn.codingame.com/smash-the-code/statement/league_wood_04.png\" /></div>\n" +
"        <div style=\"text-align: center; font-weight: 700; margin-bottom: 6px;\">\n" +
"            What is in store for me in the higher leagues?\n" +
"        </div>\n" +
"        <ul>\n" +
"            <li>Play on a massive 9x9 board with up to 3 opponents simultaneously.</li>\n" +
"            <li>Unlock the SWAP mechanic to exchange the positions of two 3x3 blocks.</li>\n" +
"        </ul>\n" +
"    </div>\n" +
"</div>";

        String baseL2 = 
topAlertPart1 +
"            <b>Wood 3 League:</b> The training wheels are off. The board has expanded to a massive 9x9 board, and you are now facing up to 3 opponents simultaneously in a free-for-all!\n" +
topAlertPart2 + goalEn +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the board.</li>\n" +
"                <li><strong>Rotate a 3x3 block</strong> 90 degrees either left (counter-clockwise) or right (clockwise).</li>\n" +
"            </ol>\n" +
"            <div style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;\">\n" +
"                <p>Blocks are numbered <const>0</const> to <const>8</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>8</const> as shown below:</p>\n" +
"            </div>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level2/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the board is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
vicDefEn + expertRules +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h2>\n" +
initInputEn +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>9</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the board state. A <const>.</const> means empty. A number <const>0</const>-<const>3</const> represents a player's marble.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Output for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">A single line containing 4 space-separated values: </span>\n" +
"                <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var>: Coordinates to place your marble.\n" +
"                <br>\n" +
"                <var>block</var>: The ID of the block you wish to rotate (<const>0</const> to <const>8</const>).\n" +
"                <br>\n" +
"                <var>dir</var>: The direction to rotate (<action>L</action> or <action>R</action>).\n" +
"                <br><br>\n" +
"                <strong>Example:</strong> <code>2 1 0 R</code> (Places marble at 2,1 and rotates block 0 right)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsEn +
"    </div>\n" +
"    <div style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-top: 10px; text-align: left;\">\n" +
"        <div style=\"text-align: center; margin-bottom: 6px\"><img src=\"//cdn.codingame.com/smash-the-code/statement/league_wood_04.png\" /></div>\n" +
"        <div style=\"text-align: center; font-weight: 700; margin-bottom: 6px;\">\n" +
"            What is in store for me in the higher leagues?\n" +
"        </div>\n" +
"        <ul>\n" +
"            <li>Unlock the SWAP mechanic to exchange the positions of two 3x3 blocks.</li>\n" +
"        </ul>\n" +
"    </div>\n" +
"</div>";

        String baseL3 = 
topAlertPart1 +
"            <b>Wood 2 League:</b> The ultimate mechanic has been unlocked: <action>SWAP</action>. You can now exchange the positions of two blocks instead of rotating them!\n" +
topAlertPart2 + goalEn +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the board.</li>\n" +
"                <li><strong>Manipulate the board</strong> using ONE of the following methods:</li>\n" +
"                <ul>\n" +
"                    <li><strong>Rotate</strong> a 3x3 block 90 degrees either left (counter-clockwise) or right (clockwise).</li>\n" +
"                    <li style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;\"><strong>Swap</strong> the positions of two adjacent 3x3 blocks (blocks sharing an edge).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>8</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>8</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the board is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
vicDefEn + expertRules +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h2>\n" +
initInputEn +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>9</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the board state. A <const>.</const> means empty. A number <const>0</const>-<const>3</const> represents a player's marble.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Output for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotate): A single line containing 4 space-separated values: </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var>: Coordinates to place your marble.<br>\n" +
"                <var>block</var>: The ID of the block you wish to rotate (<const>0</const> to <const>8</const>).<br>\n" +
"                <var>dir</var>: The direction to rotate (<action>L</action> or <action>R</action>).\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 0 R</code> (Places marble at 2,1 and rotates block 0 right)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Swap): A single line containing 5 space-separated values: </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var>: Coordinates to place your marble.<br>\n" +
"                <var>b1</var> and <var>b2</var>: The IDs of the two adjacent blocks you wish to swap.\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 SWAP 0 1</code> (Places marble at 2,1 and swaps blocks 0 and 1)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsEn +
"    </div>\n" +
"</div>";

        String baseL4 = 
topAlertPart1 +
"            <b>Wood 1 League:</b> The rules remain identical, but your opponents (and the Boss) will be much stronger. Good luck!\n" +
topAlertPart2 + goalEn +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the board.</li>\n" +
"                <li><strong>Manipulate the board</strong> using ONE of the following methods:</li>\n" +
"                <ul>\n" +
"                    <li><strong>Rotate</strong> a 3x3 block 90 degrees either left (counter-clockwise) or right (clockwise).</li>\n" +
"                    <li><strong>Swap</strong> the positions of two adjacent 3x3 blocks (blocks sharing an edge).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>8</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>8</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the board is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
vicDefEn + expertRules +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h2>\n" +
initInputEn +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>9</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the board state. A <const>.</const> means empty. A number <const>0</const>-<const>3</const> represents a player's marble.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Output for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotate): A single line containing 4 space-separated values: </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var>: Coordinates to place your marble.<br>\n" +
"                <var>block</var>: The ID of the block you wish to rotate (<const>0</const> to <const>8</const>).<br>\n" +
"                <var>dir</var>: The direction to rotate (<action>L</action> or <action>R</action>).\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 0 R</code> (Places marble at 2,1 and rotates block 0 right)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Swap): A single line containing 5 space-separated values: </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var>: Coordinates to place your marble.<br>\n" +
"                <var>b1</var> and <var>b2</var>: The IDs of the two adjacent blocks you wish to swap.\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 SWAP 0 1</code> (Places marble at 2,1 and swaps blocks 0 and 1)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsEn +
"    </div>\n" +
"</div>";

        Files.write(Paths.get("config/level1/statement_en.html"), baseL1.getBytes("UTF-8"));
        Files.write(Paths.get("config/level2/statement_en.html"), baseL2.getBytes("UTF-8"));
        Files.write(Paths.get("config/level3/statement_en.html"), baseL3.getBytes("UTF-8"));
        Files.write(Paths.get("config/level4/statement_en.html"), baseL4.getBytes("UTF-8"));
        System.out.println("Done updating statements.");
    }
}
