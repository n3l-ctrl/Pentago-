import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateStatementsFR {
    public static void main(String[] args) throws Exception {
        
        String darkRibbonStyle = "background-color: #2b2b2b; color: #f2f2f2; padding: 15px; border-radius: 5px; margin-bottom: 20px;";
        String greenHighlight = "color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;";

        String baseL1 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 Ceci est un puzzle multi-ligues !</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        Bienvenue dans la <strong>Ligue Bois</strong> ! Le jeu est simplifié pour vous apprendre les bases.<br>\n" +
"        Vous jouez un duel 1v1 sur un plateau 6x6.\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Objectif</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Soyez le premier joueur à former un alignement de 5 billes horizontalement, verticalement ou diagonalement.<br>\n" +
"                Ce puzzle est basé sur le jeu de plateau officiel <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>À votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur un emplacement vide du plateau.</li>\n" +
"                <li><strong>Pivoter un bloc 3x3</strong> de 90 degrés vers la gauche ou vers la droite.</li>\n" +
"            </ol>\n" +
"            <p>Les blocs sont numérotés de <const>0</const> à <const>3</const>, et les coordonnées <var>x</var>, <var>y</var> vont de <const>0</const> à <const>5</const> comme illustré ci-dessous :</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level1/coords_6x6.png\" style=\"width: 100%; max-width: 400px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucun alignement de 5, la partie se termine par une égalité globale.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1 : </span><var>size</var>, la taille du plateau (<const>6</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant l'état du plateau. Un <code>.</code> signifie vide. Un chiffre <const>0</const>-<const>1</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sortie pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Une seule ligne contenant 4 valeurs séparées par des espaces : </span>\n" +
"                <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.\n" +
"                <br>\n" +
"                <var>block</var> : L'ID du bloc à pivoter (<const>0</const> à <const>3</const>).\n" +
"                <br>\n" +
"                <var>dir</var> : La direction de rotation (<action>L</action> pour gauche ou <action>R</action> pour droite).\n" +
"                <br><br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"            </div>\n" +
"        </div>\n" +
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        Pour comprendre les mécaniques en détail, vous pouvez consulter le <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">code source du Referee ici</a>.\n" +
"    </div>\n" +
"</div>";

        String baseL2 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 Ceci est un puzzle multi-ligues !</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        Bienvenue dans la <strong>Ligue Bronze</strong> ! Fini de jouer.<br>\n" +
"        Le plateau s'est étendu à un format massif 9x9, et vous affrontez maintenant jusqu'à 3 adversaires en même temps dans un Chacun pour Soi !\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Objectif</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Soyez le premier joueur à former un alignement de 5 billes horizontalement, verticalement ou diagonalement.<br>\n" +
"                Ce puzzle est basé sur le jeu de plateau officiel <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>À votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur un emplacement vide du plateau.</li>\n" +
"                <li><strong>Pivoter un bloc 3x3</strong> de 90 degrés vers la gauche ou vers la droite.</li>\n" +
"            </ol>\n" +
"            <div style=\"" + greenHighlight + "\">\n" +
"                <p>Les blocs sont numérotés de <const>0</const> à <const>8</const>, et les coordonnées <var>x</var>, <var>y</var> vont de <const>0</const> à <const>8</const> comme illustré ci-dessous :</p>\n" +
"            </div>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level2/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucun alignement de 5, la partie se termine par une égalité globale.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1 : </span><var>size</var>, la taille du plateau (<const>9</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant l'état du plateau. Un <code>.</code> signifie vide. Un chiffre <const>0</const>-<const>3</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sortie pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Une seule ligne contenant 4 valeurs séparées par des espaces : </span>\n" +
"                <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.\n" +
"                <br>\n" +
"                <var>block</var> : L'ID du bloc à pivoter (<const>0</const> à <const>8</const>).\n" +
"                <br>\n" +
"                <var>dir</var> : La direction de rotation (<action>L</action> ou <action>R</action>).\n" +
"                <br><br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"            </div>\n" +
"        </div>\n" +
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        Pour comprendre les mécaniques en détail, vous pouvez consulter le <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">code source du Referee ici</a>.\n" +
"    </div>\n" +
"</div>";

        String baseL3 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 Ceci est un puzzle multi-ligues !</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        Vous êtes maintenant dans la <strong>Ligue Argent</strong>.<br>\n" +
"        La mécanique ultime a été débloquée : <action>SWAP</action>. Vous pouvez désormais échanger la position physique de deux blocs au lieu de les pivoter !\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Objectif</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Soyez le premier joueur à former un alignement de 5 billes horizontalement, verticalement ou diagonalement.<br>\n" +
"                Ce puzzle est basé sur le jeu de plateau officiel <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>À votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur un emplacement vide du plateau.</li>\n" +
"                <li><strong>Manipuler le plateau</strong> en utilisant UNE des méthodes suivantes :</li>\n" +
"                <ul>\n" +
"                    <li><strong>Pivoter</strong> un bloc 3x3 de 90 degrés vers la gauche ou la droite.</li>\n" +
"                    <div style=\"" + greenHighlight + "\">\n" +
"                        <li><strong>Échanger (Swap)</strong> les positions physiques de deux blocs 3x3 adjacents (partageant un bord).</li>\n" +
"                    </div>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Les blocs sont numérotés de <const>0</const> à <const>8</const>, et les coordonnées <var>x</var>, <var>y</var> vont de <const>0</const> à <const>8</const> comme illustré ci-dessous :</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucun alignement de 5, la partie se termine par une égalité globale.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1 : </span><var>size</var>, la taille du plateau (<const>9</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant l'état du plateau. Un <code>.</code> signifie vide. Un chiffre <const>0</const>-<const>3</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sorties pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotation) : </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>dir</var> est <action>L</action> ou <action>R</action>.\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Échange) : </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>b1</var> et <var>b2</var> sont les ID des deux blocs adjacents à échanger.\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 SWAP 0 1</code> (Place une bille en 2,1 et échange les blocs 0 et 1)\n" +
"            </div>\n" +
"        </div>\n" +
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        Pour comprendre les mécaniques en détail, vous pouvez consulter le <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">code source du Referee ici</a>.\n" +
"    </div>\n" +
"</div>";

        String baseL4 = 
"<div class=\"statement-body\">\n" +
"    <div style=\"" + darkRibbonStyle + "\">\n" +
"      <div style=\"font-size: 1.2em; margin-bottom: 10px;\">\n" +
"        <strong>🏆 Ceci est un puzzle multi-ligues !</strong>\n" +
"      </div>\n" +
"      <div>\n" +
"        Vous êtes maintenant dans l'ultime <strong>Ligue Or</strong>.<br>\n" +
"        Les règles restent identiques, mais vos adversaires (et le Boss) seront bien plus forts. Bonne chance !\n" +
"      </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-goal\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Objectif</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Soyez le premier joueur à former un alignement de 5 billes horizontalement, verticalement ou diagonalement.<br>\n" +
"                Ce puzzle est basé sur le jeu de plateau officiel <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h1>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>À votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur un emplacement vide du plateau.</li>\n" +
"                <li><strong>Manipuler le plateau</strong> en utilisant UNE des méthodes suivantes :</li>\n" +
"                <ul>\n" +
"                    <li><strong>Pivoter</strong> un bloc 3x3 de 90 degrés vers la gauche ou la droite.</li>\n" +
"                    <li><strong>Échanger (Swap)</strong> les positions physiques de deux blocs 3x3 adjacents (partageant un bord).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Les blocs sont numérotés de <const>0</const> à <const>8</const>, et les coordonnées <var>x</var>, <var>y</var> vont de <const>0</const> à <const>8</const> comme illustré ci-dessous :</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucun alignement de 5, la partie se termine par une égalité globale.</p>\n" +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h1>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h1>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1 : </span><var>size</var>, la taille du plateau (<const>9</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant l'état du plateau. Un <code>.</code> signifie vide. Un chiffre <const>0</const>-<const>3</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sorties pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotation) : </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>dir</var> est <action>L</action> ou <action>R</action>.\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Échange) : </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>b1</var> et <var>b2</var> sont les ID des deux blocs adjacents à échanger.\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 SWAP 0 1</code> (Place une bille en 2,1 et échange les blocs 0 et 1)\n" +
"            </div>\n" +
"        </div>\n" +
"    </div>\n" +
"    <br>\n" +
"    <div style=\"text-align: center; color: #888; font-size: 0.9em;\">\n" +
"        Pour comprendre les mécaniques en détail, vous pouvez consulter le <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">code source du Referee ici</a>.\n" +
"    </div>\n" +
"</div>";

        Files.write(Paths.get("config/level1/statement_fr.html"), baseL1.getBytes("UTF-8"));
        Files.write(Paths.get("config/level2/statement_fr.html"), baseL2.getBytes("UTF-8"));
        Files.write(Paths.get("config/level3/statement_fr.html"), baseL3.getBytes("UTF-8"));
        Files.write(Paths.get("config/level4/statement_fr.html"), baseL4.getBytes("UTF-8"));
        System.out.println("Done updating FR statements.");
    }
}
