// ============================================================================
// Pentago XL - "Nemesis" v2 (anti-BossGold)
// ----------------------------------------------------------------------------
// Same bitboard core as BossGold, plus three decisive upgrades:
//
//   A. EXACT forced-win detection (offense): a candidate move is a PROVEN
//      2-ply win vs the next opponent if for EVERY opponent transform, either
//      I already own a full 5-line in the transformed board (a placement
//      cannot stop me), or I keep >= 2 DISTINCT completion cells (a single
//      placement kills at most one). BossGold only approximates this with a
//      static bonus, so Nemesis finds real double threats one move earlier
//      and never chases fake ones.
//
//   B. DOOM avoidance (defense): among the top safe candidates, simulate the
//      next opponent's most threatening replies; if one of them is itself a
//      proven forced win for the opponent (and does not hand me an immediate
//      win first), the candidate is doomed and heavily penalized. This is
//      exactly the blind spot that makes BossGold lose tempo races.
//
//   C. Graded danger: when no safe move exists, minimize the number of alive
//      opponent winning options instead of a binary flag.
// ============================================================================
#pragma GCC optimize("O3,omit-frame-pointer,inline")
#include <bits/stdc++.h>
using namespace std;

typedef unsigned __int128 u128;
typedef unsigned long long u64;

static inline int pc128(u128 x) {
    return __builtin_popcountll((u64)x) + __builtin_popcountll((u64)(x >> 64));
}
static inline int ctz128(u128 x) {
    u64 lo = (u64)x;
    if (lo) return __builtin_ctzll(lo);
    return 64 + __builtin_ctzll((u64)(x >> 64));
}
static inline u128 bit128(int i) { return (u128)1 << i; }

static int S = 0, B = 0, NC = 0;
static bool SWAPS = false;

struct Transform {
    uint8_t perm[81];
    uint8_t inv[81];
    string suffix;
};

static vector<Transform> TR;
static vector<u128> WIN;
static vector<vector<uint16_t>> CWIN;
static vector<int> POS;

static inline u128 applyPerm(const uint8_t *p, u128 bb) {
    u128 r = 0;
    while (bb) {
        int i = ctz128(bb);
        bb &= bb - 1;
        r |= bit128(p[i]);
    }
    return r;
}

static void addWindow(int x, int y, int dx, int dy) {
    u128 m = 0;
    for (int i = 0; i < 5; i++) m |= bit128((y + dy * i) * S + (x + dx * i));
    WIN.push_back(m);
}

static void initTables(int size, bool swapsAllowed) {
    S = size; B = S / 3; NC = S * S; SWAPS = swapsAllowed;
    TR.clear(); WIN.clear();

    for (int b = 0; b < B * B; b++) {
        int sx = (b % B) * 3, sy = (b / B) * 3;
        for (int d = 0; d < 2; d++) {
            Transform t;
            for (int c = 0; c < NC; c++) t.perm[c] = (uint8_t)c;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++) {
                    int src = (sy + i) * S + (sx + j);
                    int dst = d == 0 ? (sy + j) * S + (sx + 2 - i)
                                     : (sy + 2 - j) * S + (sx + i);
                    t.perm[src] = (uint8_t)dst;
                }
            t.suffix = to_string(b) + (d == 0 ? " R" : " L");
            TR.push_back(t);
        }
    }
    if (SWAPS) {
        for (int b1 = 0; b1 < B * B; b1++)
            for (int b2 = b1 + 1; b2 < B * B; b2++) {
                int x1 = b1 % B, y1 = b1 / B, x2 = b2 % B, y2 = b2 / B;
                if (abs(x1 - x2) + abs(y1 - y2) != 1) continue;
                Transform t;
                for (int c = 0; c < NC; c++) t.perm[c] = (uint8_t)c;
                for (int i = 0; i < 3; i++)
                    for (int j = 0; j < 3; j++) {
                        int c1 = (y1 * 3 + i) * S + (x1 * 3 + j);
                        int c2 = (y2 * 3 + i) * S + (x2 * 3 + j);
                        t.perm[c1] = (uint8_t)c2;
                        t.perm[c2] = (uint8_t)c1;
                    }
                t.suffix = "SWAP " + to_string(b1) + " " + to_string(b2);
                TR.push_back(t);
            }
    }
    for (auto &t : TR)
        for (int c = 0; c < NC; c++) t.inv[t.perm[c]] = (uint8_t)c;

    for (int y = 0; y < S; y++)
        for (int x = 0; x + 4 < S; x++) addWindow(x, y, 1, 0);
    for (int x = 0; x < S; x++)
        for (int y = 0; y + 4 < S; y++) addWindow(x, y, 0, 1);
    for (int y = 0; y + 4 < S; y++)
        for (int x = 0; x + 4 < S; x++) addWindow(x, y, 1, 1);
    for (int y = 0; y + 4 < S; y++)
        for (int x = 4; x < S; x++) addWindow(x, y, -1, 1);

    CWIN.assign(NC, {});
    for (int w = 0; w < (int)WIN.size(); w++)
        for (int c = 0; c < NC; c++)
            if (WIN[w] & bit128(c)) CWIN[c].push_back((uint16_t)w);

    POS.assign(NC, 0);
    int mid = S / 2;
    for (int y = 0; y < S; y++)
        for (int x = 0; x < S; x++) {
            int c = y * S + x;
            if (x % 3 == 1 && y % 3 == 1) {
                POS[c] += 7;
                // block centers on the two long diagonals: the most
                // transform-resilient skeleton of the board
                if (x == y || x + y == S - 1) POS[c] += 4;
                if (x == mid && y == mid) POS[c] += 2;
            }
            POS[c] += 4 - (abs(x - mid) + abs(y - mid)) / 2;
        }
}

