DroidPermPlugin
=============

Android Studio plugin that partially automates the insertion of permission guards to fix support for Android System Permissions (API 23+).
See [Normal and Dangerous Permissions](https://developer.android.com/guide/topics/security/permissions.html#normal-dangerous) for a list of permissions that require guards. This plugin provides a menu action to encapsulate calls requiring dangerous permissions in runtime permission
check, request, and callback blocks.

Installation
============

1.  Download and install IntelliJ IDEA (2016.1 or higher).

2.  Install and configure the IntelliJ Plugin Platform. Instructions can be
    found on the IntelliJ Platform SDK DevGuide site
    ([link](http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/setting_up_environment.html)).

3.  Open IntelliJ IDEA and import the `DroidPermPlugin` project. Verify that all
    SDK and JDK settings have been set within the Project Settings menu.

4.  Select `Build -> Prepare Plugin Module 'DroidPermPlugin' for Deployment`.

5.  Launch Android Studio (2.0 or higher).

6.  Select `Install plugin from disk...` from the `File -> Settings.. ->
    Plugins` menu.

7.  Navigate to the location that the `DroidPermPlugin` project was pulled into
    and select `DroidPermPlugin.zip`.

8.  Navigate to the plugin settings panel by going to `File -> Settings -> Other -> DroidPermPlugin`. 
    Set the `DroidPerm home` field to the absolute path to `DroidPermPlugin/dp-lib` on your computer.

Usage
=====

### Automated refactoring

The automatic refactoring can be executed in Android Studio either through 
the top-menu, or by a right-click within the editor window, and selecting 
`Refactor -> Convert to Android Runtime Permissions`.

refactoring tool
=======================
### Features

-   Supports inserting `checkSelfPermission` and `requestPermissions` statements surrounding code which requires a specific dangerous permission, and the `onRequestPermissionsResult` method to handle the return from `requestPermissions`.
-   Natively supports Groovy-based templates for the following Android callback methods:
    -   `android.app.Activity`
    -   `android.app.Fragment`
    -   `android.app.Service`
    -   `android.view.View`
-   Supports a default template that inserts the `checkSelfPermission` statement only; allowing user-defined classes which do not derive from Android callback methods to be supported.
-   Simplified configuration files for editing and adding templates to the natively supported set.
-   Automatically resolves necessary import statements for libraries required by the inserted statements.
-   Extracts code into new methods when multiple lines are selected in order to prevent duplication in the `onRequestPermissionsResult` method.
-   Extracts declarations and references to local variables into new class fields to allow for scope access within the `onRequestPermissionsResult` method.
-   Extracts method parameter references into new class fields to allow for scope access within the `onRequestPermissionsResult` method.
-   Handles updating existing `onRequestPermissionsResult` methods with new if-block or switch-block statements when adding a new call to `requestPermissions`.

### Not Yet Supported

-   Support for sensitive calls requiring more than one permission.
-   Support for permissions libraries and utility classes.
-   Support for the `android.content.Intent` model of checking, requesting, and handling permissions.
    
