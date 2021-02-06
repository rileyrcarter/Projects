#include "scrabble.h"

#include "formatting.h"
#include <iomanip>
#include <iostream>
#include <map>

using namespace std;

// Given to you. this does not need to be changed
Scrabble::Scrabble(const ScrabbleConfig& config)
        : hand_size(config.hand_size),
          minimum_word_length(config.minimum_word_length),
          tile_bag(TileBag::read(config.tile_bag_file_path, config.seed)),
          board(Board::read(config.board_file_path)),
          dictionary(Dictionary::read(config.dictionary_file_path)) {}

// Game Loop should cycle through players and get and execute that players move
// until the game is over.
void Scrabble::game_loop() {
    // TODO: implement this.

    bool players_have_tiles = true;
    bool not_all_passes = true;
    size_t pass_count = 0;

    // loop while all players still have tiles and all players did not pass
    while (players_have_tiles && not_all_passes) {
        // vector of shared pointers to Players
        // iterate through vector to do action for each player
        for (size_t i = 0; i < this->players.size(); i++) {
            // show current state of board
            // Board::print(ostream& out)
            this->board.print(std::cout);

            // show the tiles the player has
            cout << "Current scores: " << endl;

            // show current score of game
            // loop and print out name and current points of each player
            for (size_t j = 0; j < this->players.size(); j++) {
                cout << (this->players[j])->get_name() << ": " << (this->players[j])->get_points() << endl;
            }
            cout << endl;
            // BOTH OF THE FOLLOWING OCCUR IN GET_MOVE:
            // print player's hand
            // query player for their move

            // call get move for every player
            Move curr_move = (this->players[i])->get_move(this->board, this->dictionary);

            // check type of move
            // case 1: PASS
            if (curr_move.kind == MoveKind::PASS) {
                // increment pass count
                pass_count = pass_count + 1;

                cout << endl << "Turn was passed." << endl;

                cout << endl << "Press [enter] to continue.";
                std::cin.ignore();

                // check if all players passed, this would end loop
                if (pass_count == this->players.size()) {
                    not_all_passes = false;
                    // break out of loop
                    break;
                }

            }

            // case 2: EXCHANGE
            else if (curr_move.kind == MoveKind::EXCHANGE) {
                // can exchange anywhere from 1 to hand_size tiles
                // exception for this??
                pass_count = 0;

                // save number of tiles to exchange
                size_t exchange_num = curr_move.tiles.size();

                // first return tiles to tile bag
                // add each tile one at a time
                for (size_t i = 0; i < curr_move.tiles.size(); i++) {
                    // add to tile bag
                    this->tile_bag.add_tile(curr_move.tiles[i]);
                }

                // now remove from hand
                // remove from hand
                (this->players[i])->remove_tiles(curr_move.tiles);

                // then draw same number of tiles from bag
                std::vector<TileKind> new_tiles = (this->tile_bag).remove_random_tiles(exchange_num);

                // add these new tiles to player's hand
                (this->players[i])->add_tiles(new_tiles);

                // show new letters picked up
                cout << "New letters after exchange: ";
                for (size_t t = 0; t < new_tiles.size(); t++) {
                    cout << new_tiles[t].letter << " ";
                }
                cout << endl;

                cout << endl << "Press [enter] to continue.";
                std::cin.ignore();
            }

            // case 3: PLACE
            else if (curr_move.kind == MoveKind::PLACE) {
                pass_count = 0;

                // place if move is of type place
                PlaceResult place_result = this->board.place(curr_move);

                // after placing, remove tiles from player's hand
                (this->players[i])->remove_tiles(curr_move.tiles);

                // get tiles from bag to replace tiles in move
                std::vector<TileKind> replaced_tiles;
                replaced_tiles = this->tile_bag.remove_random_tiles(curr_move.tiles.size());

                // add these random tiles to player's hand
                (this->players[i])->add_tiles(replaced_tiles);

                // add points earned from placement
                (this->players[i])->add_points(place_result.points);

                // if player plays all tiles in hand, +50 points
                if (curr_move.tiles.size() == (this->players[i])->get_hand_size()) {
                    cout << "Recieved empty hand bonus: +" << EMPTY_HAND_BONUS << " points" << endl;
                    (this->players[i])->add_points(EMPTY_HAND_BONUS);
                }

                // show board after move placed
                this->board.print(std::cout);

                // show player results of move
                // show player points earned
                cout << "You gained " << SCORE_COLOR << place_result.points << rang::style::reset << " points!" << endl;

                // words formed
                cout << "Words formed:" << endl;
                for (size_t w = 0; w < place_result.words.size(); w++) {
                    cout << place_result.words[w] << endl;
                }

                // show current score
                cout << "Your current score: " << SCORE_COLOR << (this->players[i])->get_points() << rang::style::reset
                     << endl;

                // show new letters picked up
                cout << "New letters: ";
                for (size_t t = 0; t < replaced_tiles.size(); t++) {
                    cout << replaced_tiles[t].letter << " ";
                }
                cout << endl;

                cout << endl << "Press [enter] to continue.";
                std::cin.ignore();
            }

            // for each player, check if they have 0 tiles
            if ((this->players[i])->count_tiles() == 0) {
                players_have_tiles = false;
                // break out of loop, player has won
                break;
            }
        }
    }
}
// Useful cout expressions with fancy colors. Expressions in curly braces, indicate values you supply.
// cout << "You gained " << SCORE_COLOR << {points} << rang::style::reset << " points!" << endl;
// cout << "Your current score: " << SCORE_COLOR << {points} << rang::style::reset << endl;
// cout << endl << "Press [enter] to continue.";