static const int SMY[6] = {0, 2, 10, 48, 240, 1000000};
static const int SOP[6] = {0, 2, 13, 100, 380, 1000000};

// ---------------------------------------------------------------------------
// Does `mine` have an immediate winning move in a state with occupancy `occ`?
// (place on an empty cell, then one transform)
// ---------------------------------------------------------------------------
static bool hasImmediateWin(u128 mine, u128 occAll) {
    if (pc128(mine) < 4) return false;
    int NT = (int)TR.size(), NW = (int)WIN.size();
    for (int T = 0; T < NT; T++) {
        u128 m = applyPerm(TR[T].perm, mine);
        for (int w = 0; w < NW; w++) {
            int c = pc128(m & WIN[w]);
            if (c == 5) return true;
            if (c == 4) {
                u128 miss = WIN[w] & ~m;
                int pre = TR[T].inv[ctz128(miss)];
                if (!(occAll & bit128(pre))) return true;
            }
        }
    }
    return false;
}

// ---------------------------------------------------------------------------
// Robustness of the threat structure of `m1` (occupancy o1), i.e. after the
// move that produced this state. Result:
//   3 : a full 5-line survives every enemy transform          -> forced win
//   2 : >= 2 distinct completion cells survive every transform -> forced win
//   1 : worst case exactly 1 completion cell (blockable)
//   0 : some enemy transform erases all winning options
// Sound for the single next opponent: their move = 1 placement + 1 transform;
// the placement can only occupy ONE completion cell.
// ---------------------------------------------------------------------------
static int robustness(u128 m1, u128 o1) {
    if (pc128(m1) < 4) return 0;
    int NT = (int)TR.size(), NW = (int)WIN.size();
    int minSurv = 3;
    for (int T = 0; T < NT; T++) {
        u128 m2 = applyPerm(TR[T].perm, m1);
        u128 o2 = applyPerm(TR[T].perm, o1);
        bool typeB = false;
        u128 Mset = 0;
        int nM = 0;
        for (int T2 = 0; T2 < NT && !typeB; T2++) {
            u128 b3 = applyPerm(TR[T2].perm, m2);
            for (int w = 0; w < NW; w++) {
                int c = pc128(b3 & WIN[w]);
                if (c == 5) { typeB = true; break; }
                if (c == 4) {
                    u128 miss = WIN[w] & ~b3;
                    int pre = TR[T2].inv[ctz128(miss)];
                    if (!(o2 & bit128(pre))) {
                        u128 pb = bit128(pre);
                        if (!(Mset & pb)) { Mset |= pb; nM++; }
                    }
                }
            }
            if (nM >= 2 && minSurv <= 2) break;   // cannot change the min
        }
        int surv = typeB ? 3 : min(2, nM);
        if (surv < minSurv) minSurv = surv;
        if (minSurv == 0) break;
    }
    return minSurv;
}

