// ============================================================================
// Pentago XL - Gold League Boss
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

// ---------------------------------------------------------------------------
// Precomputed geometry
// ---------------------------------------------------------------------------
static int S = 0;      // board size (6 or 9)
static int B = 0;      // blocks per row (2 or 3)
static int NC = 0;     // number of cells
static bool SWAPS = false;

struct Transform {
    uint8_t perm[81];  // perm[src] = dst
    uint8_t inv[81];   // inv[dst]  = src
    string suffix;     // output suffix, e.g. "0 R" or "SWAP 0 1"
};

static vector<Transform> TR;
static vector<u128> WIN;                 // 5-in-a-row window masks
static vector<vector<uint16_t>> CWIN;    // windows containing each cell
static vector<int> POS;                  // positional bonus per cell

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

    // Rotations (referee semantics: R -> (i,j) to (j,2-i), L -> (i,j) to (2-j,i))
    for (int b = 0; b < B * B; b++) {
        int sx = (b % B) * 3, sy = (b / B) * 3;
        for (int d = 0; d < 2; d++) {           // d==0 : R, d==1 : L
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
    // Swaps of adjacent blocks
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
                t.suffix = to_string(b1) + " " + to_string(b2);
                TR.push_back(t);
            }
    }
    for (auto &t : TR)
        for (int c = 0; c < NC; c++) t.inv[t.perm[c]] = (uint8_t)c;

    // Windows (same enumeration as the referee)
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

    // Positional bonus: block centers are rotation-invariant, central area
    // participates in the most windows.
    POS.assign(NC, 0);
    int mid = S / 2;
    for (int y = 0; y < S; y++)
        for (int x = 0; x < S; x++) {
            int c = y * S + x;
            if (x % 3 == 1 && y % 3 == 1) POS[c] += 7;          // block center
            POS[c] += 4 - (abs(x - mid) + abs(y - mid)) / 2;    // centrality
        }
}

