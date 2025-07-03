import pandas as pd
import threading as th
import json


def main():

    input_file = 'visualizaciones.csv'
    output_file = 'preferencias.json'
    data_frame = pd.read_csv(input_file)
    preferences = []

    users = data_frame['user_id'].unique().tolist()
    threads = []

    for user in users:
        user_views = data_frame[data_frame['user_id'] == user]
        user_thread = th.Thread(target=get_preference, name=str(user), args=(user_views, preferences))
        user_thread.start()
        threads.append(user_thread)

    for thread in threads:
        thread.join()

    with open(output_file, "w") as f:
        json.dump(preferences, f, indent=4, ensure_ascii=False)

    print(f"El archivo {output_file} ha sido creado con éxito.")


def get_preference(user_views, preferences):

    user_preferences = {
        "user_id": str(user_views['user_id'].iloc[0]),
        "user_name": str(user_views['user_name'].iloc[0]),
        "chosen_type": str(get_preference_field(user_views, 'type')),
        "chosen_genre": str(get_preference_field(user_views, 'genre')),
        "total": int(user_views.shape[0]),
        "different_genres": int(user_views['genre'].unique().shape[0])
    }

    preferences.append(user_preferences)


def get_preference_field(user_views, field):

    return user_views[field].mode()[0]


if __name__ == "__main__":
    main()