// ---------------------------------------------------------------------------
// Top-M most offensive replies (T,e in post-T space) of `mover` in the state
// given by boards sb[4] / occupancy socc. Pure offense ranking (own windows).
// ---------------------------------------------------------------------------
struct Reply { int T, e; long long sc; };

static void topOffensiveReplies(const u128 sb[4], u128 socc, int mover, int M,
                                vector<Reply> &out) {
    int NT = (int)TR.size(), NW = (int)WIN.size();
    out.clear();
    static int cnt[200]; static bool contested[200];
    u128 others = socc ^ sb[mover];
    for (int T = 0; T < NT; T++) {
        u128 mv = applyPerm(TR[T].perm, sb[mover]);
        u128 ot = applyPerm(TR[T].perm, others);
        long long base = 0;
        for (int w = 0; w < NW; w++) {
            cnt[w] = pc128(mv & WIN[w]);
            contested[w] = (ot & WIN[w]) != 0;
            if (!contested[w]) base += SMY[cnt[w]];
        }
        u128 empt = ~(mv | ot);
        if (NC < 128) empt &= (((u128)1) << NC) - 1;
        while (empt) {
            int e = ctz128(empt);
            empt &= empt - 1;
            long long sc = base + POS[e];
            for (uint16_t w : CWIN[e])
                if (!contested[w]) sc += SMY[cnt[w] + 1] - SMY[cnt[w]];
            out.push_back({T, e, sc});
        }
    }
    if ((int)out.size() > M) {
        partial_sort(out.begin(), out.begin() + M, out.end(),
                     [](const Reply &a, const Reply &b) { return a.sc > b.sc; });
        out.resize(M);
    } else {
        sort(out.begin(), out.end(),
             [](const Reply &a, const Reply &b) { return a.sc > b.sc; });
    }
}


// ===========================================================================
// Exact port of BossGold's decision policy. Because the target boss is
// deterministic, Nemesis can PREDICT its reply exactly in 2-player games and
// search 3 plies deep against the real policy instead of the paranoid worst
// case. Generic strength (safety / forced-win / doom) is kept for everything
// else, so this layer only re-ranks already-safe candidates.
// ===========================================================================
static const int BS_MY[6] = {0, 2, 10, 46, 230, 1000000};
static const int BS_OP[6] = {0, 2, 12, 58, 320, 1000000};
static vector<int> POSB;          // BossGold's positional table

static void initBossTables() {
    POSB.assign(NC, 0);
    int mid = S / 2;
    for (int y = 0; y < S; y++)
        for (int x = 0; x < S; x++) {
            int c = y * S + x;
            if (x % 3 == 1 && y % 3 == 1) POSB[c] += 7;
            POSB[c] += 4 - (abs(x - mid) + abs(y - mid)) / 2;
        }
}

