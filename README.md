# Important information
* Do not change the .gitignore file.
* The batabase password is contained in the Secret.java class (we know it whould be contained in a yaml-file but did not have the time to change it), which is included in the .gitignore file.
* If you wish to start the application and already be logged in, add this below line 49 in App.java: isLoggedIn = User.arrayUsersGlobal.get(0);
Index 0 is a student and index 1 is a staffmember.
