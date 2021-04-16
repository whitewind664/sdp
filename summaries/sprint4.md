---
# Summary for sprint 4
---

## Bajraktarov, Dimitar (Scrum Master)
- Done: Configured realtime database (Firestore can't detect offline usage without paying so I couldn't use it) and set up a schema for data storage
- Went well: Database is ready for testing and should be simple to use (for now)
- To improve: Find a way to mock the database for cirrus and improve the schema for future use cases

## Horvath-Mikulas, Szabina
- Done: I have been continuing the implementation of the chat activity. Added an interface for Firebase. It requires to discuss the data storage in Firestore to get tested. I have started to read about Android UIs in general to standardize the activities.
- Went well: Time expectation went well.
- To improve: Kotlin coding experience.

## Linder, Jan David
- Done: I learned the rules of Go myself and added a very basic tutorial to our app. Another great achievement are tests for the new activity and the main activity that can work locally and on cirrus - we can use those as examples for other activities.
- Went well: I finally feel pretty comfortable in writing tests.
- To improve: First of all, the tutorial is not finished at all, we need to invest more time into that. And the quality of some tests can still be improved.

## Protopapas, Kimon Dimitrios
- Done: Refactored Bluetooth functionality
- Went well: Achieved good architecture for bluetooth service, design part went well
- To improve: Read more about permissions and documentation for bluetooth

## Roust, Michael Murad
- Done: Created a single class for communicating for accessing User data and metadata. This was necessary as
some user info like email is stored in FirebaseAuth however, extra infomations such as nickname, lets go profile picture
and friends list cannot be stored in FirebaseAuth and has to be managed by us seperately. My work was to create
a class that communicates with both FirebaseAuth and our Firestore and give us a unified place to interact with all user
info.
- Went well: Understanding Firestore and working with Tasks (like Java Futures).
- To improve: Time needs to be invested to standardise accessing User info throughout the app. Next step will be
to be able to store more user information.

## Wengle, Erik Alessandro
- Done: Can establish bluetooth connection with any device
- Went well: Bluetooth connectivity seems to be working now
- To improve: Find a friend with an android device to finally test it

## Overall team (Scrum Master)
- Done: Advances in bluetooth connectivity, database setup and some of the existing features
- Went well: Looks like everyone finished one of their two tasks despite the complicated schedules
- To improve: Coordination was pretty hard due to upcoming holidays, conflicting advances in some areas due to having to do tasks for two sprints
