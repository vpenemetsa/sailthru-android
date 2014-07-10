Sailthru Android SDK Library
=======================

Setup
-----
###For Android Studio,
Add the SailthruSDK jar to a <code>libs/</code> folder in your project root.  
In your <code>build.gradle</code>,  

    dependencies {
        ...
        compile files('libs/sailthru-1.0.0.jar')
        ...
    }
    
###For Eclipse,
Add the Sailthru SDK jar to the <code>libs/</code> folder in your project root and refresh you project.  

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
#####TODO. IN PROGRESS
The UserRegister method 
There are two types of registrations, Anonymous and Email,

###Anonymous,
    ...
    mSailthru.register(registrationMode, domain,
                                apiKey, appId, identification,
                                email, platformAppId);
    ...



AppTrack
-------------
The AppTrack call accepts the following parameters,  

  - <code>tags</code>      : List<String> 
  - <code>url</code>       : String
  - <code>latitude</code>  : String
  - <code>longitude</code> : String
  
####It is required to have atleast one tag or a url in a request.
    ...
    mSailthru.sendAppTrackData(tagList, url, latitude, longitude);
    ...

Recommendations
----------------
The Recommend call is a network call that returns a json string containing recommendations. This call 
needs to be made on a seperate thread(AsyncTask or Service) and accepts the following parameters,

  - <code>count</code> : int. The maximum number of recommendations to return.
  - <code>tags</code>  : List<String>. Return only items that match all of the given tags.
  
  
    ...
    mSailthru.getRecommendations(count, tags);
    ...

Logging
----------
