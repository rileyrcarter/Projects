#include "player.h"

using namespace std;

// TODO: implement member functions
// Adds points to player's score
void Player::add_points(size_t points) { this->points = this->points + points; }

// Subtracts points from player's score
void Player::subtract_points(size_t points) { this->points = this->points - points; }

size_t Player::get_points() const { return this->points; }

const std::string& Player::get_name() const { return this->name; }

// Returns the number of tiles in a player's hand.
size_t Player::count_tiles() const { return this->tiles.count_tiles(); }

// Removes tiles from player's hand.
void Player::remove_tiles(const std::vector<TileKind>& tiles) {
    // iterate through tiles vector
    for (size_t j = 0; j < tiles.size(); j++) {
        // remove current tile from player's hand tiles
        this->tiles.remove_tile(tiles[j]);
    }
}

// Adds tiles to player's hand.
void Player::add_tiles(const std::vector<TileKind>& tiles) {
    // iterate tiles vector
    for (size_t i = 0; i < tiles.size(); i++) {
        // add current tile to player's tiles
        this->tiles.add_tile(tiles[i]);
    }
}

// Checks if player has a matching tile.
bool Player::has_tile(TileKind tile) {
    // define iterator for tile collection object (player's hand)
    TileCollection::const_iterator it = this->tiles.cend();

    // loop through map with iterator until finding search tile
    while (it != this->tiles.cend()) {
        // if key(tilekind) i same as target, return true
        if (*it == tile) {
            return true;
        }
    }

    // did not find target, so return false
    return true;
}

// Returns the total points of all tiles in the players hand.
unsigned int Player::get_hand_value() const { return this->tiles.total_points(); }

size_t Player::get_hand_size() const { return this->hand_size; }
