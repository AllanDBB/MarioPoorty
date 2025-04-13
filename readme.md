## Project Overview

MarioPoorty is a Mario-themed digital board game built in Java using Swing for the graphical user interface. The game supports multiplayer functionality with a client-server architecture, allowing players to join a game session, select character tokens, communicate via chat, roll dice, and participate in various mini-games.

## Technical Architecture

### Client-Server Model

- **Server**: Manages game state, player connections, and coordinates player turns
- **Client**: Handles user interface and communication with the server

### Core Components

1. **Board System**: Represents the game board with different types of tiles
2. **Player Management**: Tracks player data, tokens, and game state
3. **Mini-Games**: Collection of interactive games triggered by special tiles
4. **UI Framework**: Series of frames for different game states (Init, Lobby, Pregame, Main Game)
5. **Communication System**: Chat functionality and server message handling

## Game Flow

1. **Initialization**: Players launch the application and connect to the server
2. **Character Selection**: Players choose character tokens in the Lobby frame
3. **Pregame Phase**: Players ready up for the game to start
4. **Main Game**: Players take turns rolling dice and moving on the board
5. **Mini-Games**: Special tiles trigger mini-games which can affect player progress

## Key Classes

### UI Components

- `InitFrame`: Initial game window with start button
- `LobbyFrame`: Character selection screen
- `PregameFrame`: Waiting room before game starts
- `MainGame`: Main game board interface
- `Chat`: Chat component integrated across different frames

### Game Logic

- `Board`: Contains the game board layout and tiles
- `Tile` / `SpecialTile`: Base and special tile implementations
- `Game` (interface): Common interface for all mini-games
- `Token`: Represents player tokens on the board
- `Dices`: Implements dice rolling mechanics

### Mini-Games

- `TreasureHuntGame`: Find hidden treasures
- `MemoryGame`: Card matching game
- `MemoryPath`: Remember and follow a pattern
- `GuessTheCharacterGame`: Identify Mario characters
- `CollectTheCoins`: Collect coins in a grid
- `CatchTheCat`: Chase and catch game element

### Networking

- `Server`: Handles client connections and game coordination
- `Client`: Manages communication with the server
- `ClientHandler`: Processes individual client requests

## Assets Structure

```
main/Assets/
├── Banner.jpg                 # Main banner image
├── Character tokens (*.png)   # Mario character tokens
│   ├── Mario.png
│   ├── Luigi.png
│   ├── Peach.png
│   └── ...
├── Dice assets               # Dice-related images
│   ├── Dice1.png
│   ├── Dice2.png
│   ├── ...
│   └── DiceRoll.gif
└── Tiles/                    # Tile images for the game board
    ├── NormalTile.png
    ├── SpecialTile.png
    ├── FireFlower.png
    └── ...
```

## User Interface

The game features multiple screens:

1. **Initial Screen**: Game title and start button
2. **Lobby Screen**: Character selection grid and player name input
3. **Pregame Screen**: Waiting area with ready button and chat
4. **Main Game Screen**: Game board, player tokens, dice, and chat

## Gameplay Features

- **Dice Rolling**: Players roll two dice to determine movement
- **Special Tiles**: Unique tiles with different effects on player progress
- **Mini-Games**: Interactive games that affect game progression
- **Chat System**: Real-time communication between players

## Getting Started

1. Run the server: `org.abno.main.Main`
2. Run the client: `org.abno.server.Client`
3. Enter player name and select a character token
4. Ready up and wait for the game to start
5. Take turns rolling dice and navigating the board

## Technical Requirements

- Java 8+
- Swing for UI components
- Network connectivity for multiplayer functionality

## Future Enhancements

- Additional mini-games
- Enhanced visual effects
- Game state persistence
- Customizable game rules