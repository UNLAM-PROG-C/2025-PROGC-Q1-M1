#include <iostream>
#include <thread>
#include <mutex>
#include <vector>
#include <chrono>
#include <random>

using namespace std;

#define MAX_DOUGH_ON_TABLE 20
#define MAX_BREAD_IN_BASKET 50
#define MAX_PACKAGES_ON_COUNTER 30
#define INITIAL_LABELERS_AVAILABLE 2
#define MAESTRO_PRODUCES 2
#define ASSISTANT_PRODUCES 1
#define BAKER_CONSUMES 5
#define PACKER_CONSUMES 3
#define MIN_PACKAGES_TO_BUY 1
#define MAX_PACKAGES_TO_BUY 3
#define NUMBER_OF_CLIENTS 10

int doughOnTable = 0;
mutex doughMutex;

int breadInBasket = 0;
mutex basketMutex;

int packagesOnCounter = 0;
mutex counterMutex;

int labelersAvailable = INITIAL_LABELERS_AVAILABLE;
mutex labelerMutex;

int packagesSold = 0;
mutex salesMutex;

random_device rd;
mt19937 gen(rd());
uniform_int_distribution<> distClients(MIN_PACKAGES_TO_BUY, MAX_PACKAGES_TO_BUY);

void masterBaker() {
    while (true) {
        doughMutex.lock();
        if (doughOnTable <= MAX_DOUGH_ON_TABLE - MAESTRO_PRODUCES) {
            doughOnTable += MAESTRO_PRODUCES;
        }
        doughMutex.unlock();
    }
}

void assistantBaker() {
    while (true) {
        doughMutex.lock();
        if (doughOnTable < MAX_DOUGH_ON_TABLE) {
            doughOnTable += ASSISTANT_PRODUCES;
        }
        doughMutex.unlock();
    }
}

void baker() {
    while (true) {
        doughMutex.lock();
        basketMutex.lock();
        if (doughOnTable >= BAKER_CONSUMES && breadInBasket <= MAX_BREAD_IN_BASKET - BAKER_CONSUMES) {
            doughOnTable -= BAKER_CONSUMES;
            breadInBasket += BAKER_CONSUMES;
        }
        basketMutex.unlock();
        doughMutex.unlock();
    }
}

void packer() {
    while (true) {
        basketMutex.lock();
        counterMutex.lock();

        if (breadInBasket >= PACKER_CONSUMES && packagesOnCounter < MAX_PACKAGES_ON_COUNTER) {
            breadInBasket -= PACKER_CONSUMES;
            packagesOnCounter++;

            counterMutex.unlock();
            basketMutex.unlock();

            labelerMutex.lock();

            if (labelersAvailable > 0) {
                labelersAvailable--;
                labelerMutex.unlock();

                labelerMutex.lock();
                labelersAvailable++;
                labelerMutex.unlock();
            } else {
                labelerMutex.unlock();
            }
        } else {
            counterMutex.unlock();
            basketMutex.unlock();
        }
    }
}

void client() {
    int packagesToBuy = distClients(gen);
    int packagesBought = 0;
    while (packagesBought < packagesToBuy) {
        counterMutex.lock();
        if (packagesOnCounter > 0) {
            packagesOnCounter--;
            counterMutex.unlock();

            packagesBought++;

            salesMutex.lock();
            packagesSold++;
            salesMutex.unlock();
        } else {
            counterMutex.unlock();
        }
    }
}

int main() {
    thread t1(masterBaker);
    thread t2(assistantBaker);
    thread t3(baker);
    thread t4(packer);
    thread t5(packer);

    vector<thread> clients;
    for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
        clients.emplace_back(client);
    }

    for (auto& c : clients) {
        c.join();
    }

    cout << "\nTOTAL PAQUETES VENDIDOS: ";
    cout << packagesSold << endl;

    return EXIT_SUCCESS;;
}
