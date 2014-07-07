Sailthru Android SDK Library
=======================

Setup
-----
The Sailthru SDK uses the following open source libraries  
  
  - [Gson](https://code.google.com/p/google-gson/downloads/list)
  - [Tape](https://github.com/square/tape/downloads) 
  - [Retrofit](http://square.github.io/retrofit/#download)
  
Download and add these libraries along with the Sailthru SDK in your projects.

###For Android Studio,
Add the SailthruSDK jar to a <code>libs/</code> folder in your project root.  
In your <code>build.gradle</code>,  

    dependencies {
        ...
        compile files('libs/sailthru-1.0.0.jar')
        compile 'com.google.code.gson:gson:2.2.4'
        compile 'com.squareup:tape:1.2.2'
        compile 'com.squareup.retrofit:retrofit:1.6.0'
        ...
    }
    
###For Eclipse,
Add the following jars to the <code>libs/</code> folder in your project root and refresh you project.  
  
  - SailthruSDK  
  - [Gson](https://code.google.com/p/google-gson/downloads/list)
  - [Tape](https://github.com/square/tape/downloads)
  - [Retrofit](http://square.github.io/retrofit/#download)
  
Initialization
---------------

User Register
---------------

AppTrack
-------------

Recommendations
----------------

Logging
------------
