Sailthru Android SDK Library
=======================


Product Features
-----
####Supported Features
The Sailthru Android SDK provides:
*  User registration
*  Anonymous user support 
*  Profile data acquisition 
*  Recommendation delivery 


Setup
-----

###JAR
Download the [SailthruAndroidSDK] jar

####For Android Studio,
Add the SailthruSDK jar to a <code>libs/</code> folder in your project root.  
In your <code>build.gradle</code>,  

    dependencies {
        ...
        compile files('libs/sailthru-1.0.0.jar')
        ...
    }
    
####For Eclipse,
Add the Sailthru SDK jar to the <code>libs/</code> folder in your project root and refresh you project.
  
###Gradle
    dependencies {
            ...
            compile 'com.sailthru:com.sailthru.android:1.+'
            ...
        }
        
###Maven
    <dependency>
      <groupId>com.sailthru</groupId>
      <artifactId>com.sailthru.android</artifactId>
      <version>1.0</version>
    </dependency>

Initialization
---------------
Initialize the SDK in an <code>Application</code> class,
    
    public class MyApplication extends Application {
    
        Sailthru mSailthru;
        
        ...
        
        @Override
            public void onCreate() {
                super.onCreate();
                mSailthru = new Sailthru(getApplicationContext());
                ...
            }
            
        ...
        
        public Sailthru getSailthruInstance() {
            return mSailthru;
        }
        
        ...
    }
    
And Access the Sailthru client in your classes,
    
    public class MyActivity extends Activity {
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.my_activity);
            
            Sailthru sailthru = ((SailthruQAClientApplication) getApplicationContext()).getSailthruInstance();
            ...
        }
    }
    
Or,

    public class MyFragment extends Fragment {
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            Sailthru sailthru = ((SailthruQAClientApplication) getActivity().getApplicationContext()).getSailthruInstance();
            ...
        }
    }
    

User Register
---------------
The UserRegister method accepts the following parameters,

  - <code>environment</code>    : RegistrationEnvironment. (__DEV__ or __PROD__). 
  - <code>domain</code>         : String. Created in the [Sailthru] dashboard.
  - <code>apiKey</code>         : String. Can be found in the [Sailthru] dashboard. 
  - <code>appId</code>          : String. Can be found in the [Sailthru] dashboard.
  - <code>identification</code> : Identification. (__EMAIL__ or __ANONYMOUS__).
  - <code>uid</code>            : String. Email of user if <code>identification</code> is __EMAIL__. <code>null</code> if <code>identification</code> is __ANONYMOUS__.
  - <code>platformAppId</code>  : String. Can be found in the [Sailthru] dashboard. 
  
There are two types of registrations, Anonymous and Email,

###Anonymous
    
    mSailthru.register(RegistrationMode.PROD, example.sailthru.com, 000000, 123456, Identification.ANONYMOUS, null, com.example);
    
###Email

    mSailthru.register(RegistrationMode.PROD, example.sailthru.com, 000000, 123456, Identification.EMAIL, jdoe@sailthru.com, com.example);

AppTrack
-------------
The AppTrack call accepts the following parameters,  

  - <code>tags</code>      : List<String> 
  - <code>url</code>       : String
  - <code>latitude</code>  : String
  - <code>longitude</code> : String
  
####It is required to have atleast one tag or a url in a request.
    mSailthru.sendAppTrackData(tagList, url, latitude, longitude);


Recommendations
----------------
The Recommend call is a network call that returns a json string containing recommendations. This call 
needs to be made off of the UI thread(AsyncTask or Service) and accepts the following parameters,

  - <code>count</code> : int. The maximum number of recommendations to return.
  - <code>tags</code>  : List<String>. Return only items that match all of the given tags.

Example call,  

    mSailthru.getRecommendations(count, tags);  

Logging
----------
If you need to take a closer look at the requests, responses and other log messages, you can intercept logs using a custom implementation of the <code>Logger</code>. You can also set the <code>LogLevel</code> property. The possible logging levels are <code>NONE</code>, <code>BASIC</code> and <code>FULL</code>

    Logger logger = new Logger() {
        @Override
        protected void d(LogLevel logLevel, String s, String s2) {
            ...
        }

        @Override
        protected void w(LogLevel logLevel, String s, String s2) {
            ...
        }

        @Override
        protected void e(LogLevel logLevel, String s, String s2) {
            ...
        }
    };
    
    logger.setLogLevel(LogLevel.FULL);
    
    mSailthru.setLogger(logger);
  
The <code>LogLevel</code> is set to <code>BASIC</code> by default.


[SailthruAndroidSDK]:http://search.maven.org/remotecontent?filepath=com/sailthru/com.sailthru.android/1.0/com.sailthru.android-1.0.jar
[Sailthru]:https://my.sailthru.com/

