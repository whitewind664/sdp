---
# Summary for week 6
---

## Bajraktarov, Dimitar
- Done: Wrote tests for the database class
- Went well: Found a way to test the database without writing a mocked one, which is probably better
- To improve: Learn to use app contexts better in order to simulate logged in users

## Horvath-Mikulas, Szabina
- Done: I checked the existing component of MATERIAL design for Android as well as the Jetpack libraries and the way how they can be integrated to the app. I also analysed the Google’s recommendation of having 1 activity and many fragments.
- Went well: I received the necessary knowledge to generalise the UI.
- To improve: I could not create a PR this week, should further divide my task to complete them in a week.

## Linder, Jan David
- Done: I worked on the tutorial activity and made it possible to combine text with small (guided) game parts. Furthermore, I did some refactoring in the beginning of the sprint.
- Went well: Good time management (especially also for writing tests).
- To improve: I feel like I could communicate more effectively with the team. Reducing quantity and improve quality and relevance.

## Protopapas, Kimon Dimitrios (Scrum Master)
- Done: Worked on making the game playable over Bluetooth using the Bluetooth communication code we wrote
- Went well: Design done, and architecture is clean
- To improve: Need to do more refactoring as there is a lot of common code

## Roust, Michael Murad
- Done: Changed User class from using Firestore to RealtimeDatabase. In the process I decoupled all online elements
such as Database and Authentication from the User class to allow for it to be tested with a Mock Database.
- Went well: Cleaning up the User class code and modifing exisiting code to fit the changes. Understanding RealtimeDatabase.
- To improve: After switiching to RealtimeDatabase I realise that this will unfortunatly make things more tedious to work
with lists. Also mocking the database will be a very challenging task since some functionalities we will probably use
are quite complex, but I guess we will hear more from the team about this.

## Wengle, Erik Alessandro
- Done: two let's go apps can exchange messages using bluetooth
- Went well: i found most of the bugs from last week quickly
- To improve: there are still unrelated devices being listed, which i will fix in the future

## Overall team (Scrum Master)
- Done: Bluetooth is moving forward, Firebase is getting closer to completion and the tutorial is done.
- Went well: Overall we are working well together, our code is more interdependent and we are adapting well to that.
- To improve: We need to make smaller issues so that we have something to turn into a PR.
Also testing needs to be part of the work done each week.