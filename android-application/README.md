# EventMe Android Mobile Application

### Description
- Team of 3 software developers working for ~2 months.
- Project for a Software Engineering course in Fall 2022.

### Technical Specifications
- XML
- Java
- Android Studio
- Firebase Realtime Database
- Google Maps API
- White-box testing: JUnit (local unit tests)
- Black-box testing: Espresso (instrumented tests)

### Requirements
###### User Story: Create an Account
- If a user does not have an account, the user will create one by inserting account information (name, birthday, profile picture, password and email). User object will be created. 
- If a user does not input a valid name, a TextView will appear asking the user to input a valid name.
- If a user inputs an email that has already been used for an account, a TextView will appear saying an account with that email already exists.
- If a user does not input a valid email, a TextView will appear asking the user to input a valid email.
- If the user does not upload a profile picture, default will be used.
- If a user does not input a valid birthday, a TextView will appear asking the user to input a valid birthday.

###### User Story: Log In
- The user inserts email and password. Creates cookies. If valid email and password, reroute to default page.
- The user inserts an incorrect combination of email and password, a TextView will appear stating “Incorrect email/password”.

###### Log Out
- Log out of account. Deletes cookies.

###### Search for an event.
- When a word or phrase is searched, all events with that word or phrase in them will be listed in cost order. 
- When a range of dates is searched, all events in that timeframe will be listed in lowest to highest cost order.
- When a word or phrase is searched and a range of dates is added, all events with that word or phrase will be listed in lowest to highest cost order. 
- When a word or phrase is searched and if there are no events with that word or phrase in them, the screen will display “No Events Found.”
- When a range of dates is searched and if there are no events within that time frame, the screen will display “No Events Found.”
- When a word or phrase is searched and a range of dates is added and if there are no matching events, the screen will display “No Events Found.”

###### After a search, sort events with filter.
- When sort by date is chosen, the events will be resorted again by closest date to farthest date.
- When sorting from lowest price to highest price is chosen, the events will be sorted again from lowest price to highest price.
- When sort alphabetically is chosen, the events will be sorted again in alphabetical order.
- When sort by location is chosen, the events will be sorted by closest location to farthest location.

###### Unregister for an event.
- Events will be removed from profile registered list.

###### View registered events.
- A list of registered events will be displayed in profile, sorted by date.
- If there are no registered events, the profile will be blank.

###### Find nearby events on a map visually.
- Display map with event bubbles, within 5 mile radius of user’s location.
- If no events are within 5 miles, the map is empty.

###### View map details in event list.
- All events are listed, sorted by closest distance. Display Event widget: name, location, date, time, cost($) and organization.
###### View event details.
- Event TextView appears with all event information, including name, location, date, time, cost, sponsoring organization, and description.

###### Nonfunctional Requirements
- Each page must load within 2 seconds.
- The sort must occur within 1 second. 
- Name must be valid. No numbers or special characters are allowed. Last name and first name must be at least one character long.
- Email must be valid. There must be at least 1 character, then a “@”, at least 1 other character, then a “.”, and then 3 characters.
- Birthday must be valid. Birthday must be a past day.
- When sorting, at most 20 listings will be presented per page.
