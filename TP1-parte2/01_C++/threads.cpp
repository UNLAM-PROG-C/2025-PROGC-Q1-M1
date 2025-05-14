#include <fstream>
#include <iostream>
#include <thread>
#include <vector>
#include <random>
#include <chrono>

using namespace std;

const int DURATION_MIN_MS = 100;
const int DURATION_MAX_MS = 200;
const double SUCCESS_PROBABILITY = 0.5;
const int PROBABILITY_RANGE = 100;
const int CHAKRA_MIN = 5;
const int CHAKRA_MAX = 10;

struct FinalResult {
    int clones;
    double duration;
    int totalLevel;
};

int trainClone(int id_clon) {
    int chakra = CHAKRA_MIN + rand() % (CHAKRA_MAX - CHAKRA_MIN + 1);
    int level = 0;
    for (int i = 0; i < chakra; ++i) {
        int duration = DURATION_MIN_MS + rand() % (DURATION_MAX_MS - DURATION_MIN_MS + 1);
        this_thread::sleep_for(chrono::milliseconds(duration));
        if (rand() % PROBABILITY_RANGE < SUCCESS_PROBABILITY * PROBABILITY_RANGE) {
            level++;
        }
    }
    return level;
}

void LaunchThreads(int numClones, vector<int>& levels) {
    vector<thread> clones;
    for (int i = 0; i < numClones; ++i) {
        clones.emplace_back([i, &levels]() {
            levels[i] = trainClone(i);
        });
    }

    for (auto& clone : clones) {
        clone.join();
    }
}

int CalculateTotalLevel(const vector<int>& levels) {
    int totalLevel = 0;
    for (int level : levels) {
        totalLevel += level;
    }
    return totalLevel;
}

void PrintResults(int numClones, const vector<int>& levels, int totalLevel, double duration) {
    for (int i = 0; i < numClones; ++i) {
        cout << "El clon " << i + 1 << " alcanzó el nivel " << levels[i] << endl;
    }
    cout << "Nivel total alcanzado: " << totalLevel << endl;
    cout << "Tiempo total: " << duration << " segundos" << endl;
    cout << "------------------------------------------" << endl;
}

void SaveSummaryInCSV(const vector<FinalResult>& results, const string& fileName) {
    ofstream file(fileName);
    if (!file.is_open()) {
        cerr << "Error al abrir el archivo " << fileName << endl;
        return;
    }

    file << "Clones,Duracion,NivelTotal\n";
    for (const auto& res : results) {
        file << res.clones << "," << res.duration << "," << res.totalLevel << "\n";
    }

    file.close();
    cout << "Comparativa guardada en " << fileName << endl;
}

int main() {
    vector<int> cloneCounts = {5, 10, 20, 40, 60, 70};
    vector<FinalResult> summary;
    for (int numClones : cloneCounts) {
        cout << "\n--- Ejecutando con " << numClones << " clones ---" << endl;
        vector<int> levels(numClones, 0);
        auto startTime = chrono::high_resolution_clock::now();
        LaunchThreads(numClones, levels);
        auto endTime = chrono::high_resolution_clock::now();
        chrono::duration<double> duration = endTime - startTime;
        int totalLevel = CalculateTotalLevel(levels);

        PrintResults(numClones, levels, totalLevel, duration.count());

        summary.push_back({numClones, duration.count(), totalLevel});
    }

    SaveSummaryInCSV(summary, "resumen.csv");

    return 0;
}