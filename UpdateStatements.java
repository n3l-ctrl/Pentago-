import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateStatements {
    public static void main(String[] args) throws Exception {
        String ribbonStyle = "color: #333; background-color: #ffeb3b; padding: 15px; margin-bottom: 30px; border-radius: 5px; border-left: 8px solid #f57f17; font-size: 1.1em; box-shadow: 0 4px 6px rgba(0,0,0,0.3);";
        String darkRibbonStyle = "background-color: rgba(255, 193, 7, 0.15); padding: 15px; margin-bottom: 30px; border-radius: 5px; border-left: 8px solid #ffc107; font-size: 1.1em;";

        String baseL1 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 This is a multi-league puzzle!</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        Welcome to the <strong>Wood League</strong>! The game is simplified for you to learn the basics.<br>\n" +
"        You play in a 1v1 duel on a 6x6 grid.\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Goal</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Be the first player to form a line of 5 of your marbles either horizontally, vertically, or diagonally.<br>\n" +
"                This puzzle is based on the official board game <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the grid.</li>\n" +
"                <li><strong>Rotate a 3x3 block</strong> 90 degrees either left (counter-clockwise) or right (clockwise).</li>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>3</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>5</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level1/coords_6x6.png\" style=\"width: 100%; max-width: 400px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the grid is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>6</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the grid state. A <code>.</code> means empty. A number <const>0</const>-<const>1</const> represents a player's marble.</p>\n" +
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
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        If you want to understand all the mechanics in detail, you can find the <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">Referee source code here</a>.\n" +
"    </div>\n" +
"</div>";

        String baseL2 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 This is a multi-league puzzle!</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        Welcome to the <strong>Bronze League</strong>! The training wheels are off.<br>\n" +
"        The board has expanded to a massive 9x9 grid, and you are now facing up to 3 opponents simultaneously in a Free-For-All!\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Goal</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Be the first player to form a line of 5 of your marbles either horizontally, vertically, or diagonally.<br>\n" +
"                This puzzle is based on the official board game <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the grid.</li>\n" +
"                <li><strong>Rotate a 3x3 block</strong> 90 degrees either left (counter-clockwise) or right (clockwise).</li>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>8</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>8</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level2/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the grid is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>9</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the grid state. A <code>.</code> means empty. A number <const>0</const>-<const>3</const> represents a player's marble.</p>\n" +
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
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        If you want to understand all the mechanics in detail, you can find the <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">Referee source code here</a>.\n" +
"    </div>\n" +
"</div>";

        String baseL3 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 This is a multi-league puzzle!</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        You are now in the <strong>Silver League</strong>.<br>\n" +
"        The ultimate mechanic has been unlocked: <action>SWAP</action>. You can now exchange the physical positions of two blocks instead of rotating them!\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Goal</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Be the first player to form a line of 5 of your marbles either horizontally, vertically, or diagonally.<br>\n" +
"                This puzzle is based on the official board game <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the grid.</li>\n" +
"                <li><strong>Manipulate the board</strong> using ONE of the following methods:</li>\n" +
"                <ul>\n" +
"                    <li><strong>Rotate</strong> a 3x3 block 90 degrees either left or right.</li>\n" +
"                    <li><strong>Swap</strong> the physical positions of two adjacent 3x3 blocks (blocks sharing an edge).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>8</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>8</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the grid is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>9</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the grid state. A <code>.</code> means empty. A number <const>0</const>-<const>3</const> represents a player's marble.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Output for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotate): </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>dir</var> is <action>L</action> or <action>R</action>.\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 0 R</code> (Places marble at 2,1 and rotates block 0 right)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Swap): </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>b1</var> and <var>b2</var> are the IDs of the two adjacent blocks you wish to swap.\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 SWAP 0 1</code> (Places marble at 2,1 and swaps blocks 0 and 1)\n" +
"            </div>\n" +
"        </div>\n" +
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        If you want to understand all the mechanics in detail, you can find the <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">Referee source code here</a>.\n" +
"    </div>\n" +
"</div>";

        String baseL4 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 This is a multi-league puzzle!</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        You are now in the ultimate <strong>Gold League</strong>.<br>\n" +
"        The rules remain identical, but your opponents (and the Boss) will be much stronger. Good luck!\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Goal</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Be the first player to form a line of 5 of your marbles either horizontally, vertically, or diagonally.<br>\n" +
"                This puzzle is based on the official board game <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Rules</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>On your turn, you must perform exactly two actions:</p>\n" +
"            <ol>\n" +
"                <li><strong>Place a marble</strong> on any empty space on the grid.</li>\n" +
"                <li><strong>Manipulate the board</strong> using ONE of the following methods:</li>\n" +
"                <ul>\n" +
"                    <li><strong>Rotate</strong> a 3x3 block 90 degrees either left or right.</li>\n" +
"                    <li><strong>Swap</strong> the physical positions of two adjacent 3x3 blocks (blocks sharing an edge).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Blocks are numbered <const>0</const> to <const>8</const>, and coordinates <var>x</var>, <var>y</var> from <const>0</const> to <const>8</const> as shown below:</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Draws:</strong> If multiple players form a line of 5 simultaneously, it is a draw between those players. If the grid is completely filled with no lines of 5, the game ends in a global draw for all active players.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Game Input/Output</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Input for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Line 1: </span><var>size</var>, the size of the board (<const>9</const>).</p>\n" +
"                <p>Next <var>size</var> lines: strings representing the grid state. A <code>.</code> means empty. A number <const>0</const>-<const>3</const> represents a player's marble.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Output for one game turn</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotate): </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>dir</var> is <action>L</action> or <action>R</action>.\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 0 R</code> (Places marble at 2,1 and rotates block 0 right)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Swap): </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>b1</var> and <var>b2</var> are the IDs of the two adjacent blocks you wish to swap.\n" +
"                <br>\n" +
"                <strong>Example:</strong> <code>2 1 SWAP 0 1</code> (Places marble at 2,1 and swaps blocks 0 and 1)\n" +
"            </div>\n" +
"        </div>\n" +
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        If you want to understand all the mechanics in detail, you can find the <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">Referee source code here</a>.\n" +
"    </div>\n" +
"</div>";

        Files.write(Paths.get("config/level1/statement_en.html"), baseL1.getBytes("UTF-8"));
        Files.write(Paths.get("config/level2/statement_en.html"), baseL2.getBytes("UTF-8"));
        Files.write(Paths.get("config/level3/statement_en.html"), baseL3.getBytes("UTF-8"));
        Files.write(Paths.get("config/level4/statement_en.html"), baseL4.getBytes("UTF-8"));
        System.out.println("Done updating statements.");
    }
}
