#include "board.h"

#include "board_square.h"
#include "exceptions.h"
#include "formatting.h"
#include <fstream>
#include <iomanip>

using namespace std;

bool Board::Position::operator==(const Board::Position& other) const {
    return this->row == other.row && this->column == other.column;
}

bool Board::Position::operator!=(const Board::Position& other) const {
    return this->row != other.row || this->column != other.column;
}

Board::Position Board::Position::translate(Direction direction) const { return this->translate(direction, 1); }

Board::Position Board::Position::translate(Direction direction, ssize_t distance) const {
    if (direction == Direction::DOWN) {
        return Board::Position(this->row + distance, this->column);
    } else {
        return Board::Position(this->row, this->column + distance);
    }
}

Board Board::read(const string& file_path) {
    ifstream file(file_path);
    if (!file) {
        throw FileException("cannot open board file!");
    }

    size_t rows;
    size_t columns;
    size_t starting_row;
    size_t starting_column;
    file >> rows >> columns >> starting_row >> starting_column;
    Board board(rows, columns, starting_row, starting_column);

    // TODO: complete implementation of reading in board from file here.
    // x lines of exactly y characters each
    // loops for each line (each row of board)

    // before calling getline, ignore prior line
    file.ignore();

    string row_str;
    while (getline(file, row_str)) {
        // instantiate vector for current row (current row_str)
        std::vector<BoardSquare> curr_row;

        // loop through row_str, adding each to row vector
        for (size_t i = 0; i < row_str.size(); i++) {
            // case 1 no bonus
            if (row_str[i] == '.') {
                BoardSquare square = BoardSquare(1, 1);
                curr_row.push_back(square);
            }
            // case 2: double letter bonus
            else if (row_str[i] == '2') {
                // boardsquare(letter_multiplier, word_multiplier)
                BoardSquare square = BoardSquare(2, 1);
                curr_row.push_back(square);
            }
            // case 3: triple letter bonus
            else if (row_str[i] == '3') {
                BoardSquare square = BoardSquare(3, 1);
                curr_row.push_back(square);
            }
            // case 4: double word bonus
            else if (row_str[i] == 'd') {
                BoardSquare square = BoardSquare(1, 2);
                curr_row.push_back(square);
            }
            // case 5: triple word bonus
            else if (row_str[i] == 't') {
                BoardSquare square = BoardSquare(1, 3);
                curr_row.push_back(square);
            }
        }

        // push vector onto board object 2D vector
        board.squares.push_back(curr_row);
    }

    return board;
}

size_t Board::get_move_index() const { return this->move_index; }

