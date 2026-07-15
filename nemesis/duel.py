#!/usr/bin/env python3
"""Duel Nemesis vs BossGold with fair random openings and seat rotation.

Both bots are deterministic, so variety comes from a 1-round random opening
(each player gets one random placement + rotation, chosen by the harness).
During the opening every bot process is still fed the state and its answer is
discarded, so that each bot sees its true first turn and deduces its id
correctly (piece count == seat index).

Usage: duel.py NEW_EXE OLD_EXE N_PLAYERS N_SEEDS [SEED0]
"""
import random, sys, time
import harness as H


def play_with_opening(exes, size, swaps, seed):
    """exes: list of executable paths, index = player id."""
    rng = random.Random(seed)
    P = len(exes)
    bots = [H.CppBot(p, P, i) for i, p in enumerate(exes)]
    board = H.Board(size)

    # --- opening: one full round of harness-chosen random moves ------------
    for pid in range(P):
        bots[pid].move(board, swaps)          # feed state, discard answer
        cells = [(x, y) for x in range(size) for y in range(size)
                 if board.g[y][x] == -1]
        x, y = rng.choice(cells)
        board.place(x, y, pid)
        board.rotate(rng.randrange(board.bpr ** 2), rng.choice("LR"))

    # --- real game ----------------------------------------------------------
    active = [True] * P
    cur, turn = 0, 0
    result = None
    while turn < 200:
        turn += 1
        if sum(active) <= 1:
            result = ({i for i, a in enumerate(active) if a}, "last-standing")
            break
        if not active[cur]:
            cur = (cur + 1) % P
            continue
        mv = bots[cur].move(board, swaps)
        ok = board.place(mv[0], mv[1], cur)
        if ok:
            if mv[2] == "SWAP":
                ok = swaps and board.swap(mv[3], mv[4])
            else:
                ok = board.rotate(mv[3], mv[4])
        if not ok:
            active[cur] = False
            print(f"    !! player {cur} DISQUALIFIED on {mv}")
        else:
            w = board.winners()
            if w:
                result = (w, "line")
                break
            if board.full():
                result = ({i for i, a in enumerate(active) if a}, "full-draw")
                break
        cur = (cur + 1) % P
    if result is None:
        result = ({i for i, a in enumerate(active) if a}, "max-turns")

    tmax = max(b.max_ms for b in bots)
    for b in bots:
        b.close()
    return result[0], result[1], tmax


def main():
    new_exe, old_exe = sys.argv[1], sys.argv[2]
    P = int(sys.argv[3])
    n_seeds = int(sys.argv[4])
    seed0 = int(sys.argv[5]) if len(sys.argv) > 5 else 1000

    stats = {"win": 0, "shared": 0, "loss": 0, "draw": 0}
    tmax = 0.0
    for s in range(n_seeds):
        for seat in range(P):                     # rotate Nemesis seat
            exes = [old_exe] * P
            exes[seat] = new_exe
            winners, how, t = play_with_opening(exes, 9, True, seed0 + s)
            tmax = max(tmax, t)
            if seat in winners and len(winners) == 1:
                stats["win"] += 1; res = "WIN"
            elif seat in winners:
                stats["shared"] += 1; res = "SHARED"
            elif how in ("full-draw", "max-turns"):
                stats["draw"] += 1; res = "DRAW"
            else:
                stats["loss"] += 1; res = "LOSS"
            print(f"seed {seed0+s} seat {seat}: {res} ({how})", flush=True)
    n = n_seeds * P
    print(f"\n== {P}p, {n} games: {stats} | winrate {stats['win']/n*100:.0f}%"
          f" | max move {tmax:.1f} ms", flush=True)


if __name__ == "__main__":
    main()
