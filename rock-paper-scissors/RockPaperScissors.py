import  ccm
from ccm.lib.actr import *
import pickle
import sys

arguments = sys.argv
run_number = arguments[1]
utility = arguments[2]

game_rounds = 10
model_trials = 10
#utility = "PMPGCMixedWeighted"
#run_number = 1
utility_fn = PMQLearn(alpha=0.1,gamma=1)

if(utility == "PMNew"):
    utility_fn = PMNew(alpha=0.2)

if(utility == "PMQLearn"):
    utility_fn = PMQLearn(alpha=0.1,gamma=1)#alpha=0.1,gamma=1)
    #alpha = learning rate (between 0-1)

if(utility == "PMPGC"):
    utility_fn = PMPGC(goal=20)

if(utility == "PMPGCSuccessWeighted"):
    utility_fn = PMPGCSuccessWeighted(goal=20)

if(utility == "PMPGCMixedWeighted"):
    utility_fn = PMPGCMixedWeighted(goal=20)



class OpponentScreen(ccm.Model):
    rock = ccm.Model(isa='word', type='rock', salience= .1)
    paper = ccm.Model(isa='word', type='paper', salience= 10)
    scissors = ccm.Model(isa='word', type='scissors', salience= 10)

class OpponentScreen_switch(ccm.Model):

    scissors = ccm.Model(isa='word', type='scissors', salience= 10)
    #update Rock
    rock = ccm.Model(isa='word', type='rock', salience= 10)
    #update paper
    paper = ccm.Model(isa='word', type='paper', salience= .1)

class RockPaperScissors(ACTR):

    #goal buffer
    goal = Buffer()
    retrieve = Buffer()
    memory = Memory(retrieve)
    data = []

    #vision buffer and vision module
    visual_buffer = Buffer()
    location = Buffer()
    vision_module = SOSVision(visual_buffer)

    #working memory buffer
    imaginal = Buffer()

    pm = utility_fn
    utility_name = utility
    run_number = run_number

    def init():
        for i in range(1000):
            memory.add('count {} {}'.format(i, i+1))


    #start of a new round
    def start(goal = 'action:start count:?x'):
        self.print_production_utilities()
        print "New game:"
        goal.modify(action='move')




    #model plays rock
    def rock(goal= 'action:move count:?x', imaginal= 'my:none opponent:none'):
        print "Rock selected."
        imaginal.set('my:rock opponent:none')
        print "After imaginal changed"
        goal.modify(action= 'attendOpponent')


    #model plays paper
    def paper(goal= 'action:move count:?x', imaginal= 'my:none opponent:none'):
        print "Paper selected."
        imaginal.set('my:paper opponent:none')
        print "After imaginal changed"
        goal.modify(action= 'attendOpponent')


    #model plays scissors
    def scissors(goal= 'action:move count:?x', imaginal= 'my:none opponent:none'):
        print "Scissors selected."
        imaginal.set('my:scissors opponent:none')
        print "After imaginal changed"
        goal.modify(action= 'attendOpponent')


    #attend to screen to get opponent's move
    def attend(goal= 'action:attendOpponent count:?x', imaginal= 'my:!none opponent:none' ):
        print "Attending to opponent move."
        # print(vision_module._buffer.chunk.__repr__())
        #rock, paper, or scissors is put into visual_buffer
        vision_module.request('isa:?word')
        # print(vision_module._buffer.chunk)
        goal.modify(action= 'interpretOpponent')


    #opponent move (the word) is found, it is located in  visual_buffer
    def opponentFound(goal= 'action:interpretOpponent count:?x', imaginal= 'my:?move opponent:none', visual_buffer= 'isa:word type:?type'):
        print "Opponent found!"

        #store word(opponent move) in working memory then clear from visual buffer
        imaginal.set('my:?move opponent:?type')
        visual_buffer.clear

        #update goal to determine results
        goal.modify(action = 'results')


    #no visual cue found if visual_buffer is empty
    def opponentNotFound(goal= 'action:interpretOpponent count:?x', visual_buffer= 'isa:none type:none'):
        print "Opponent not found!"

        #run attend production to try again
        goal.modify(action= "attendOpponent")

