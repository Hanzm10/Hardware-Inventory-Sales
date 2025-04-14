# TODO

Project todos

## User Authentication 
 - [ ] Password Hashing using Crypto library.
 - [ ] Tests for password hashing
 - [ ] Password verification where it should meet our criteria (e.g. requires special character)
 - [ ] Tests for password verification
 - [ ] Email verification to verify that it's valid
 - [ ] Tests for email verification
 - [ ] For login, check if password is correct
 - [ ] Test to see if comparison logic for passwords is correct.
 - [ ] For registration, check if email exists
 - [ ] Saving session uid in GlobalConfig after login
 - [ ] Logout
 - [ ] Removing session uid in GlobalConfig after login
 
## User Authorization
 - [ ] If user navigates to a page that they're not allowed to access, redirect them to the previous page they were in.
 - [ ] If a user performs an action that they're not allowed to perform, show them an error.

## Database
 - [ ] Creating new User and UserCredentials
 - [ ] Creating new Session after login
 - [ ] Querying a user's password in UserCredentials
 - [ ] Querying a user's email in UserCredentials
 - [ ] Removing session in database after logout

## Controllers
 - [ ] Login Button controller
 - [ ] Register Button controller
 - [ ] Allotting login functionality in a SwingWorker
 - [ ] Allotting register functionality in a SwingWorker
 
## View
 - [ ] When logging in, disable button and show loading indicator
 - [ ] When registering, disable button and show loading indicator
 - [ ] Error messages for login
 - [ ] Error messages for register
 - [ ] Setting SessionManager after login to then redirect to main page.
 
## Image module
 - [ ] An image factory to preload or cache images
 