// Performs final score subtraction. Players lose points for each tile in their
// hand. The player who cleared their hand receives all the points lost by the
// other players.
void Scrabble::final_subtraction(vector<shared_ptr<Player>>& plrs) {
    // TODO: implement this method.
    // Do not change the method signature.

    size_t total_sum = 0;
    size_t winner_index = 100;

    // iterate through players
    // if still have tiles, add sum of tile points to score
    // if have 0 tiles, winner, sum of all players tile points added to score
    for (size_t i = 0; i < plrs.size(); i++) {
        // zero tiles in hand, winner
        if (plrs[i]->count_tiles() == 0) {
            winner_index = i;
        }
        // still has tiles in hand
        else {
            // find sum of player's hand
            unsigned int hand_sum = plrs[i]->get_hand_value();

            size_t player_curr_points = plrs[i]->get_points();

            // check for potential negative score
            if (player_curr_points < hand_sum) {
                // set score to zero by subtracting the points in hand
                plrs[i]->subtract_points(player_curr_points);
            } else {
                // subtract sum of player's hand from points
                plrs[i]->subtract_points(hand_sum);
            }

            // update total sum
            total_sum = total_sum + hand_sum;
        }
    }
    // winner exists
    if (winner_index != 100) {
        plrs[winner_index]->add_points(total_sum);
    }
}

// TODO: implement add players
// called at the start of the game in the game_loop
void Scrabble::add_players() {
    // std::vector<std::shared_ptr<Player>> players;

    int num_players = 0;

    // number of players is [1, 8]
    while (num_players < 1 || num_players > 8) {
        // query for number of players
        cout << "What is the number of players?" << endl;

        // take in input from user
        cin >> num_players;
    }

    // for getline call in next loop, need to ignore end of line
    cin.ignore();

    // query for names
    for (int j = 1; j <= num_players; j++) {
        cout << "What is the name of player " << j << "?" << endl;

        std::string name = "";

        std::getline(std::cin, name);

        // each name entered on it's own line, so use getline
        // names may contain whitespace

        // constructor initilaizes points to 0
        // instantiate shared pointer to player object
        std::shared_ptr<Player> curr_player(new HumanPlayer(name, this->hand_size));

        // give each player, hand_size tiles to start
        curr_player->add_tiles((this->tile_bag).remove_random_tiles(this->hand_size));

        // add player object to players vector
        this->players.push_back(curr_player);
    }
}

// You should not need to change this function.
void Scrabble::print_result() {
    // Determine highest score
    size_t max_points = 0;
    for (auto player : this->players) {
        if (player->get_points() > max_points) {
            max_points = player->get_points();
        }
    }

    // Determine the winner(s) indexes
    vector<shared_ptr<Player>> winners;
    for (auto player : this->players) {
        if (player->get_points() >= max_points) {
            winners.push_back(player);
        }
    }

    cout << (winners.size() == 1 ? "Winner:" : "Winners: ");
    for (auto player : winners) {
        cout << SPACE << PLAYER_NAME_COLOR << player->get_name();
    }
    cout << rang::style::reset << endl;

    // now print score table
    cout << "Scores: " << endl;
    cout << "---------------------------------" << endl;

    // Justify all integers printed to have the same amount of character as the high score, left-padding with spaces
    cout << setw(static_cast<uint32_t>(floor(log10(max_points) + 1)));

    for (auto player : this->players) {
        cout << SCORE_COLOR << player->get_points() << rang::style::reset << " | " << PLAYER_NAME_COLOR
             << player->get_name() << rang::style::reset << endl;
    }
}

// You should not need to change this.
void Scrabble::main() {
    add_players();
    game_loop();
    final_subtraction(this->players);
    print_result();
}
