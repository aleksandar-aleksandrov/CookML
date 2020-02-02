import matplotlib.pyplot as plt
import statistics


def load_metrics(path: str):
    metrics = []

    with open(path, "r") as f:
        line = f.readline()

        while line:
            metrics.append(int(line))
            line = f.readline()

    return metrics


def display_metrics(path: str):
    times = load_metrics(path)
    print(f'Mean {statistics.mean(times)}')

    times_count = range(1, len(times) + 1)

    plt.plot(times_count, times)
    plt.xlabel('Attempt')
    plt.ylabel('Execution time')
    plt.show()


def display_both_metrics():
    rec_times = load_metrics("recipe_recommendation.txt")
    ing_times = load_metrics("ingredient_recognition.txt")
    times_count = range(1, len(rec_times) + 1)

    plt.plot(times_count, rec_times)
    plt.plot(times_count, ing_times)
    plt.legend(['Recipe Recommendation', 'Ingredient Recognition'])
    plt.xlabel('Attempt')
    plt.ylabel('Execution time in ms')
    plt.show()


display_metrics("recipe_recommendation.txt")
display_metrics("ingredient_recognition.txt")

display_both_metrics()