// returns {T, e} in post-transform space, or {-1,-1}
static pair<int,int> bossReply(const u128 inBB[4], u128 inOcc, int bossId) {
    int NT = (int)TR.size(), NW = (int)WIN.size();
    if (pc128(inOcc) == NC) return {-1, -1};

    int opps[3], nOpp = 0;
    for (int p = 0; p < 4; p++)
        if (p != bossId && inBB[p]) opps[nOpp++] = p;

    struct BC { int T, e; long long ev; };
    vector<BC> safeC; safeC.reserve(NT * 8);
    long long bestUK = LLONG_MIN; int uT = -1, uE = -1;
    int shT = -1, shE = -1, clT = -1, clE = -1;
    vector<u128> tbMe(NT), toccA(NT);
    static int myC[200], oppN[200], contrib[200];

    for (int T = 0; T < NT && clT < 0; T++) {
        u128 tb[4], tocc = 0;
        for (int p = 0; p < 4; p++) {
            tb[p] = inBB[p] ? applyPerm(TR[T].perm, inBB[p]) : 0;
            tocc |= tb[p];
        }
        tbMe[T] = tb[bossId]; toccA[T] = tocc;
        bool oppLineT = false, myFive = false;
        u128 my4 = 0;
        long long base = 0;
        for (int w = 0; w < NW; w++) {
            u128 wm = WIN[w];
            int cm = pc128(tb[bossId] & wm);
            myC[w] = cm;
            int on = 0, oc = 0;
            for (int k = 0; k < nOpp; k++) {
                int c = pc128(tb[opps[k]] & wm);
                if (c) { on++; oc = c; if (c == 5) oppLineT = true; }
            }
            oppN[w] = on;
            int ct;
            if (cm && on) ct = 0;
            else if (cm) ct = BS_MY[cm];
            else if (on == 1) ct = -BS_OP[oc];
            else ct = 0;
            contrib[w] = ct; base += ct;
            if (cm == 5) myFive = true;
            else if (cm == 4) {
                u128 miss = wm & ~tb[bossId];
                if (!(miss & tocc)) my4 |= miss;
            }
        }
        bool uncond[4] = {false,false,false,false};
        u128 blockM[4] = {0,0,0,0};
        for (int k = 0; k < nOpp; k++) {
            int o = opps[k];
            if (pc128(inBB[o]) < 4) continue;
            for (int T2 = 0; T2 < NT && !uncond[o]; T2++) {
                u128 b2 = applyPerm(TR[T2].perm, tb[o]);
                for (int w = 0; w < NW; w++) {
                    int c = pc128(b2 & WIN[w]);
                    if (c == 5) { uncond[o] = true; break; }
                    if (c == 4) {
                        u128 miss = WIN[w] & ~b2;
                        int pre = TR[T2].inv[ctz128(miss)];
                        if (!(tocc & bit128(pre))) blockM[o] |= bit128(pre);
                    }
                }
            }
        }
        u128 empties = ~tocc;
        if (NC < 128) empties &= (((u128)1) << NC) - 1;
        while (empties) {
            int e = ctz128(empties);
            empties &= empties - 1;
            bool win = myFive || (my4 & bit128(e));
            if (win) {
                if (!oppLineT) { clT = T; clE = e; break; }
                if (shT < 0) { shT = T; shE = e; }
                continue;
            }
            int danger = oppLineT ? 100 : 0;
            for (int k = 0; k < nOpp; k++) {
                int o = opps[k];
                if (uncond[o]) danger++;
                else if (blockM[o] & ~bit128(e)) danger++;
            }
            long long ev = base + POSB[e];
            for (uint16_t w : CWIN[e]) {
                int nw = oppN[w] ? 0 : BS_MY[myC[w] + 1];
                ev += nw - contrib[w];
            }
            if (danger == 0) safeC.push_back({T, e, ev});
            else {
                long long key = -(long long)danger * 10000000LL + ev;
                if (key > bestUK) { bestUK = key; uT = T; uE = e; }
            }
        }
    }

    int outT = -1, outE = -1;
    if (clT >= 0) { outT = clT; outE = clE; }
    else if (!safeC.empty()) {
        sort(safeC.begin(), safeC.end(),
             [](const BC &a, const BC &b) { return a.ev > b.ev; });
        int K = min((int)safeC.size(), 48);
        long long bestKey = LLONG_MIN;
        for (int i = 0; i < K; i++) {
            BC &cd = safeC[i];
            u128 m1 = tbMe[cd.T] | bit128(cd.e);
            u128 o1 = toccA[cd.T] | bit128(cd.e);
            u128 winCells = 0; int lines = 0;
            for (int T2 = 0; T2 < NT; T2++) {
                u128 b2 = applyPerm(TR[T2].perm, m1);
                for (int w = 0; w < NW; w++) {
                    int c = pc128(b2 & WIN[w]);
                    if (c == 5) lines++;
                    else if (c == 4) {
                        u128 miss = WIN[w] & ~b2;
                        int pre = TR[T2].inv[ctz128(miss)];
                        if (!(o1 & bit128(pre))) winCells |= bit128(pre);
                    }
                }
            }
            int distinct = pc128(winCells);
            long long bonus = (distinct >= 2 ? 650 : distinct == 1 ? 170 : 0)
                              + (long long)min(lines, 3) * 130;
            long long key = cd.ev + bonus;
            if (key > bestKey) { bestKey = key; outT = cd.T; outE = cd.e; }
        }
        if (outT < 0) { outT = safeC[0].T; outE = safeC[0].e; }
    }
    else if (shT >= 0) { outT = shT; outE = shE; }
    else if (uT >= 0) { outT = uT; outE = uE; }
    return {outT, outE};
}