#variability in matching with imaginal buffer?
    #win results, POSITIVE reward
    def s_p(goal= 'action:results count:?x', imaginal= 'my:scissors opponent:paper'):
        print "Opponent plays paper."
        print "Result: WIN"
        self.reward(0.1)
        goal.modify(action= 'newGame')
        self.data.append("S-P win")

    def r_s(goal= 'action:results count:?x', imaginal= 'my:rock opponent:scissors'):
        print "Opponent plays scissors."
        print "Result: WIN"
        self.reward(0.1)
        goal.modify(action= 'newGame')
        self.data.append("R-S win")

    def p_r(goal= 'action:results count:?x', imaginal= 'my:paper opponent:rock'):
        print "Opponent plays rock."
        print "Result: WIN"
        self.reward(0.1)
        goal.modify(action= 'newGame')
        self.data.append("P-R win")


    #loss results, NEGATIVE reward
    def r_p(goal= 'action:results count:?x', imaginal= 'my:rock opponent:paper'):
        print "Opponent plays paper."
        print "Result: LOSS"
        self.reward(-0.1)
        goal.modify(action= 'newGame')
        self.data.append("R-P loss")

    def p_s(goal= 'action:results count:?x', imaginal= 'my:paper opponent:scissors'):
        print "Opponent plays scissors."
        print "Result: LOSS"
        self.reward(-0.1)
        goal.modify(action= 'newGame')
        self.data.append("P-S loss")

    def s_r(goal= 'action:results count:?x', imaginal= 'my:scissors opponent:rock'):
        print "Opponent plays rock."
        print "Result: LOSS"
        self.reward(-0.1)
        goal.modify(action= 'newGame')
        self.data.append("S-R loss")


    #tie results, NO reward
    def r_r(goal= 'action:results count:?x', imaginal= 'my:rock opponent:rock'):
        print "Opponent plays rock."
        print "Result: TIE"
        self.reward(0)
        goal.modify(action= 'newGame')
        self.data.append("R-R tie")

    def p_p(goal= 'action:results count:?x', imaginal= 'my:paper opponent:paper'):
        print "Opponent plays paper."
        print "Result: TIE"
        self.reward(0)
        goal.modify(action= 'newGame')
        self.data.append("P-P tie")

    def s_s(goal= 'action:results count:?x', imaginal= 'my:scissors opponent:scissors'):
        print "Opponent plays scissors."
        print "Result: TIE"
        self.reward(0)
        goal.modify(action= 'newGame')
        self.data.append("S-S tie")

    #change max count in game_over AND update_count
    def game_over(goal= 'action:newGame count:1000'):
        import pickle
        print "Game Over"
        goal.modify(action= 'gameOver')
        print(self.data)
        with open('my_data_{}_{}.pkl'.format(utility_name, run_number), 'wb') as f:
            pickle.dump(self.data, f)

    #requests next number for count from memory
    def update_count(goal= 'action:newGame count:!1000?num1'):
        goal.modify(action= 'newRound')
        memory.request('count ?num1 ?num2')

    #CODE ADDED
    #after 500th round, change opponent strategy
    #switch from not playing rock to not playing paper
    def new_round_switch_opponent(goal= 'action:newRound count:500'):
        goal.modify(action= 'new env')

    def new_round(goal= 'action:newRound count:?x', retrieve= 'count ?num1 ?num2'):
        #print(retrieve._buffer.chunk)
        #update count from memory buffer
        goal.modify(count= num2)
        #reset necessary buffers
        retrieve.clear()
        imaginal.set('my:none opponent:none')
        goal.modify(action= 'start')

    def print_production_utilities(self):
        utilities_dict = self.get_activation()
        print "Current production utilities:"
        print "-----------------------------"
        for key, value in utilities_dict.items():
            print key, "-->", round(value, 3)
        print "\n"


# env=PairedExperiment()
# env.model=Paired()
# ccm.log_everything(env)
# env.model.goal.set('state:start word:None num:None')
# ccm.display(env)
# env.run()

env = OpponentScreen()
#env.model = RockPaperScissors()
env.agent = RockPaperScissors()
ccm.log_everything(env)
env.agent.goal.set('action:start count:1')
env.agent.imaginal.set('my:none opponent:none')
#ccm.display(env)
env.run()

env2 = OpponentScreen_switch()
#env2.agent = env.agent
env2.agent = RockPaperScissors()
env2.agent._adaptors = env.agent._adaptors
env2.agent.data = env.agent.data
ccm.log_everything(env2)
env2.agent.goal.set('action:start count:501')
env2.agent.imaginal.set('my:none opponent:none')
#ccm.display(env)
print "anything"
env2.run()
