# Systems-for-Design-and-Implementation

# Swimming Competition Registration System

This repository contains a **client-server** application developed in **Java** using **Hibernate** for database operations. The system allows organizers to manage participant registrations for a national swimming competition. The client application provides a desktop interface that interacts with the server to manage the competition data.

## Features

### 1. Login
Users (office personnel) can log in using a unique username and password. After a successful login, a new window opens displaying:
- Available swimming events (distances: 50m, 200m, 800m, 1500m) and styles (freestyle, backstroke, butterfly, medley).
- The number of participants already registered for each event.

### 2. Search Participants
After login, users can search for participants who are registered in a specific event. The system will display:
- A list of participants, including their name, age, and all the events they are registered in.

### 3. Register Participants
Users can register new participants by entering:
- Participant name, age, and the events they want to participate in.  
Once a participant is registered, the system immediately updates the event lists for all users in all offices, reflecting the updated number of participants.

### 4. Logout
The user can securely log out from the system, and the session will be terminated.
