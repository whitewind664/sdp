---
# Summary for week 9
---

## Bajraktarov, Dimitar
- Done: Set up firebase emulator (locally, and with dedicated script on the cirrus container) and adapted all androidTests to use emulated database
- Went well: Finished full firebase emulation of the database
- To improve: Couldn't find a way to put initialization code which runs only once before all tests

## Horvath-Mikulas, Szabina
- Done: This week I worked on the chat feature. I created a new activity that fetches the users to chat with. I can also list last messages from the different users. I also worked on the implementation on the different chat channels. In the meantime I tried to improve the navigation UI, ie. the AppBar and BottomNavigationBar.
- Went well: I got very close to completion. I learnt how to use and communicate with the database.
- To improve: I need to add tests to be able to merge. I should not pick different tasks for one week period.

## Linder, Jan David
- Done: I implemented the profile picture such that it is stored on the CloudStorage and linked to a user. As I had some more times, I tried to work on better tests for services like the database and CloudStorage, but unfortunately I lost a lot of time because mockito is really not easy to use in instrumented android tests. Therefore, it was impossible to mock Tasks, hence I needed to convert those to futures.
- Went well: I managed to finish last weeks task quite quickly.
- To improve: I missed a stand-up meeting - this should not happen!

## Protopapas, Kimon Dimitrios
- Done: finally finished Bluetooth gameplay
- Went well: extremely efficient pair programming with Erik, we know the Bluetooth code well now
- To improve: more tests

## Roust, Michael Murad
- Done: Worked on the user login
- Went well: the user login is progressing
- To improve: Started work way too late

## Wengle, Erik Alessandro (Scrum Master)
- Done: I refactored the entire code of the Bluetooth Activity and its respective classes
- Went well: I was able to finish work quickly using Android studio's tools and was able to progress to write tests
- To improve: In the future, I should write clean code from the beginning in order to spend less work in refactoring

## Overall team (Scrum Master: Erik)
- Done: We were finally able to have a working bluetooth game
- Went well: Communication between now goes very smoothly and efficient
- To improve: Once again, most of the work is done on thursday night / friday morning, which we should change.
