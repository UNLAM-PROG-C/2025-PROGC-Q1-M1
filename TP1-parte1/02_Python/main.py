import os
import random
import time
import sys

DICE_MIN_VALUE = 1
DICE_MAX_VALUE = 6
MIN_RANDOM_VALUE = 0.1
MAX_RANDOM_VALUE = 0.3

PLAYER = 5
THROWS = 10

def player(id):
    sys.stdout.write(f"Jugador {id} entra al juego.\n")
    sys.stdout.flush()
    points = 0
    for i in range(THROWS):
        dice = random.randint(DICE_MIN_VALUE, DICE_MAX_VALUE)
        points += dice
        sys.stdout.write(f"Jugador {id} - Lanzamiento {i+1}: {dice}\n")
        sys.stdout.flush()
        time.sleep(random.uniform(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE))
    sys.stdout.write(f"Jugador {id} finaliza con {points} puntos.\n")
    sys.stdout.flush()

def main():
    children = [] #Creamos una lista para todos los procesos hijo

    for i in range(PLAYER):

        #Creamos un nuevo proceso hijo para cada jugador
        pid = os.fork()
        if pid == 0:
            # Es el proceso hijo
            player(i+1)
            os._exit(os.EX_OK)  # Finalizamos el proceso hijo
        else:
            # Es el proceso padre
            children.append(pid)

    # Mediante su pid, esperamos a que todos los hijos terminen
    for pid in children:
        os.waitpid(pid, 0)

    print("Todos los jugadores han terminado.")

if __name__ == "__main__":
    main()
