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


display_metrics("recipe_recommendation.txt")
display_metrics("ingredient_recognition.txt")

