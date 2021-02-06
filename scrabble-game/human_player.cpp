#include "human_player.h"

#include "exceptions.h"
#include "formatting.h"
#include "move.h"
#include "place_result.h"
#include "rang.h"
#include "tile_kind.h"
#include <algorithm>
#include <iomanip>
#include <iostream>
#include <map>
#include <sstream>
#include <stdexcept>
#include <vector>

using namespace std;

// This method is fully implemented.
inline string& to_upper(string& str) {
    transform(str.begin(), str.end(), str.begin(), ::toupper);
    return str;
}

Move HumanPlayer::get_move(const Board& board, const Dictionary& dictionary) const {
    // TODO: begin your implementation here.
    // catch exception here
    // catch(MoveException& e)
    //{
    //    cout << e.what() << endl;
    //}

    // print player's hand
    cout << this->get_name() << "'s turn!" << endl;
    this->print_hand(std::cout);

    // query player for their move
    cout << "Enter next move: " << endl;

    // cin.ignore();
    string line_str = "";
    getline(std::cin, line_str);

    Move move_obj;

    // Exception thrown if:
    // User is proposing to place/discard tiles he/she does not have
    // or does not have enough copies
    try {
        move_obj = parse_move(line_str);
    } catch (out_of_range& e) {
        std::cerr << e.what();
        std::cerr << " Try Again!";
        std::cerr << endl;

        move_obj = get_move(board, dictionary);
    }

    // if move is PLACE, more to do:
    if (move_obj.kind == MoveKind::PLACE) {
        PlaceResult move_result = board.test_place(move_obj);

        try {

            // if placeResult object is not valid, print error message
            // then throw exception
            if (!move_result.valid) {
                throw MoveException(move_result.error);
            }

        } catch (MoveException& e) {
            std::cerr << e.what();
            std::cerr << endl;

            move_obj = get_move(board, dictionary);
        }

        // check if each word in move result exists in dictionary
        for (size_t i = 0; i < move_result.words.size(); i++) {
            bool result = dictionary.is_word(move_result.words[i]);

            // exception thrown if:
            // One or more of the words formed by placing the tiles are not in the dictionary.
            try {
                // if false result, means invalid word, throw exception
                if (!result) {
                    // throw exception
                    throw CommandException("Word does not exist in the dictionary. Try Again!");
                }
            } catch (CommandException& e) {
                std::cerr << e.what();
                std::cerr << endl;

                // recursive call, must get a new move
                move_obj = get_move(board, dictionary);
            }
        }
    }

    return move_obj;
}

vector<TileKind> HumanPlayer::parse_tiles(string& letters, string& move) const {
    // TODO: begin implementation here.

    vector<TileKind> letters_v;

    // iterate through letters string
    for (size_t i = 0; i < letters.size(); i++) {
        // Throws std::out_of_range if no tile with this letter exists in the collection.
        TileKind tile_to_add = this->tiles.lookup_tile(letters[i]);

        // check that place move, update assigned for balnk tile
        if ((move == "place" || move == "PLACE") && (tile_to_add.letter == '?')) {
            tile_to_add.assigned = std::tolower(letters[i + 1]);

            // increment i
            i++;
        }

        // now add tile to vector
        letters_v.push_back(tile_to_add);
    }

    // check for duplicates in string
    for (size_t j = 0; j < letters_v.size() - 1; j++) {
        size_t current_index_count = 1;
        for (size_t k = j + 1; k < letters_v.size(); k++) {
            if (letters_v[j] == letters_v[k]) {
                current_index_count++;
            }
        }
        // for each letter in string, check if same quantity in hand
        TileKind curr_tile = letters_v[j];
        size_t num_in_hand = this->tiles.count_tiles(curr_tile);

        if (current_index_count > num_in_hand) {
            throw out_of_range("Duplicate tile does not exist in hand.");
        }
    }
    // return vector
    return letters_v;
}

