## Team / App Name
goGetters / Let’s Go

## Build and test coverage information
Cirrus CI: [![Build Status](https://api.cirrus-ci.com/github/whitewind664/sdp.svg)](https://cirrus-ci.com/github/whitewind664/sdp)

Code Climate: [![Test Coverage](https://api.codeclimate.com/v1/badges/eb499ed5d368f682cb26/test_coverage)](https://codeclimate.com/github/whitewind664/sdp/test_coverage) [![Maintainability](https://api.codeclimate.com/v1/badges/eb499ed5d368f682cb26/maintainability)](https://codeclimate.com/github/whitewind664/sdp/maintainability)

## Description
An Android app that is realized as part of the SDP course at EPFL.

Standalone client for playing Go online against real players and offline against bots. Some ideas of features are:  
 - Interfacing with the OGS website for online playing (most popular Go website)
 - Puzzles for practicing tactics
 - Offline play against bots
 - Custom matchmaking that takes into account GPS position of players
 - Support for “teaching games”: so that teachers can supervise students’ games and give them advice

# Requirements
## Split app model
OGS website has an API that we can interface to play multiplayer games with players that have an OGS account, and has features like tournaments/forums. Most likely firebase for backend. We can store games with the history of moves with engine evaluations, forum posts and more.

## Sensor usage
 - Gyroscope: Flip phone to flip the board in order to forfeit in a rage-quitting manner.
 - GPS: Local matchmaking
 - Voice: Voice chat for ~~trash~~ smalltalk during games (streaming real-time voice might be too complicated)

## User support
Registration is required to play online against other players. Users will have an ELO rating according to their skill. Users can add friends and post on forums.


## Local cache
We use the local cache to store the standing of a current (offline) game. It may also be used to store messages from other users sent earlier or to look at game statistics of previous games. Can cache a certain number of puzzles to use when offline.

## Offline mode

Offline play against bots, cached puzzles/joseki (learn theory) plus store cached history of games to review. Can use offline puzzles stored in cache.
