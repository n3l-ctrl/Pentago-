# Nemesis — bot conçu pour battre BossGold

## Compilation

```
g++ -O3 -std=c++17 -o nemesis Nemesis.cpp
```

Compatible CodinGame (fichier unique, mêmes E/S que le boss). Fonctionne en 6x6
(sans SWAP) et 9x9 (avec SWAP), 2 à 4 joueurs, < 42 ms par coup (limite 50 ms).

## Ce qui le rend plus fort que BossGold

1. **Détection exacte de victoire forcée (offense, 2-ply prouvé).** Un coup est
   déclaré gagnant si, pour *chaque* transformation adverse, soit une ligne de 5
   complète survit (aucun placement ne peut l'arrêter), soit il reste au moins
   2 cases de complétion distinctes (un seul placement adverse n'en tue qu'une).
   BossGold n'a qu'un bonus heuristique qui poursuit parfois de fausses doubles
   menaces.

2. **Évitement de la condamnation (défense, 2-ply).** Pour chaque candidat sûr,
   Nemesis simule les ripostes les plus offensives de l'adversaire suivant ; si
   l'une d'elles est une victoire forcée prouvée pour lui, le candidat est
   condamné et pénalisé. Le scan s'étend dans le budget temps jusqu'à trouver un
   coup non condamné (souvent un coup qui *conteste* la structure adverse, mal
   classé par l'éval statique). C'est exactement l'angle mort qui fait perdre
   BossGold.

3. **Exploitation de politique (2 joueurs).** BossGold est déterministe : sa
   logique de décision est portée à l'identique dans Nemesis, qui prédit sa
   riposte exacte et évalue la position réelle 3-4 demi-coups plus loin, au lieu
   du pire cas paranoïaque.

4. **Stratégie conditionnelle au tempo (2 joueurs).** Le miroir
   BossGold-vs-BossGold montre que le premier joueur gagne ~90 % : avec le
   trait, Nemesis joue la politique de course la plus forte connue (celle du
   boss), surclassée par la détection de victoires forcées ; sans le trait, il
   bascule en mode défensif.

5. **Danger gradué + éval affinée.** Blocage plus précoce des structures à 3
   (SOP[3]↑), bonus positionnel sur les centres de blocs des grandes diagonales
   (squelette le plus résistant aux transformations), et minimisation du nombre
   d'options gagnantes adverses quand aucun coup sûr n'existe.

## Résultats (seeds de validation, jamais utilisés pour le réglage)

Ouvertures aléatoires équitables, rotation des sièges, `duel.py` (référee porté
de Board.java). « Par » = score d'un clone de BossGold (symétrie).

| Format | Games | Nemesis | Par (clone) |
|---|---|---|---|
| 2 joueurs, siège 0 (trait) | 40 | **38 victoires (95 %)** | 90 % |
| 2 joueurs, siège 1 | 40 | **5 victoires (12,5 %)** | 10 % |
| 3 joueurs (1 vs 2 boss) | 66 | 23 victoires (35 %) | 33 % |
| 4 joueurs (1 vs 3 boss), score=1 | 56 | **25 (45 %)** | 25 % |

Constat clé : entre deux bots de ce niveau, Pentago XL est dominé par
l'avantage du trait (~90 % dans le miroir). Nemesis bat BossGold sur tous les
formats sans jamais être en dessous, avec l'écart le plus net en 4 joueurs
(x1,8 le taux de score attendu) et une conversion quasi parfaite du trait en
duel.

## Reproduire

```
g++ -O3 -o nemesis Nemesis.cpp
g++ -O3 -o bossgold ../boss_gold/BossGold.cpp
cp ../boss_gold/harness.py .
python3 duel.py ./nemesis ./bossgold 2 20 6000   # 2 joueurs, 20 seeds
python3 duel.py ./nemesis ./bossgold 3 12 9500   # 3 joueurs
python3 duel.py ./nemesis ./bossgold 4 8 9800    # 4 joueurs
```