PlaceResult Board::test_place(const Move& move) const {
    // TODO: complete implementation here

    // check that it is place kind
    if (move.kind == MoveKind::PLACE) {
        // initialize vector of valid words
        std::vector<std::string> words_v;

        // initialize string for word
        std::string move_word = "";

        // initialize sum of points for current move
        unsigned int word_points = 0;

        // initialize sum of points for additional words
        unsigned int additional_points = 0;

        // bool to keep track of existence of at least 1 vertical/horiz existing tile
        bool adjacent_tile = false;

        // bool to keep track if placement on start square
        bool start_square_used = false;

        bool final_tile_placed = false;

        int double_word = 0;
        int triple_word = 0;

        // iterator for move vector
        std::vector<TileKind>::const_iterator move_tiles_it = (move.tiles).begin();

        Position move_start = Position(move.row, move.column);

        Position curr_pos = Position(move.row, move.column);

        // CASE: start square is out of board bounds
        if (!is_in_bounds(move_start)) {
            return PlaceResult("Invalid Placement: Starting position does not exist on the board.");
        }

        // CASE: start square already has a tile
        if (at(move_start).has_tile()) {
            // return invalid placeresult object
            return PlaceResult("Invalid Placement: Starting position already has a tile!");
        }

        // iterate while there are still tiles in move to test placement
        while (!final_tile_placed && is_in_bounds(curr_pos)) {
            // BIG CASE 1: horizontal placement
            if (move.direction == Direction::ACROSS) {

                // check if current square position is start position
                if (curr_pos == start) {
                    start_square_used = true;
                }

                // CASE: PLACING FIRST TILE
                // if placing first tile, check for letters to left
                // check for adjacent tiles to left
                if (move_tiles_it == move.tiles.begin()) {
                    // decrement column
                    Position left_check = Position(curr_pos.row, curr_pos.column - 1);

                    // check to left until no tile or out of bounds
                    while (in_bounds_and_has_tile(left_check)) {
                        left_check.column = left_check.column - 1;
                        adjacent_tile = true;
                    }
                    // loop will stop when reaching position
                    // that does not have tile or is out of bounds
                    // so add one to column to get to position where last tile occurs
                    left_check.column = left_check.column + 1;

                    // loop until reaching starting row to add letters/ points for move
                    while (left_check.column != move_start.column) {
                        BoardSquare curr_square = this->at(left_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to moves's word string
                        move_word.append(1, tile_char);

                        // update word points
                        word_points = word_points + curr_tile.points;

                        left_check.column = left_check.column + 1;
                    }
                }

                // CASE: EXISTING TILE AT POSITION IS PART OF MOVE WORD
                // already checked that this is not the first tile being placed

                // checks curr_pos to add existing tile to move word
                // don't need to check up and down
                if (in_bounds_and_has_tile(curr_pos)) {
                    adjacent_tile = true;

                    BoardSquare curr_square = this->at(curr_pos);

                    TileKind curr_tile = curr_square.get_tile_kind();

                    char tile_char = curr_tile.letter;

                    // check for blank
                    if (tile_char == '?') {
                        tile_char = curr_tile.assigned;
                    }

                    // append tile letter to moves's word string
                    move_word.append(1, tile_char);

                    // update word points
                    word_points = word_points + curr_tile.points;
                }

                // CASE: CURRENT TILE TO PLACE IS PUT AT CURRENT POSITION
                // tile does not exists at position
                // check for letter/word multipliers!!

                // add move tile to move word
                // then check up and down for additional word
                else if (is_in_bounds(curr_pos)) {
                    TileKind place_tile = *move_tiles_it;

                    char tile_char = place_tile.letter;

                    // check for blank
                    if (tile_char == '?') {
                        tile_char = place_tile.assigned;
                    }

                    BoardSquare place_square = this->at(curr_pos);

                    // append tile letter to moves's word string
                    move_word.append(1, tile_char);

                    // update word points, checking for multipliers
                    // first check for word multipliers
                    // case 1: double word
                    if (place_square.word_multiplier == 2) {
                        double_word++;
                    }
                    // case 2: triple word
                    else if (place_square.word_multiplier == 3) {
                        triple_word++;
                    }

                    // now check for letter multipliers and update points
                    // case 1: letter multiplier
                    if (place_square.letter_multiplier > 1) {
                        word_points = word_points + (place_square.letter_multiplier) * place_tile.points;
                    }
                    // case 2: no letter multipliers
                    else {
                        word_points = word_points + place_tile.points;
                    }

                    // now check upwards for additional word
                    // decrement row
                    Position up_check = Position(curr_pos.row - 1, curr_pos.column);

                    // check upwards until no tile or out of bounds
                    while (in_bounds_and_has_tile(up_check)) {
                        up_check.row = up_check.row - 1;
                        adjacent_tile = true;
                    }
                    // loop will stop when reaching position
                    // that does not have tile or is out of bounds
                    // so add one to row to get to position where last tile occurs
                    up_check.row = up_check.row + 1;

                    // string to store additional word
                    string extra_word = "";

                    // loop until reaching starting row to add letters/ points for extra word
                    while (up_check.row != move_start.row) {
                        BoardSquare curr_square = this->at(up_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        // update extra word points
                        additional_points = additional_points + curr_tile.points;

                        up_check.row = up_check.row + 1;
                    }

                    // CASE: TILES ABOVE
                    // add place letter to extra_word
                    if (extra_word != "") {
                        TileKind place_tile = *move_tiles_it;

                        char tile_char = place_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = place_tile.assigned;
                        }

                        // BoardSquare place_square = this->at(curr_pos);

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        additional_points = additional_points + place_tile.points;
                    }

                    // now do a downwards check for additional letters for extra_word
                    // increment row
                    Position down_check = Position(curr_pos.row + 1, curr_pos.column);

                    // CASE: NO TILES ABOVE, TILES EXIST BELOW
                    // only downwards letters for extra_word
                    // so place tile is first letter in extra word
                    // add place tile to extra word
                    if ((extra_word == "") && (in_bounds_and_has_tile(down_check))) {
                        TileKind place_tile = *move_tiles_it;

                        char tile_char = place_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = place_tile.assigned;
                        }

                        // BoardSquare place_square = this->at(curr_pos);

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        additional_points = additional_points + place_tile.points;

                        adjacent_tile = true;
                    }

                    // CASE: DOWN CHECK, adding existing tiles to extra word
                    // loop until reaching out of bounds or no tile
                    while (in_bounds_and_has_tile(down_check)) {
                        BoardSquare curr_square = this->at(down_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        // update extra points
                        additional_points = additional_points + curr_tile.points;

                        adjacent_tile = true;

                        down_check.row = down_check.row + 1;
                    }

                    // after checking up and down, if extra_word exists, add to vector of words if valid
                    if (extra_word != "") {
                        words_v.push_back(extra_word);
                    }

                    // increment iterator to get next tile to place
                    move_tiles_it++;
                }

                // CASE: FINAL TILE IS PLACED
                // already placed in function, now just checking right
                // check to right for additional letters for move word
                if (move_tiles_it == move.tiles.end()) {
                    // update boolean to stop loop
                    final_tile_placed = true;

                    Position right_check = Position(curr_pos.row, curr_pos.column + 1);

                    // move to right until encountering a space without tile or out of bounds
                    while (in_bounds_and_has_tile(right_check)) {
                        adjacent_tile = true;

                        BoardSquare curr_square = this->at(right_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to moves's word string
                        move_word.append(1, tile_char);

                        // update word points
                        word_points = word_points + curr_tile.points;

                        right_check.column = right_check.column + 1;
                    }
                }

                // increment current position horizontally to right before next loop
                curr_pos.column = curr_pos.column + 1;
            }

            // BIG CASE 2: vertical placement
            else if (move.direction == Direction::DOWN) {

                // check if current square position is start position
                if (curr_pos == start) {
                    start_square_used = true;
                }

                // CASE: PLACING FIRST TILE
                // if placing first tile, check for letters UP
                // check for adjacent tiles above
                if (move_tiles_it == move.tiles.begin()) {
                    // decrement row
                    Position up_check = Position(curr_pos.row - 1, curr_pos.column);

                    // check to left until no tile or out of bounds
                    while (in_bounds_and_has_tile(up_check)) {
                        up_check.row = up_check.row - 1;
                        adjacent_tile = true;
                    }
                    // loop will stop when reaching position
                    // that does not have tile or is out of bounds
                    // so add one to column to get to position where last tile occurs
                    up_check.row = up_check.row + 1;

                    // loop until reaching starting row to add letters/ points for move
                    while (up_check.row != move_start.row) {
                        BoardSquare curr_square = this->at(up_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to moves's word string
                        move_word.append(1, tile_char);

                        // update word points
                        word_points = word_points + curr_tile.points;

                        up_check.row = up_check.row + 1;
                    }
                }

                // CASE: EXISTING TILE AT POSITION IS PART OF MOVE WORD
                // already checked that this is not the first tile being placed

                // checks curr_pos to add existing tile to move word
                // don't need to check up and down
                if (in_bounds_and_has_tile(curr_pos)) {
                    adjacent_tile = true;

                    BoardSquare curr_square = this->at(curr_pos);

                    TileKind curr_tile = curr_square.get_tile_kind();

                    char tile_char = curr_tile.letter;

                    // check for blank
                    if (tile_char == '?') {
                        tile_char = curr_tile.assigned;
                    }

                    // append tile letter to moves's word string
                    move_word.append(1, tile_char);

                    // update word points
                    word_points = word_points + curr_tile.points;
                }

                // CASE: NO EXISTING TILE, CURRENT TILE IS PLACED AT POSITION
                // tile does not exists at position
                // check for letter/word multipliers!!

                // add move tile to move word
                // since placing tile, need to check for extra words
                // then check up and down for additional word
                else if (is_in_bounds(curr_pos)) {
                    TileKind place_tile = *move_tiles_it;

                    char tile_char = place_tile.letter;

                    // check for blank
                    if (tile_char == '?') {
                        tile_char = place_tile.assigned;
                    }

                    BoardSquare place_square = this->at(curr_pos);

                    // append tile letter to moves's word string
                    move_word.append(1, tile_char);

                    // update word points, checking for multipliers
                    // first check for word multipliers
                    // case 1: double word
                    if (place_square.word_multiplier == 2) {
                        double_word++;
                    }
                    // case 2: triple word
                    else if (place_square.word_multiplier == 3) {
                        triple_word++;
                    }

                    // now check for letter multipliers and update points
                    // case 1: letter multiplier
                    if (place_square.letter_multiplier > 1) {
                        word_points = word_points + (place_square.letter_multiplier) * place_tile.points;
                    }
                    // case 2: no letter multipliers
                    else {
                        word_points = word_points + place_tile.points;
                    }

                    // now check LEFT for additional word
                    // decrement column
                    Position left_check = Position(curr_pos.row, curr_pos.column - 1);

                    // check left until no tile or out of bounds
                    while (in_bounds_and_has_tile(left_check)) {
                        left_check.column = left_check.column - 1;
                        adjacent_tile = true;
                    }
                    // loop will stop when reaching position
                    // that does not have tile or is out of bounds
                    // so add one to column  to get to position where last tile occurs
                    left_check.column = left_check.column + 1;

                    // string to store additional word
                    string extra_word = "";

                    // loop until reaching starting row to add letters/ points for extra word
                    while (left_check.column != move_start.column) {
                        BoardSquare curr_square = this->at(left_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        // update extra word points
                        additional_points = additional_points + curr_tile.points;

                        left_check.column = left_check.column + 1;
                    }

                    // CASE: TILES LEFT EXIST, ADD PLACE CHAR TO EXTRA WORD
                    // add place letter to extra_word
                    if (extra_word != "") {
                        TileKind place_tile = *move_tiles_it;

                        char tile_char = place_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = place_tile.assigned;
                        }

                        // BoardSquare place_square = this->at(curr_pos);

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        additional_points = additional_points + place_tile.points;
                    }

                    // now do a right check for additional letters for extra_word
                    // increment column
                    Position right_check = Position(curr_pos.row, curr_pos.column + 1);

                    // CASE: NO TILES LEFT BUT TILES RIGHT EXIST, ADD PLACE CHAR TO EXTRA WORD
                    // only RIGHT letters for extra_word
                    // so place tile is first letter in extra word
                    // add place tile to extra word
                    if ((extra_word == "") && (in_bounds_and_has_tile(right_check))) {
                        TileKind place_tile = *move_tiles_it;

                        char tile_char = place_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = place_tile.assigned;
                        }

                        // BoardSquare place_square = this->at(curr_pos);

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        additional_points = additional_points + place_tile.points;

                        adjacent_tile = true;
                    }

                    // CASE: DOWN CHECK, adding existing tiles to extra word
                    // loop until reaching out of bounds or no tile
                    while (in_bounds_and_has_tile(right_check)) {
                        BoardSquare curr_square = this->at(right_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to extra word string
                        extra_word.append(1, tile_char);

                        // update extra points
                        additional_points = additional_points + curr_tile.points;

                        adjacent_tile = true;

                        right_check.column = right_check.column + 1;
                    }

                    // after checking left and right, if extra_word exists, add to vector of words
                    if (extra_word != "") {
                        words_v.push_back(extra_word);
                    }

                    // IF tile placed, increment iterator to get next tile to place
                    move_tiles_it++;
                }

                // CASE: FINAL TILE IS PLACED
                // already placed in function, now just checking down
                // check down for additional letters for move word
                if (move_tiles_it == move.tiles.end()) {
                    // update boolean to stop loop
                    final_tile_placed = true;

                    Position down_check = Position(curr_pos.row + 1, curr_pos.column);

                    // move down until encountering a space without tile or out of bounds
                    while (in_bounds_and_has_tile(down_check)) {
                        adjacent_tile = true;

                        BoardSquare curr_square = this->at(down_check);

                        TileKind curr_tile = curr_square.get_tile_kind();

                        char tile_char = curr_tile.letter;

                        // check for blank
                        if (tile_char == '?') {
                            tile_char = curr_tile.assigned;
                        }

                        // append tile letter to moves's word string
                        move_word.append(1, tile_char);

                        // update word points
                        word_points = word_points + curr_tile.points;

                        down_check.row = down_check.row + 1;
                    }
                }

                // increment current position vertically down one before next loop
                curr_pos.row = curr_pos.row + 1;
            }
        }

        // CASE: NO ADJACENT TILE and start not used, INVALID
        if (!adjacent_tile && !start_square_used) {
            return PlaceResult("Invalid Placement: No adjacent tile. If first move, must use the start square.");
        }

        // CASE: TILES PLACED OUT OF BOUNDS
        // unable to place all letters in v, iterator is not at end
        if (move_tiles_it != move.tiles.end()) {
            return PlaceResult("Invalid Placement: Tiles would be placed out of bounds on the board.");
        }

        unsigned int point_sum = 0;

        // word was created as move_word
        if (move_word.length() > 1) {
            // after testing placement, add move_word to vector
            words_v.push_back(move_word);

            // check for word multipliers
            if (double_word > 0) {
                word_points = word_points * 2 * double_word;
            }
            if (triple_word > 0) {
                word_points = word_points * 3 * triple_word;
            }

            // total up move points and additional word points
            point_sum = word_points + additional_points;
        }
        // word was created as additional word
        else {
            // check for word multipliers
            if (double_word > 0) {
                additional_points = additional_points * 2 * double_word;
            }
            if (triple_word > 0) {
                additional_points = additional_points * 3 * triple_word;
            }

            // total up move points and additional word points
            point_sum = additional_points;
        }

        // if no adjacent tile, but start square is used, valid result
        PlaceResult result(words_v, point_sum);
        return result;
    }
    // otherwise invalid so return invalid PlaceResult object with error message
    PlaceResult invalid_result("Invalid Placement");
    return invalid_result;
}

PlaceResult Board::place(const Move& move) {
    // TODO: Complete implementation here

    // call test_place to see if a valid placement
    PlaceResult tested_place = test_place(move);

    Position curr_pos = Position(move.row, move.column);

    // iterator for moves vector
    std::vector<TileKind>::const_iterator move_it = move.tiles.begin();

    // check if a valid PlaceResult
    if (tested_place.valid) {
        if (move.direction == Direction::ACROSS) {
            // update the board, only iterate through columns
            for (size_t c = curr_pos.column; c < columns; c++) {
                // update position object with current column
                curr_pos.column = c;

                // in bounds and has tile
                if (in_bounds_and_has_tile(curr_pos)) {
                    continue;
                }
                // in bounds does not have tile, update boardsquare
                else if (is_in_bounds(curr_pos)) {
                    // update tile kind, automatically updates boolean
                    squares[curr_pos.row][curr_pos.column].set_tile_kind(*move_it);

                    // increment moves vector iterator to next tilekind to place
                    move_it = move_it + 1;
                }

                // check if last tile was just placed, break from loop
                if (move_it == move.tiles.end()) {
                    break;
                }
            }
        }
        // vertical placement
        else if (move.direction == Direction::DOWN) {
            // update the board, only iterate through rows
            for (size_t r = curr_pos.row; r < rows; r++) {
                // update position object with current column
                curr_pos.row = r;

                // in bounds and has tile
                if (in_bounds_and_has_tile(curr_pos)) {
                    continue;
                }
                // in bounds does not have tile, update boardsquare
                else if (is_in_bounds(curr_pos)) {
                    // update tile kind, automatically updates boolean
                    squares[curr_pos.row][curr_pos.column].set_tile_kind(*move_it);

                    // increment moves vector iterator to next tilekind to place
                    move_it = move_it + 1;
                }
                // check if last tile was just placed, break from loop
                if (move_it == move.tiles.end()) {
                    break;
                }
            }
        }
    }

    // both cases return PlaceResult
    return tested_place;
}

// The rest of this file is provided for you. No need to make changes.

BoardSquare& Board::at(const Board::Position& position) { return this->squares.at(position.row).at(position.column); }

const BoardSquare& Board::at(const Board::Position& position) const {
    return this->squares.at(position.row).at(position.column);
}

bool Board::is_in_bounds(const Board::Position& position) const {
    return position.row < this->rows && position.column < this->columns;
}

bool Board::in_bounds_and_has_tile(const Position& position) const {
    return is_in_bounds(position) && at(position).has_tile();
}

void Board::print(ostream& out) const {
    // Draw horizontal number labels
    for (size_t i = 0; i < BOARD_TOP_MARGIN - 2; ++i) {
        out << std::endl;
    }
    out << FG_COLOR_LABEL << repeat(SPACE, BOARD_LEFT_MARGIN);
    const size_t right_number_space = (SQUARE_OUTER_WIDTH - 3) / 2;
    const size_t left_number_space = (SQUARE_OUTER_WIDTH - 3) - right_number_space;
    for (size_t column = 0; column < this->columns; ++column) {
        out << repeat(SPACE, left_number_space) << std::setw(2) << column + 1 << repeat(SPACE, right_number_space);
    }
    out << std::endl;

    // Draw top line
    out << repeat(SPACE, BOARD_LEFT_MARGIN);
    print_horizontal(this->columns, L_TOP_LEFT, T_DOWN, L_TOP_RIGHT, out);
    out << endl;

    // Draw inner board
    for (size_t row = 0; row < this->rows; ++row) {
        if (row > 0) {
            out << repeat(SPACE, BOARD_LEFT_MARGIN);
            print_horizontal(this->columns, T_RIGHT, PLUS, T_LEFT, out);
            out << endl;
        }

        // Draw insides of squares
        for (size_t line = 0; line < SQUARE_INNER_HEIGHT; ++line) {
            out << FG_COLOR_LABEL << BG_COLOR_OUTSIDE_BOARD;

            // Output column number of left padding
            if (line == 1) {
                out << repeat(SPACE, BOARD_LEFT_MARGIN - 3);
                out << std::setw(2) << row + 1;
                out << SPACE;
            } else {
                out << repeat(SPACE, BOARD_LEFT_MARGIN);
            }

            // Iterate columns
            for (size_t column = 0; column < this->columns; ++column) {
                out << FG_COLOR_LINE << BG_COLOR_NORMAL_SQUARE << I_VERTICAL;
                const BoardSquare& square = this->squares.at(row).at(column);
                bool is_start = this->start.row == row && this->start.column == column;

                // Figure out background color
                if (square.word_multiplier == 2) {
                    out << BG_COLOR_WORD_MULTIPLIER_2X;
                } else if (square.word_multiplier == 3) {
                    out << BG_COLOR_WORD_MULTIPLIER_3X;
                } else if (square.letter_multiplier == 2) {
                    out << BG_COLOR_LETTER_MULTIPLIER_2X;
                } else if (square.letter_multiplier == 3) {
                    out << BG_COLOR_LETTER_MULTIPLIER_3X;
                } else if (is_start) {
                    out << BG_COLOR_START_SQUARE;
                }

                // Text
                if (line == 0 && is_start) {
                    out << "  \u2605  ";
                } else if (line == 0 && square.word_multiplier > 1) {
                    out << FG_COLOR_MULTIPLIER << repeat(SPACE, SQUARE_INNER_WIDTH - 2) << 'W' << std::setw(1)
                        << square.word_multiplier;
                } else if (line == 0 && square.letter_multiplier > 1) {
                    out << FG_COLOR_MULTIPLIER << repeat(SPACE, SQUARE_INNER_WIDTH - 2) << 'L' << std::setw(1)
                        << square.letter_multiplier;
                } else if (line == 1 && square.has_tile()) {
                    char l = square.get_tile_kind().letter == TileKind::BLANK_LETTER ? square.get_tile_kind().assigned
                                                                                     : ' ';
                    out << repeat(SPACE, 2) << FG_COLOR_LETTER << square.get_tile_kind().letter << l
                        << repeat(SPACE, 1);
                } else if (line == SQUARE_INNER_HEIGHT - 1 && square.has_tile()) {
                    out << repeat(SPACE, SQUARE_INNER_WIDTH - 1) << FG_COLOR_SCORE << square.get_points();
                } else {
                    out << repeat(SPACE, SQUARE_INNER_WIDTH);
                }
            }

            // Add vertical line
            out << FG_COLOR_LINE << BG_COLOR_NORMAL_SQUARE << I_VERTICAL << BG_COLOR_OUTSIDE_BOARD << std::endl;
        }
    }

    // Draw bottom line
    out << repeat(SPACE, BOARD_LEFT_MARGIN);
    print_horizontal(this->columns, L_BOTTOM_LEFT, T_UP, L_BOTTOM_RIGHT, out);
    out << endl << rang::style::reset << std::endl;
}
