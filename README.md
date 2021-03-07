# SDP Proposal - Team \#12

## Team / App Name
goGetters / Let’s Go

## Description

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