Move HumanPlayer::parse_move(string& move_string) const {
    // TODO: begin implementation here.
    // create stringstream from string
    stringstream sstream(move_string);

    string move_str;
    sstream >> move_str;

    // CASE: MOVE IS PASS
    // could be in upper or lowercase
    if (move_str == "PASS" || move_str == "pass") {
        // PASS constructor
        return Move();

        // nothing else to do so return object
    }

    // Otherwise place or exchange, call parse_tiles
    else {
        string second_input_str;
        sstream >> second_input_str;

        // CASE: MOVE IS PLACE
        // move is PLACE if direction char is second input
        if (move_str == "PLACE" || move_str == "place") {
            if (second_input_str == "-" || second_input_str == "|") {
                // create corresponding direction object
                Direction dir;

                if (second_input_str == "-") {
                    dir = Direction::ACROSS;
                } else if (second_input_str == "|") {
                    dir = Direction::DOWN;
                }

                size_t start_row;
                size_t start_col;

                sstream >> start_row;
                sstream >> start_col;

                // make sure move struct uses 0 indexing
                start_row = start_row - 1;
                start_col = start_col - 1;

                // get rest of sstream, it is the letters
                string letters_str;
                sstream >> letters_str;

                // call parse tiles
                vector<TileKind> letters_v = parse_tiles(letters_str, move_str);

                // PLACE constructor
                return Move(letters_v, start_row, start_col, dir);
            }
        }

        // CASE: MOVE IS EXCHANGE
        else if (move_str == "EXCHANGE" || move_str == "exchange") {
            // call parse tiles
            vector<TileKind> letters_v = parse_tiles(second_input_str, move_str);

            // EXCHANGE constructor
            return Move(letters_v);
        }
    }
}

// This function is fully implemented.
void HumanPlayer::print_hand(ostream& out) const {
    const size_t tile_count = tiles.count_tiles();
    const size_t empty_tile_count = this->get_hand_size() - tile_count;
    const size_t empty_tile_width = empty_tile_count * (SQUARE_OUTER_WIDTH - 1);

    for (size_t i = 0; i < HAND_TOP_MARGIN - 2; ++i) {
        out << endl;
    }

    out << repeat(SPACE, HAND_LEFT_MARGIN) << FG_COLOR_HEADING << "Your Hand: " << endl << endl;

    // Draw top line
    out << repeat(SPACE, HAND_LEFT_MARGIN) << FG_COLOR_LINE << BG_COLOR_NORMAL_SQUARE;
    print_horizontal(tile_count, L_TOP_LEFT, T_DOWN, L_TOP_RIGHT, out);
    out << repeat(SPACE, empty_tile_width) << BG_COLOR_OUTSIDE_BOARD << endl;

    // Draw middle 3 lines
    for (size_t line = 0; line < SQUARE_INNER_HEIGHT; ++line) {
        out << FG_COLOR_LABEL << BG_COLOR_OUTSIDE_BOARD << repeat(SPACE, HAND_LEFT_MARGIN);
        for (auto it = tiles.cbegin(); it != tiles.cend(); ++it) {
            out << FG_COLOR_LINE << BG_COLOR_NORMAL_SQUARE << I_VERTICAL << BG_COLOR_PLAYER_HAND;

            // Print letter
            if (line == 1) {
                out << repeat(SPACE, 2) << FG_COLOR_LETTER << (char)toupper(it->letter) << repeat(SPACE, 2);

                // Print score in bottom right
            } else if (line == SQUARE_INNER_HEIGHT - 1) {
                out << FG_COLOR_SCORE << repeat(SPACE, SQUARE_INNER_WIDTH - 2) << setw(2) << it->points;

            } else {
                out << repeat(SPACE, SQUARE_INNER_WIDTH);
            }
        }
        if (tiles.count_tiles() > 0) {
            out << FG_COLOR_LINE << BG_COLOR_NORMAL_SQUARE << I_VERTICAL;
            out << repeat(SPACE, empty_tile_width) << BG_COLOR_OUTSIDE_BOARD << endl;
        }
    }

    // Draw bottom line
    out << repeat(SPACE, HAND_LEFT_MARGIN) << FG_COLOR_LINE << BG_COLOR_NORMAL_SQUARE;
    print_horizontal(tile_count, L_BOTTOM_LEFT, T_UP, L_BOTTOM_RIGHT, out);
    out << repeat(SPACE, empty_tile_width) << rang::style::reset << endl;
}
