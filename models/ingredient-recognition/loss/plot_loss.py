import matplotlib.pyplot as plt


def plot_loss(training_loss):
    epoch_count = range(1, len(training_loss) + 1)

    plt.plot(epoch_count, training_loss, 'r')
    plt.legend(['Full Training Loss'])
    plt.xlabel('Epoch')
    plt.ylabel('Loss')
    plt.show()


history = [float(val.replace('[', '').replace(']', '')) for val in open('loss.txt', 'r').read().split(',')]
plot_loss(history)
