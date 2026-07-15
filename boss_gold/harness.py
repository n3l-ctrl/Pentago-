#!/usr/bin/env python3
"""Test harness: faithful port of Board.java + Referee turn loop.

Pits the C++ BossGold (subprocess, persistent like on CG) against baseline
bots replicating the current Boss.java logic.
"""
import random, re, subprocess, sys, time

class Board:
    def __init__(self, size):
        self.size = size
        self.bpr = 2 if size == 6 else 3
        self.g = [[-1] * size for _ in range(size)]

    def place(self, x, y, pid):
        s = self.size
        if x < 0 or x >= s or y < 0 or y >= s or self.g[y][x] != -1:
            return False
        self.g[y][x] = pid
        return True

    def rotate(self, b, d):
        mb = self.bpr * self.bpr
        if b < 0 or b >= mb or d not in ("L", "R"):
            return False
        sx, sy = (b % self.bpr) * 3, (b // self.bpr) * 3
        t = [[self.g[sy + i][sx + j] for j in range(3)] for i in range(3)]
        for i in range(3):
            for j in range(3):
                if d == "R":
                    self.g[sy + j][sx + 2 - i] = t[i][j]
                else:
                    self.g[sy + 2 - j][sx + i] = t[i][j]
        return True

    def swap(self, b1, b2):
        mb = self.bpr * self.bpr
        if not (0 <= b1 < mb and 0 <= b2 < mb) or b1 == b2:
            return False
        x1, y1, x2, y2 = b1 % self.bpr, b1 // self.bpr, b2 % self.bpr, b2 // self.bpr
        if abs(x1 - x2) + abs(y1 - y2) != 1:
            return False
        for i in range(3):
            for j in range(3):
                a, b = (y1 * 3 + i, x1 * 3 + j), (y2 * 3 + i, x2 * 3 + j)
                self.g[a[0]][a[1]], self.g[b[0]][b[1]] = self.g[b[0]][b[1]], self.g[a[0]][a[1]]
        return True

    def winners(self):
        s, g, won = self.size, self.g, set()
        def chk(x, y, dx, dy):
            p = g[y][x]
            if p == -1: return
            for i in range(1, 5):
                if g[y + dy * i][x + dx * i] != p: return
            won.add(p)
        for y in range(s):
            for x in range(s - 4): chk(x, y, 1, 0)
        for x in range(s):
            for y in range(s - 4): chk(x, y, 0, 1)
        for y in range(s - 4):
            for x in range(s - 4): chk(x, y, 1, 1)
        for y in range(s - 4):
            for x in range(4, s): chk(x, y, -1, 1)
        return won

    def full(self):
        return all(c != -1 for r in self.g for c in r)

    def rows(self):
        return ["".join("." if c == -1 else str(c) for c in r) for r in self.g]


def apply_sim(board, pid, mv):
    """Simulate move on a copy; return resulting Board or None if invalid."""
    b = Board(board.size)
    b.g = [r[:] for r in board.g]
    if not b.place(mv[0], mv[1], pid):
        return None
    if mv[2] == "SWAP":
        if not b.swap(mv[3], mv[4]): return None
    else:
        if not b.rotate(mv[3], mv[4]): return None
    return b


def all_moves(board, swaps):
    s, bpr = board.size, board.bpr
    mvs = []
    for y in range(s):
        for x in range(s):
            if board.g[y][x] != -1:
                continue
            for b in range(bpr * bpr):
                mvs.append((x, y, "ROT", b, "L"))
                mvs.append((x, y, "ROT", b, "R"))
            if swaps:
                for b1 in range(bpr * bpr):
                    for b2 in range(b1 + 1, bpr * bpr):
                        x1, y1 = b1 % bpr, b1 // bpr
                        x2, y2 = b2 % bpr, b2 // bpr
                        if abs(x1 - x2) + abs(y1 - y2) == 1:
                            mvs.append((x, y, "SWAP", b1, b2))
    return mvs


class BaselineBot:
    """Port of the repo's Boss.java (win / block / random), with CORRECT id."""
    def __init__(self, pid, opponents, rng):
        self.pid, self.opponents, self.rng = pid, opponents, rng

    def move(self, board, swaps):
        mvs = all_moves(board, swaps)
        for m in mvs:                       # 1. win
            nb = apply_sim(board, self.pid, m)
            if nb and self.pid in nb.winners():
                return m
        for o in self.opponents:            # 2. block
            for m in mvs:
                nb = apply_sim(board, o, m)
                if nb and o in nb.winners():
                    return m
        return self.rng.choice(mvs)         # 3. random


class CppBot:
    def __init__(self, path, n_players, pid, size):
        self.p = subprocess.Popen([path], stdin=subprocess.PIPE,
                                  stdout=subprocess.PIPE, text=True, bufsize=1)
        self.max_ms = 0.0
        self.p.stdin.write(f"{n_players}\n{pid}\n{size}\n")
        self.p.stdin.flush()

    def move(self, board, swaps):
        inp = "\n".join(board.rows()) + "\n"
        t0 = time.perf_counter()
        self.p.stdin.write(inp); self.p.stdin.flush()
        line = self.p.stdout.readline().strip()
        ms = (time.perf_counter() - t0) * 1000
        self.max_ms = max(self.max_ms, ms)
        m = re.match(r"^(\d+) (\d+) (\d+) (\d+)$", line)
        if m:
            return (int(m[1]), int(m[2]), "SWAP", int(m[3]), int(m[4]))
        m = re.match(r"^(\d+) (\d+) (\d+) ([LR])$", line)
        if m:
            return (int(m[1]), int(m[2]), "ROT", int(m[3]), m[4])
        raise RuntimeError("bad output: " + repr(line))

    def close(self):
        self.p.stdin.close(); self.p.terminate()


def play(bots, size, swaps, max_turns=200):
    """bots: list of move-providers, index = player id. Returns set of winners
    (scores 1), or 'draw-all', plus disqualified list."""
    board = Board(size)
    active = [True] * len(bots)
    cur, turn = 0, 0
    while turn < max_turns:
        turn += 1
        if sum(active) <= 1:
            return {i for i, a in enumerate(active) if a}, "last-standing"
        if not active[cur]:
            cur = (cur + 1) % len(bots); continue
        mv = bots[cur].move(board, swaps)
        ok = board.place(mv[0], mv[1], cur)
        if ok:
            if mv[2] == "SWAP":
                ok = board.swap(mv[3], mv[4]) and swaps
            else:
                ok = board.rotate(mv[3], mv[4])
        if not ok:
            active[cur] = False
            print(f"  !! player {cur} DISQUALIFIED on {mv}")
        else:
            w = board.winners()
            if w:
                return w, "line"
            if board.full():
                return {i for i, a in enumerate(active) if a}, "full-draw"
        cur = (cur + 1) % len(bots)
    return {i for i, a in enumerate(active) if a}, "max-turns"


def main():
    exe = "./bossgold"
    n_players = int(sys.argv[1]) if len(sys.argv) > 1 else 3
    n_games = int(sys.argv[2]) if len(sys.argv) > 2 else 12
    seed = int(sys.argv[3]) if len(sys.argv) > 3 else 42
    rng = random.Random(seed)

    stats = {"win": 0, "shared": 0, "loss": 0, "draw": 0}
    gmax = 0.0
    cpp = None
    for game in range(n_games):
        seat = game % n_players           # rotate boss seat
        if cpp: gmax = max(gmax, cpp.max_ms); cpp.close()
        cpp = CppBot(exe, n_players, seat)                 # fresh process per game (id caching!)
        bots = []
        for i in range(n_players):
            if i == seat:
                bots.append(cpp)
            else:
                bots.append(BaselineBot(i, [o for o in range(n_players) if o != i], rng))
        winners, how = play(bots, 9, True)
        if seat in winners and len(winners) == 1:
            stats["win"] += 1; res = "WIN"
        elif seat in winners:
            stats["shared"] += 1; res = "SHARED"
        elif how in ("full-draw", "max-turns"):
            stats["draw"] += 1; res = "DRAW"
        else:
            stats["loss"] += 1; res = "LOSS"
        print(f"game {game}: seat={seat} -> {res} ({how}, winners={sorted(winners)})")
    gmax = max(gmax, cpp.max_ms)
    print(f"\n{n_players}p x {n_games} games: {stats}, boss max move time = {gmax:.1f} ms")
    cpp.close()

if __name__ == "__main__":
    main()
