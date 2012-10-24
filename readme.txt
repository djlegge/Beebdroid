
Troubleshooting
===============

After deploy from Eclipse if it crashes with this error...

	10-24 09:36:12.003: E/AndroidRuntime(22162): java.lang.RuntimeException: Unable to instantiate activity 
		ComponentInfo{com.littlefluffytoys.beebdroid/com.littlefluffytoys.beebdroid.Beebdroid}: 
		java.lang.ClassNotFoundException: com.littlefluffytoys.beebdroid.Beebdroid

...check these libraries are ticked in "Order And Export" according to this page:

http://stackoverflow.com/questions/6253173/testing-android-project-with-jar-dependecies

