import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateStatementsFR {
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
"            Ce challenge se déroule en <b>ligues</b>.\n" +
"        </p>\n" +
"        <div class=\"statement-league-alert-content\">\n" +
"            Pour ce challenge, plusieurs ligues pour le même jeu sont disponibles. Une fois que vous aurez prouvé votre valeur contre le Boss, vous accéderez à la ligue supérieure.<br>\n" +
"            <br>\n";

        String topAlertPart2 = 
"        </div>\n" +
"    </div>\n\n";

        String goalFr = 
"    <div class=\"statement-section statement-goal\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-goal\">&nbsp;</span>\n" +
"            <span>Objectif</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-goal-content\">\n" +
"            <p>\n" +
"                Soyez le premier joueur à former une ligne de 5 de vos billes horizontalement, verticalement ou diagonalement.<br>\n" +
"                Ce puzzle est basé sur le jeu de plateau <a href=\"https://en.wikipedia.org/wiki/Pentago\" target=\"_blank\">Pentago</a>.\n" +
"            </p>\n" +
"        </div>\n" +
"    </div>\n\n";

        String vicDefFr = 
"      <br />\n" +
"      <div class=\"statement-victory-conditions\">\n" +
"        <div class=\"icon victory\"></div>\n" +
"        <div class=\"blk\">\n" +
"          <div class=\"title\">Conditions de Victoire</div>\n" +
"          <div class=\"text\">\n" +
"          <ul style=\"padding-bottom: 0;\">\n" +
"            <li>Former une ligne de 5 billes de votre couleur horizontalement, verticalement ou diagonalement.</li>\n" +
"          </ul>\n" +
"          </div>\n" +
"        </div>\n" +
"      </div>\n" +
"      <div class=\"statement-lose-conditions\">\n" +
"        <div class=\"icon lose\"></div>\n" +
"        <div class=\"blk\">\n" +
"          <div class=\"title\">Conditions de Défaite</div>\n" +
"          <div class=\"text\">\n" +
"          <ul style=\"padding-bottom: 0;\">\n" +
"            <li>Votre programme dépasse le temps imparti, plante ou renvoie une commande invalide.</li>\n" +
"          </ul>\n" +
"          </div>\n" +
"        </div>\n" +
"      </div>\n" +
"      <br />\n";

        String expertRulesFr = 
"    <div class=\"statement-section statement-expertrules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-expertrules\">&nbsp;</span>\n" +
"            <span>Détails Techniques</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-expert-rules-content\">\n" +
"            <ul style=\"padding-left: 20px; padding-bottom: 0\">\n" +
"                <li>Vous pouvez consulter le code source du Referee sur <a href=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/src/main/java/com/codingame/game/Referee.java\" target=\"_blank\">GitHub</a>.</li>\n" +
"            </ul>\n" +
"        </div>\n" +
"    </div>\n";

        String initInputFr = 
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées d'initialisation</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1: </span><var>playerCount</var>, le nombre de joueurs dans la partie.</p>\n" +
"                <p><span class=\"statement-lineno\">Ligne 2: </span><var>myId</var>, votre ID de joueur (<const>0</const> à <const>3</const>).</p>\n" +
"            </div>\n" +
"        </div>\n";

        String constraintsFr = 
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Contraintes</div>\n" +
"            <div class=\"text\">\n" +
"                Temps de réponse pour le premier tour ≤ <const>1000</const> ms<br>\n" +
"                Temps de réponse pour un tour ≤ <const>50</const> ms<br>\n" +
"            </div>\n" +
"        </div>\n";

        String baseL1 = 
topAlertPart1 +
"            <b>Ligue Wood 4 :</b> Bienvenue ! Le jeu est simplifié pour que vous appreniez les bases. Vous jouez en duel 1v1 sur un plateau 6x6.\n" +
topAlertPart2 + goalFr +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>A votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur n'importe quelle case vide du plateau.</li>\n" +
"                <li><strong>Pivoter un bloc 3x3</strong> de 90 degrés vers la gauche (sens anti-horaire) ou la droite (sens horaire).</li>\n" +
"            </ol>\n" +
"            <p>Les blocs sont numérotés de <const>0</const> à <const>3</const>, et les coordonnées <var>x</var>, <var>y</var> de <const>0</const> à <const>5</const> comme illustré ci-dessous :</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level1/coords_6x6.png\" style=\"width: 100%; max-width: 400px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucune ligne de 5, la partie se termine par une égalité globale pour tous les joueurs actifs.</p>\n" +
vicDefFr + expertRulesFr +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h2>\n" +
initInputFr +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1: </span><var>size</var>, la taille du plateau (<const>6</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant le plateau. Un <const>.</const> signifie vide. Un chiffre de <const>0</const> à <const>1</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sorties pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Une seule ligne contenant 4 valeurs séparées par des espaces : </span>\n" +
"                <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.<br>\n" +
"                <var>block</var> : L'ID du bloc que vous souhaitez pivoter (<const>0</const> à <const>3</const>).<br>\n" +
"                <var>dir</var> : La direction de la rotation (<action>L</action> ou <action>R</action>).\n" +
"                <br><br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsFr +
"    </div>\n" +
"    <div style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-top: 10px; text-align: left;\">\n" +
"        <div style=\"text-align: center; margin-bottom: 6px\"><img src=\"//cdn.codingame.com/smash-the-code/statement/league_wood_04.png\" /></div>\n" +
"        <div style=\"text-align: center; font-weight: 700; margin-bottom: 6px;\">\n" +
"            Qu'est-ce qui m'attend dans les ligues supérieures ?\n" +
"        </div>\n" +
"        <ul>\n" +
"            <li>Jouez sur un plateau massif de 9x9 avec jusqu'à 3 adversaires simultanément.</li>\n" +
"            <li>Débloquez la mécanique SWAP pour échanger les positions de deux blocs 3x3.</li>\n" +
"        </ul>\n" +
"    </div>\n" +
"</div>";

        String baseL2 = 
topAlertPart1 +
"            <b>Ligue Wood 3 :</b> Fini de jouer. Le plateau s'est agrandi pour devenir un massif 9x9, et vous affrontez maintenant jusqu'à 3 adversaires simultanément !\n" +
topAlertPart2 + goalFr +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>A votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur n'importe quelle case vide du plateau.</li>\n" +
"                <li><strong>Pivoter un bloc 3x3</strong> de 90 degrés vers la gauche (sens anti-horaire) ou la droite (sens horaire).</li>\n" +
"            </ol>\n" +
"            <div style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;\">\n" +
"                <p>Les blocs sont numérotés de <const>0</const> à <const>8</const>, et les coordonnées <var>x</var>, <var>y</var> de <const>0</const> à <const>8</const> comme illustré ci-dessous :</p>\n" +
"            </div>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level2/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucune ligne de 5, la partie se termine par une égalité globale pour tous les joueurs actifs.</p>\n" +
vicDefFr + expertRulesFr +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h2>\n" +
initInputFr +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1: </span><var>size</var>, la taille du plateau (<const>9</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant le plateau. Un <const>.</const> signifie vide. Un chiffre de <const>0</const> à <const>3</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sorties pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Une seule ligne contenant 4 valeurs séparées par des espaces : </span>\n" +
"                <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.<br>\n" +
"                <var>block</var> : L'ID du bloc que vous souhaitez pivoter (<const>0</const> à <const>8</const>).<br>\n" +
"                <var>dir</var> : La direction de la rotation (<action>L</action> ou <action>R</action>).\n" +
"                <br><br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsFr +
"    </div>\n" +
"    <div style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 20px; margin-top: 10px; text-align: left;\">\n" +
"        <div style=\"text-align: center; margin-bottom: 6px\"><img src=\"//cdn.codingame.com/smash-the-code/statement/league_wood_04.png\" /></div>\n" +
"        <div style=\"text-align: center; font-weight: 700; margin-bottom: 6px;\">\n" +
"            Qu'est-ce qui m'attend dans les ligues supérieures ?\n" +
"        </div>\n" +
"        <ul>\n" +
"            <li>Débloquez la mécanique SWAP pour échanger les positions de deux blocs 3x3.</li>\n" +
"        </ul>\n" +
"    </div>\n" +
"</div>";

        String baseL3 = 
topAlertPart1 +
"            <b>Ligue Wood 2 :</b> La mécanique ultime a été débloquée : <action>SWAP</action>. Vous pouvez désormais échanger les positions de deux blocs au lieu de les pivoter !\n" +
topAlertPart2 + goalFr +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>A votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur n'importe quelle case vide du plateau.</li>\n" +
"                <li><strong>Manipuler le plateau</strong> en utilisant UNE des méthodes suivantes :</li>\n" +
"                <ul>\n" +
"                    <li><strong>Pivoter</strong> un bloc 3x3 de 90 degrés vers la gauche ou la droite.</li>\n" +
"                    <li style=\"color: #7cc576; background-color: rgba(124, 197, 118,.1); padding: 2px;\"><strong>Échanger (Swap)</strong> les positions de deux blocs 3x3 adjacents (blocs partageant un bord).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Les blocs sont numérotés de <const>0</const> à <const>8</const>, et les coordonnées <var>x</var>, <var>y</var> de <const>0</const> à <const>8</const> comme illustré ci-dessous :</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucune ligne de 5, la partie se termine par une égalité globale pour tous les joueurs actifs.</p>\n" +
vicDefFr + expertRulesFr +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h2>\n" +
initInputFr +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1: </span><var>size</var>, la taille du plateau (<const>9</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant le plateau. Un <const>.</const> signifie vide. Un chiffre de <const>0</const> à <const>3</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sorties pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotation) : Une seule ligne contenant 4 valeurs séparées par des espaces : </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.<br>\n" +
"                <var>block</var> : L'ID du bloc que vous souhaitez pivoter (<const>0</const> à <const>8</const>).<br>\n" +
"                <var>dir</var> : La direction de la rotation (<action>L</action> ou <action>R</action>).\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Échange) : Une seule ligne contenant 5 valeurs séparées par des espaces : </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.<br>\n" +
"                <var>b1</var> et <var>b2</var> : Les ID des deux blocs adjacents que vous souhaitez échanger.\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 SWAP 0 1</code> (Place une bille en 2,1 et échange les blocs 0 et 1)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsFr +
"    </div>\n" +
"</div>";

        String baseL4 = 
topAlertPart1 +
"            <b>Ligue Wood 1 :</b> Les règles restent identiques, mais vos adversaires (et le Boss) seront beaucoup plus forts. Bonne chance !\n" +
topAlertPart2 + goalFr +
"    <div class=\"statement-section statement-rules\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-rules\">&nbsp;</span>\n" +
"            <span>Règles</span>\n" +
"        </h2>\n" +
"        <div class=\"statement-rules-content\">\n" +
"            <p>A votre tour, vous devez effectuer exactement deux actions :</p>\n" +
"            <ol>\n" +
"                <li><strong>Placer une bille</strong> sur n'importe quelle case vide du plateau.</li>\n" +
"                <li><strong>Manipuler le plateau</strong> en utilisant UNE des méthodes suivantes :</li>\n" +
"                <ul>\n" +
"                    <li><strong>Pivoter</strong> un bloc 3x3 de 90 degrés vers la gauche ou la droite.</li>\n" +
"                    <li><strong>Échanger (Swap)</strong> les positions de deux blocs 3x3 adjacents (blocs partageant un bord).</li>\n" +
"                </ul>\n" +
"            </ol>\n" +
"            <p>Les blocs sont numérotés de <const>0</const> à <const>8</const>, et les coordonnées <var>x</var>, <var>y</var> de <const>0</const> à <const>8</const> comme illustré ci-dessous :</p>\n" +
"            <img src=\"https://raw.githubusercontent.com/n3l-ctrl/Pentago-/main/config/level3/coords_9x9.png\" style=\"width: 100%; max-width: 500px; display: block; margin: 10px auto;\" />\n" +
"            <p><strong>Égalités :</strong> Si plusieurs joueurs forment une ligne de 5 simultanément, c'est un match nul entre ces joueurs. Si le plateau est totalement rempli sans aucune ligne de 5, la partie se termine par une égalité globale pour tous les joueurs actifs.</p>\n" +
vicDefFr + expertRulesFr +
"        </div>\n" +
"    </div>\n" +
"\n" +
"    <div class=\"statement-section statement-protocol\">\n" +
"        <h2>\n" +
"            <span class=\"icon icon-protocol\">&nbsp;</span>\n" +
"            <span>Entrées/Sorties du jeu</span>\n" +
"        </h2>\n" +
initInputFr +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Entrées pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <p><span class=\"statement-lineno\">Ligne 1: </span><var>size</var>, la taille du plateau (<const>9</const>).</p>\n" +
"                <p>Les <var>size</var> lignes suivantes : chaînes de caractères représentant le plateau. Un <const>.</const> signifie vide. Un chiffre de <const>0</const> à <const>3</const> représente la bille d'un joueur.</p>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div class=\"blk\">\n" +
"            <div class=\"title\">Sorties pour un tour de jeu</div>\n" +
"            <div class=\"text\">\n" +
"                <span class=\"statement-lineno\">Option A (Rotation) : Une seule ligne contenant 4 valeurs séparées par des espaces : </span> <var>x</var> <var>y</var> <var>block</var> <var>dir</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.<br>\n" +
"                <var>block</var> : L'ID du bloc que vous souhaitez pivoter (<const>0</const> à <const>8</const>).<br>\n" +
"                <var>dir</var> : La direction de la rotation (<action>L</action> ou <action>R</action>).\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 0 R</code> (Place une bille en 2,1 et pivote le bloc 0 vers la droite)\n" +
"                <br><br>\n" +
"                <span class=\"statement-lineno\">Option B (Échange) : Une seule ligne contenant 5 valeurs séparées par des espaces : </span> <var>x</var> <var>y</var> <action>SWAP</action> <var>b1</var> <var>b2</var>\n" +
"                <br>\n" +
"                <var>x</var>, <var>y</var> : Coordonnées où placer votre bille.<br>\n" +
"                <var>b1</var> et <var>b2</var> : Les ID des deux blocs adjacents que vous souhaitez échanger.\n" +
"                <br>\n" +
"                <strong>Exemple :</strong> <code>2 1 SWAP 0 1</code> (Place une bille en 2,1 et échange les blocs 0 et 1)\n" +
"            </div>\n" +
"        </div>\n" +
constraintsFr +
"    </div>\n" +
"</div>";

        Files.write(Paths.get("config/level1/statement_fr.html"), baseL1.getBytes("UTF-8"));
        Files.write(Paths.get("config/level2/statement_fr.html"), baseL2.getBytes("UTF-8"));
        Files.write(Paths.get("config/level3/statement_fr.html"), baseL3.getBytes("UTF-8"));
        Files.write(Paths.get("config/level4/statement_fr.html"), baseL4.getBytes("UTF-8"));
        System.out.println("Done updating FR statements.");
    }
}