// ---------------------------------------------------------------------------
// Evaluation tables (index = number of own marbles in a 5-window)
// ---------------------------------------------------------------------------
static const int SMY[6] = {0, 2, 10, 46, 230, 1000000};
static const int SOP[6] = {0, 2, 12, 58, 320, 1000000};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int playerCount;
    if (!(cin >> playerCount)) return 0;
    int myId;
    if (!(cin >> myId)) return 0;
    int size;
    if (!(cin >> size)) return 0;

    string firstRow;
    while (cin >> firstRow) {
        auto t0 = chrono::steady_clock::now();
        auto elapsedMs = [&]() {
            return chrono::duration_cast<chrono::milliseconds>(
                       chrono::steady_clock::now() - t0).count();
        };

        if (S != size) initTables(size, false);

        u128 bb[4] = {0, 0, 0, 0}, occ = 0;
        int totalPieces = 0;
        for (int y = 0; y < S; y++) {
            string row = (y == 0) ? firstRow : "";
            if (y > 0) cin >> row;
            for (int x = 0; x < S; x++) {
                char ch = row[x];
                if (ch == '.') continue;
                int p = ch - '0';
                bb[p] |= bit128(y * S + x);
                occ |= bit128(y * S + x);
                totalPieces++;
            }
        }

        // Deduce own id on the first turn we ever see: with round-robin order
        // the number of marbles already placed equals our index.
        if (myId < 0) myId = min(totalPieces, 3);

        int opps[3], nOpp = 0;
        for (int p = 0; p < 4; p++)
            if (p != myId && bb[p]) opps[nOpp++] = p;

        int NT = (int)TR.size();
        int NW = (int)WIN.size();

        // Board full: game is over anyway, emit anything syntactically valid.
        if (pc128(occ) == NC) { cout << "0 0 0 R" << endl; continue; }

        // ------------------------------------------------------------------
        // Per-transform data
        // ------------------------------------------------------------------
        struct Cand { int T, e; long long ev; };
        vector<Cand> safeCands;   safeCands.reserve(NT * 8);
        long long bestUnsafeKey = LLONG_MIN; int unsafeT = -1, unsafeE = -1;
        int sharedT = -1, sharedE = -1;
        int cleanT = -1, cleanE = -1;

        vector<u128> tbMe(NT), toccArr(NT);

        // scratch buffers
        static int myC[200], oppN[200], contrib[200];
        static int oppSingleCnt[200];

        for (int T = 0; T < NT && cleanT < 0; T++) {
            const Transform &tr = TR[T];
            u128 tb[4], tocc = 0;
            for (int p = 0; p < 4; p++) {
                tb[p] = bb[p] ? applyPerm(tr.perm, bb[p]) : 0;
                tocc |= tb[p];
            }
            tbMe[T] = tb[myId];
            toccArr[T] = tocc;

            // --- windows: my wins, opponent lines, base eval ---------------
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
                oppSingleCnt[w] = (on == 1) ? ocnt : 0;
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

            // --- opponent immediate threats after (any placement, T) -------
            bool uncond[4] = {false, false, false, false};
            u128 blockMask[4] = {0, 0, 0, 0};
            bool anyUncond = false;
            for (int k = 0; k < nOpp; k++) {
                int o = opps[k];
                if (pc128(bb[o]) < 4) continue;
                for (int T2 = 0; T2 < NT && !uncond[o]; T2++) {
                    u128 b2 = applyPerm(TR[T2].perm, tb[o]);
                    for (int w = 0; w < NW; w++) {
                        u128 inter = b2 & WIN[w];
                        int c = pc128(inter);
                        if (c == 5) { uncond[o] = true; anyUncond = true; break; }
                        if (c == 4) {
                            u128 miss = WIN[w] & ~b2;
                            int pre = TR[T2].inv[ctz128(miss)];
                            if (!(tocc & bit128(pre)))
                                blockMask[o] |= bit128(pre);
                        }
                    }
                }
            }

            // --- iterate placements (post-transform space) ------------------
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

                // danger
                int danger = oppLineT ? 100 : 0;
                for (int k = 0; k < nOpp; k++) {
                    int o = opps[k];
                    if (uncond[o]) danger++;
                    else if (blockMask[o] & ~bit128(e)) danger++;
                }

                // eval delta for the placed marble
                long long ev = base + POS[e];
                for (uint16_t w : CWIN[e]) {
                    int nw;
                    if (oppN[w]) nw = 0;               // window now contested
                    else nw = SMY[myC[w] + 1];
                    ev += nw - contrib[w];
                }

                if (danger == 0) {
                    safeCands.push_back({T, e, ev});
                } else {
                    long long key = -(long long)danger * 10000000LL + ev;
                    if (key > bestUnsafeKey) {
                        bestUnsafeKey = key; unsafeT = T; unsafeE = e;
                    }
                }
                (void)anyUncond;
            }
        }

        // ------------------------------------------------------------------
        // Decide
        // ------------------------------------------------------------------
        int outT = -1, outE = -1;
        if (cleanT >= 0) { outT = cleanT; outE = cleanE; }
        else if (!safeCands.empty()) {
            // Phase B: re-rank top safe candidates by own next-turn threats.
            sort(safeCands.begin(), safeCands.end(),
                 [](const Cand &a, const Cand &b) { return a.ev > b.ev; });
            int K = min((int)safeCands.size(), 48);
            long long bestKey = LLONG_MIN;
            for (int i = 0; i < K; i++) {
                if (elapsedMs() > 32) { K = i; break; }
                Cand &cd = safeCands[i];
                u128 m1 = tbMe[cd.T] | bit128(cd.e);
                u128 o1 = toccArr[cd.T] | bit128(cd.e);
                u128 winCells = 0;
                int lines = 0;
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
                long long bonus =
                    (distinct >= 2 ? 650 : distinct == 1 ? 170 : 0) +
                    (long long)min(lines, 3) * 130;
                long long key = cd.ev + bonus;
                if (key > bestKey) { bestKey = key; outT = cd.T; outE = cd.e; }
            }
            if (outT < 0) { outT = safeCands[0].T; outE = safeCands[0].e; }
        }
        else if (sharedT >= 0) { outT = sharedT; outE = sharedE; }   // draw > loss
        else if (unsafeT >= 0) { outT = unsafeT; outE = unsafeE; }

        if (outT < 0) {
            // Should be unreachable; emit first legal move.
            int e = ctz128(~occ & ((NC < 128) ? (((u128)1) << NC) - 1 : ~(u128)0));
            cout << (e % S) << " " << (e / S) << " " << TR[0].suffix << endl;
            continue;
        }

        int pre = TR[outT].inv[outE];        // placement happens BEFORE transform
        cout << (pre % S) << " " << (pre / S) << " " << TR[outT].suffix << endl;
    }
    return 0;
}
