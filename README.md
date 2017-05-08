# cs0320 Term Project

**Team Members:** jpattiz, knimmaga, rhecht, wriley1

**Project Idea:** A Player vs. Player Collectable Card Game

**Mentor TA:** Nick DeMarco, nicholas_demarco@brown.edu

## Project Requirements

#### Concept
Players take turns drawing and playing cards to influence the game state, the goal being to reduce the opponent’s life to 0. 

The game will consist of two pieces of software--a server and a client. More than one server can be run from a given computer, but each server can only handle a single game with exactly two players. 

Players can create decks as plaintext files where each line contains the name of a card. 

#### Basic Control Flow
The client load screen will have an option to input a path to a deck and validate the deck (whether or not the deck contains an acceptable combination of cards).

Upon launch, a client will have a field to input ip address and port of the game server, and the path to the desired deck to load. On connecting, decks will be validated, and if they are not acceptable the player cannot connect. Once a server has two clients logged in, it will stop accepting new clients and begin a game.

Once the game begins, players will be able to see their own hand, the entire board state, and their opponents resources. Players will not be able to know the cards in their opponent’s hand. 

The game is played, with players alternating turns, until end goals are met. 

After a game is completed, each player will be presented with a screen informing them which player won. Players will have an option to rematch. If they choose this option, they may select a new deck for the next game. By default this field will be filled with the path to the deck they used last game. If both players choose to rematch, a new game will be started with the given decks.

#### Game Mechanics/Design Philosophies
We have not described the specific mechanics of the game, as this will be fairly complicated and will evolve significantly over time. However, we plan to adhere to the following design philosophies.

Give players a strong sense of agency throughout the course of the game. Players we talked to who have played other similar games (Hearthstone) complained a lot about the randomness. We are not totally against randomness but we want to give players control over the randomness: random choices may be presented, but players should still have an option. Also, we want to keep the scope of the randomness limited. Within reason, we want games to feel skill driven.

Despite focusing on controlled randomness, we want individual games between the same decks to feel different. There needs to be enough randomness that separate games feel like unique experiences. 
We want to avoid situations where between two reasonably constructed decks, one deck will win more than ~75% of the time. Reasonably constructed deck in this case means that it is a deck that has other decks it has winning matchups against. This is a fairly nebulous concept, which will have to evolve based on actual game data. 

We will have the ability to add or rebalance cards flexibly. One of the main benefits of a digital game vs a physical one is the ability to change cards. Often games like these devolve into degenerate game states where one strategy is overpowering, overly used, and becomes frustrating to play against. These games often sit in this state for long periods of time until new cards are added. We want to avoid this.

We want the game to be relatively accessible. These types of games feature huge amount of information. We want to develop relatively simple stategies that can be played with a small subset of the cardpool and a less refined understanding of the game that still perform reasonably well. Unlike some games that attempt this, however, we want to give a not insignificant disadvantage against higher skillcap decks. Easy decks played poorly should beat hard decks played poorly, and hard decks player well should beat easy decks player well.

To aid in this, we want to offer default decks that fall into this category of “easy decks” so that new players can just pick up and play relatively easily.

#### Rationale for Requirements
Control flow requirements were sourced from our combined years of experience playing multiplayer games with private server-client releationships. Design philosophies were sourced from conversations with players of industry leading CCG's such as Hearthstone and Magic, as well as reading online posts about the problems players have with these games. We're striving to improve on existing games by building on the mechanics they have introduced, while taking into account player feedback to improve them further. 


## Project Specs and Mockup
https://docs.google.com/a/brown.edu/document/d/1xQXIpBMozzv3iitiQWFQKrZzZy74x-2DrMD3RI9Evj8/edit?usp=sharing

Mockup:

https://xd.adobe.com/view/31887873-4f11-46d0-9159-c05904adf2e5/

## Project Design Presentation
https://docs.google.com/presentation/d/1iJAo8T5gdSyt4Vo4BV-95i9ne7NhUJs37k6EnSeNA94/edit?usp=sharing

## How to Build and Run
If YOU want to build and run it, clone the repository and create an empty folder data/users. Build with mvn clean -> mvn package and use the included run script. It will run on port 8080.  If you want to play the game, head to http://cardst.one and make an account! New accounts are subject to an optional tutorial that will explain the site. 
