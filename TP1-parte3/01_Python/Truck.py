from multiprocessing import Process, Semaphore, Value, Lock
import time
import argparse
import random

LOADING_TIME = 2
UNLOADING_TIME = 2
REFUEL_TIME = 1


def truck(
    truck_id,
    flour_zone_tapiales,
    coal_zone_tapiales,
    flour_zone_fernandez,
    coal_zone_fernandez,
    tapiales_plant,
    fernandez_plant,
    gas_station,
    time_lock,
    trip_lock,
    remaining_trips,
    total_time,
    total_trips,
):
    while True:

        with trip_lock:
            if remaining_trips.value <= 0:
                break
            remaining_trips.value -= 1
            trip_number = total_trips - remaining_trips.value
            print(f"Camión {truck_id}: realizando viaje #{trip_number}")


        tapiales_plant.acquire()
        flour_zone_tapiales.acquire()
        print(f"Camión {truck_id}: cargando harina en Tapiales...")
        time.sleep(LOADING_TIME)
        flour_zone_tapiales.release()
        tapiales_plant.release()


        travel_outbound = random.randint(18, 24)
        print(f"Camión {truck_id}: viajando a Fernández por {travel_outbound} hs")
        time.sleep(travel_outbound / 10)


        fernandez_plant.acquire()
        coal_zone_fernandez.acquire()
        print(f"Camión {truck_id}: descargando harina en Fernández...")
        time.sleep(UNLOADING_TIME)
        coal_zone_fernandez.release()

        flour_zone_fernandez.acquire()
        print(f"Camión {truck_id}: cargando carbón en Fernández...")
        time.sleep(LOADING_TIME)
        flour_zone_fernandez.release()

        gas_station.acquire()
        print(f"Camión {truck_id}: cargando combustible...")
        time.sleep(REFUEL_TIME)
        gas_station.release()

        fernandez_plant.release()


        travel_return = random.randint(18, 24)
        print(f"Camión {truck_id}: viajando de vuelta a Tapiales por {travel_return} hs")
        time.sleep(travel_return / 10)


        tapiales_plant.acquire()
        coal_zone_tapiales.acquire()
        print(f"Camión {truck_id}: descargando carbón en Tapiales...")
        time.sleep(UNLOADING_TIME)
        coal_zone_tapiales.release()
        tapiales_plant.release()


        with time_lock:
            total_time.value += (
                travel_outbound
                + travel_return
                + LOADING_TIME * 2
                + UNLOADING_TIME * 2
                + REFUEL_TIME
            )

    print(f"Camión {truck_id}: terminó sus viajes.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Simulación de camiones de carga")
    parser.add_argument("trucks", type=int, help="Cantidad de camiones")
    parser.add_argument("trips", type=int, help="Cantidad total de viajes")
    args = parser.parse_args()

    num_trucks = args.trucks
    total_trips = args.trips


    flour_zone_tapiales = Semaphore(1)
    coal_zone_tapiales = Semaphore(1)
    flour_zone_fernandez = Semaphore(1)
    coal_zone_fernandez = Semaphore(1)

    tapiales_plant = Semaphore(2)
    fernandez_plant = Semaphore(2)
    gas_station = Semaphore(2)


    total_time = Value("i", 0)
    remaining_trips = Value("i", total_trips)
    time_lock = Lock()
    trip_lock = Lock()


    trucks = [
        Process(
            target=truck,
            args=(
                i,
                flour_zone_tapiales,
                coal_zone_tapiales,
                flour_zone_fernandez,
                coal_zone_fernandez,
                tapiales_plant,
                fernandez_plant,
                gas_station,
                time_lock,
                trip_lock,
                remaining_trips,
                total_time,
                total_trips,
            ),
        )
        for i in range(num_trucks)
    ]

    for t in trucks:
        t.start()
    for t in trucks:
        t.join()

    print("\n--- SIMULACIÓN COMPLETA ---")
    print(f"Tiempo total estimado (en horas): {total_time.value}")
    print(f"Tiempo total estimado (en días): {total_time.value / 24:.2f}")