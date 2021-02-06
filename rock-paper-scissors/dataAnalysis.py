import pickle
import numpy as np

def get_scores(file):
    with open(file, 'rb') as f:
        data = pickle.load(f)

    my_move = ['R', 'P', 'S']
    opponent_move = ['R', 'P', 'S']
    result = ['Win', 'Tie', 'Loss']

    moves = np.zeros((3, 3))
    results = [0, 0, 0]

    #new
    num_rounds = 0;
    score = 0;

    for item in data:
        # R-S win
        x = 1
        y = 1
        #keep track of self moves (x axis)
        if item[0] == 'R':
            x = 0
        elif item[0] == 'P':
            x = 1
        elif item[0] == 'S':
            x = 2

        #keep track of opponent moves (y axis)
        if item[2] == 'R':
            y = 0
        elif item[2] == 'P':
            y = 1
        elif item[2] == 'S':
            y = 2

        moves[x, y] += 1

        # Keep track of wins and ties and losses
        if item[4] == 'w':
            results[0] += 1

        elif item[4] == 't':
            results[1] += 1

        elif item[4] == 'l':
            results[2] += 1

        num_rounds += 1

    #create 2 arrays size num_rounds
    #1 filled incrementally
    time = np.arange(num_rounds)

    scores= np.zeros(num_rounds)

    #1 filled with score at the end of each round
    for i,item in enumerate (data):
        if item[4] == 'w':
            score += 1

        elif item[4] == 't':
            score += 0

        elif item[4] == 'l':
            score -= 1

        #after each iteration add score to scores
        scores[i] = score


    #CODE FOR OTHER GRAPHS
    # moves[0, 0] means I played rock, opponent played Rock
    # moves[1, 2] means I played paper, opponent played scissors
    print moves
    print results


    """import matplotlib.pyplot as plt
    # figure out how to add a title, labels,

    #plot results in bar chart
    y_pos = np.arange(len(result))
    plt.bar(y_pos, results, align='center', alpha=0.5)
    plt.xticks(y_pos, result)
    plt.ylabel('Count')
    plt.title('Rock Paper Scissors Results')
    plt.show()

    fig, ax = plt.subplots()
    im = ax.imshow(moves)
    #arrange self x-axis, opponent y-axis
    ax.set_xticks(np.arange(len(my_move)))
    ax.set_yticks(np.arange(len(opponent_move)))

    #label ticks
    ax.set_xticklabels(my_move)
    ax.set_yticklabels(opponent_move)

    #label axess
    plt.xlabel('Opponent Move')
    plt.ylabel('Self Move')
    #loop data dimensions, add text annotations
    for i in range(len(my_move)):
        for j in range(len(opponent_move)):
            text = ax.text(j, i, moves[i, j], ha="center", va="center", color="w")


    #set title
    ax.set_title("Rock Paper Scissors Self and Opponent Moves")
    fig.tight_layout()

    plt.show()

    #line graph
    #example: to plot x versus y
    #plt.plot([1, 2, 3, 4], [1, 4, 9, 16])
    plt.plot(time, scores)
    plt.show()
    """
    return scores


import matplotlib.pyplot as plt

#loop through pickle files
import glob
import sys

arguments = sys.argv
folder_name = arguments[1]
#dictionary
utility_scores = {}

for f in glob.glob(folder_name + "*.pkl"):
    print f
    name = f.split("_")[2]
    scores = get_scores(f)

    if name not in utility_scores.keys():
        utility_scores[name] = []

    utility_scores[name].append(scores)

for k,v in utility_scores.items():
    for item in v:
        plt.plot(item)

#averages
"""for k,v in utility_scores.items():
    v = np.array(v)
    averages = np.mean(v, axis=0)
    std_dev = np.std(v, axis=0)

    plt.plot(averages, label=k)
    plt.fill_between(np.arange(len(averages)), averages+std_dev, averages-std_dev, alpha=0.2)
"""

plt.title("Change in Score for Various Utility Algorithms")
plt.xlabel("Number of Rounds")
plt.ylabel("Total Score")
plt.legend(loc=0, prop={"size":12})
plt.show()