// static leaf evaluation of a state from `me` perspective
static long long leafEval(const u128 rb[4], u128 rocc, int me) {
    int NW = (int)WIN.size();
    long long sc = 0;
    for (int w = 0; w < NW; w++) {
        u128 wm = WIN[w];
        int cm = pc128(rb[me] & wm);
        int on = 0, oc = 0;
        for (int p = 0; p < 4; p++) {
            if (p == me || !rb[p]) continue;
            int c = pc128(rb[p] & wm);
            if (c) { on++; oc = c; }
        }
        if (cm && on) continue;
        if (cm) sc += SMY[cm];
        else if (on == 1) sc -= SOP[oc];
    }
    return sc;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int playerCount;
    if (!(cin >> playerCount)) return 0;
    int myId;
    if (!(cin >> myId)) return 0;
    int maxIdSeen = 0;
    int size;

    while (cin >> size) {
        auto t0 = chrono::steady_clock::now();
        auto elapsedMs = [&]() {
            return (long long)chrono::duration_cast<chrono::milliseconds>(
                       chrono::steady_clock::now() - t0).count();
        };

        if (S != size) { initTables(size, size == 9); initBossTables(); }

        u128 bb[4] = {0, 0, 0, 0}, occ = 0;
        int totalPieces = 0;
        for (int y = 0; y < S; y++) {
            string row; cin >> row;
            for (int x = 0; x < S; x++) {
                char ch = row[x];
                if (ch == '.') continue;
                int p = ch - '0';
                bb[p] |= bit128(y * S + x);
                occ |= bit128(y * S + x);
                totalPieces++;
                if (p > maxIdSeen) maxIdSeen = p;
            }
        }

        if (myId < 0) myId = min(totalPieces, 3);
        if (myId > maxIdSeen) maxIdSeen = myId;

        int opps[3], nOpp = 0;
        for (int p = 0; p < 4; p++)
            if (p != myId && bb[p]) opps[nOpp++] = p;
        int P = maxIdSeen + 1;
        int oppOrder[3]; int nOppOrder = 0;
        for (int d = 1; d < P; d++) {
            int o = (myId + d) % P;
            if (o != myId && bb[o]) oppOrder[nOppOrder++] = o;
        }

        int NT = (int)TR.size();
        int NW = (int)WIN.size();

        if (pc128(occ) == NC) { cout << "0 0 0 R" << endl; continue; }

        struct Cand { int T, e; long long ev; };
        vector<Cand> safeCands;   safeCands.reserve(NT * 8);
        long long bestUnsafeKey = LLONG_MIN; int unsafeT = -1, unsafeE = -1;
        int sharedT = -1, sharedE = -1;
        int cleanT = -1, cleanE = -1;

        vector<u128> tbAll(NT * 4), toccArr(NT);
        static int myC[200], oppN[200], contrib[200];

        for (int T = 0; T < NT && cleanT < 0; T++) {
            const Transform &tr = TR[T];
            u128 tb[4], tocc = 0;
            for (int p = 0; p < 4; p++) {
                tb[p] = bb[p] ? applyPerm(tr.perm, bb[p]) : 0;
                tocc |= tb[p];
                tbAll[T * 4 + p] = tb[p];
            }
            toccArr[T] = tocc;

            bool oppLineT = false, myFive = false;
            u128 my4 = 0;
            long long base = 0;

            for (int w = 0; w < NW; w++) {
                u128 wm = WIN[w];
                int cm = pc128(tb[myId] & wm);
                myC[w] = cm;
                int on = 0, ocnt = 0;
                for (int k = 0; k < nOpp; k++) {
                    int c = pc128(tb[opps[k]] & wm);
                    if (c) {
                        on++; ocnt = c;
                        if (c == 5) oppLineT = true;
                    }
                }
                oppN[w] = on;
                int ct;
                if (cm && on) ct = 0;
                else if (cm) ct = SMY[cm];
                else if (on == 1) ct = -SOP[ocnt];
                else ct = 0;
                contrib[w] = ct;
                base += ct;

                if (cm == 5) myFive = true;
                else if (cm == 4) {
                    u128 miss = wm & ~tb[myId];
                    if (!(miss & tocc)) my4 |= miss;
                }
            }

            bool uncond[4] = {false, false, false, false};
            u128 blockMask[4] = {0, 0, 0, 0};
            for (int k = 0; k < nOpp; k++) {
                int o = opps[k];
                if (pc128(bb[o]) < 4) continue;
                for (int T2 = 0; T2 < NT && !uncond[o]; T2++) {
                    u128 b2 = applyPerm(TR[T2].perm, tb[o]);
                    for (int w = 0; w < NW; w++) {
                        int c = pc128(b2 & WIN[w]);
                        if (c == 5) { uncond[o] = true; break; }
                        if (c == 4) {
                            u128 miss = WIN[w] & ~b2;
                            int pre = TR[T2].inv[ctz128(miss)];
                            if (!(tocc & bit128(pre)))
                                blockMask[o] |= bit128(pre);
                        }
                    }
                }
            }

            u128 empties = ~tocc;
            if (NC < 128) empties &= (((u128)1) << NC) - 1;
            while (empties) {
                int e = ctz128(empties);
                empties &= empties - 1;

                bool win = myFive || (my4 & bit128(e));
                if (win) {
                    if (!oppLineT) { cleanT = T; cleanE = e; break; }
                    if (sharedT < 0) { sharedT = T; sharedE = e; }
                    continue;
                }

                long long danger = oppLineT ? 1000000 : 0;
                for (int k = 0; k < nOpp; k++) {
                    int o = opps[k];
                    if (uncond[o]) danger += 1000;
                    else danger += pc128(blockMask[o] & ~bit128(e));
                }

                long long ev = base + POS[e];
                for (uint16_t w : CWIN[e]) {
                    int nw = oppN[w] ? 0 : SMY[myC[w] + 1];
                    ev += nw - contrib[w];
                }

                if (danger == 0) {
                    safeCands.push_back({T, e, ev});
                } else {
                    long long key = -danger * 10000000LL + ev;
                    if (key > bestUnsafeKey) {
                        bestUnsafeKey = key; unsafeT = T; unsafeE = e;
                    }
                }
            }
        }

        // ------------------------------------------------------------------
        // Decide
        // ------------------------------------------------------------------
        int outT = -1, outE = -1;
        long long bestFinalKey = LLONG_MIN;

        if (cleanT >= 0) { outT = cleanT; outE = cleanE; }
        else if (!safeCands.empty()) {
            sort(safeCands.begin(), safeCands.end(),
                 [](const Cand &a, const Cand &b) { return a.ev > b.ev; });

            int NCand = (int)safeCands.size();

            // ---- Phase F: exact forced-win scan ---------------------------
            vector<int> surv(NCand, -1);
            int forcedIdx = -1;
            for (int i = 0; i < min(NCand, 220); i++) {
                if (elapsedMs() > 20) break;
                Cand &cd = safeCands[i];
                u128 m1 = tbAll[cd.T * 4 + myId] | bit128(cd.e);
                u128 o1 = toccArr[cd.T] | bit128(cd.e);
                surv[i] = robustness(m1, o1);
                if (surv[i] >= 2) { forcedIdx = i; break; }
            }
            if (forcedIdx >= 0) {
                outT = safeCands[forcedIdx].T; outE = safeCands[forcedIdx].e;
            } else if (P == 2 && myId == 0) {
                // Tempo leader in a duel: mirror stats show the aggressive
                // racing policy wins ~90% with the first move. Play it
                // exactly; Phase F above still upgrades to proven wins.
                auto rp = bossReply(bb, occ, myId);
                if (rp.first >= 0) { outT = rp.first; outE = rp.second; }
                else { outT = safeCands[0].T; outE = safeCands[0].e; }
            } else {
                // ---- Phase D: doom avoidance, time-budgeted expansion -----
                // Keep scanning candidates until we find one the opponent
                // cannot answer with a proven forced win. Capping this scan
                // at a fixed depth loses games: when the opponent has a big
                // structure (e.g. the center diagonal), only low-eval
                // CONTESTING moves survive, and they rank far down the list.
                vector<Reply> reps;
                bool haveClean = false;
                for (int i = 0; i < NCand; i++) {
                    if (elapsedMs() > 34 && (haveClean || i >= 10)) break;
                    Cand &cd = safeCands[i];
                    long long key = cd.ev + (surv[i] > 0 ? surv[i] * 900 : 0);
                    // remaining candidates cannot beat the current best
                    if (haveClean && cd.ev + 1500 < bestFinalKey) break;
                    bool doomed = false;
                    if (nOppOrder == 1 && P == 2 && elapsedMs() < 32) {
                        // 2-player: predict BossGold's EXACT reply and score
                        // the true resulting position (3-ply search against
                        // the actual deterministic policy).
                        int oppo = oppOrder[0];
                        u128 sb[4];
                        for (int p = 0; p < 4; p++) sb[p] = tbAll[cd.T * 4 + p];
                        sb[myId] |= bit128(cd.e);
                        u128 socc = toccArr[cd.T] | bit128(cd.e);
                        auto rp = bossReply(sb, socc, oppo);
                        if (rp.first >= 0) {
                            u128 rb[4], rOcc = 0;
                            for (int p = 0; p < 4; p++) {
                                rb[p] = sb[p] ? applyPerm(TR[rp.first].perm, sb[p]) : 0;
                                rOcc |= rb[p];
                            }
                            rb[oppo] |= bit128(rp.second);
                            rOcc |= bit128(rp.second);
                            bool oppWon = false;
                            for (int w = 0; w < NW && !oppWon; w++)
                                if (pc128(rb[oppo] & WIN[w]) == 5) oppWon = true;
                            if (oppWon) { doomed = true; key -= 250000; }
                            else if (hasImmediateWin(rb[myId], rOcc)) {
                                key += 400000;      // boss reply fails: I win
                               
                            } else {
                                int s2 = robustness(rb[myId], rOcc);
                                if (s2 >= 2) key += 350000;
                                else {
                                    key += (long long)s2 * 900;
                                    int so = robustness(rb[oppo], rOcc);
                                    if (so >= 2) { doomed = true; key -= 200000; }
                                }
                            }
                        }
                    } else
                    for (int ko = 0; ko < min(nOppOrder, 1) && !doomed; ko++) {
                        int oppo = oppOrder[ko];
                        if (elapsedMs() >= 36) break;
                        u128 sb[4];
                        for (int p = 0; p < 4; p++) sb[p] = tbAll[cd.T * 4 + p];
                        sb[myId] |= bit128(cd.e);
                        u128 socc = toccArr[cd.T] | bit128(cd.e);
                        topOffensiveReplies(sb, socc, oppo, 12, reps);
                        for (auto &r : reps) {
                            if (elapsedMs() > 37) break;
                            u128 rOpp = applyPerm(TR[r.T].perm, sb[oppo]) |
                                        bit128(r.e);
                            u128 rMe  = applyPerm(TR[r.T].perm, sb[myId]);
                            u128 rOcc = applyPerm(TR[r.T].perm, socc) |
                                        bit128(r.e);
                            if (robustness(rOpp, rOcc) >= 2 &&
                                !hasImmediateWin(rMe, rOcc)) {
                                doomed = true;
                                key -= 200000;      // doomed candidate
                                break;
                            }
                        }
                    }
                    if (!doomed) haveClean = true;
                    if (key > bestFinalKey) {
                        bestFinalKey = key; outT = cd.T; outE = cd.e;
                    }
                }
                if (outT < 0) { outT = safeCands[0].T; outE = safeCands[0].e; }
                // every inspected line is doomed: a shared draw beats a loss
                if (bestFinalKey < -100000 && sharedT >= 0) {
                    outT = sharedT; outE = sharedE;
                }
            }
        }
        else if (sharedT >= 0) { outT = sharedT; outE = sharedE; }
        else if (unsafeT >= 0) { outT = unsafeT; outE = unsafeE; }

        if (outT < 0) {
            int e = ctz128(~occ & ((NC < 128) ? (((u128)1) << NC) - 1 : ~(u128)0));
            cout << (e % S) << " " << (e / S) << " " << TR[0].suffix << endl;
            continue;
        }

        int pre = TR[outT].inv[outE];
        cout << (pre % S) << " " << (pre / S) << " " << TR[outT].suffix << endl;
    }
    return 0;
}
