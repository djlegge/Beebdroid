6th March 2014.
This is a fork of DuncanHodson with some cherry-picks from richq.
DuncanHodson gave the ability to load locally stores disk images and richq removed tracking and adds.
The main change here is that locally stored double sided disk images now can be loaded and read.
Previously double sided disk images just gave 'Disk Fault'.
.
.
Experimental release 28th October 2012 (DuncanHodson)

Changes
=======

- Now possible to load games stored in //SDcard/Beebdroid/files/disks/*.ssd
- Now possible to use USB attached joysticks and keyboards (Logitech Wingman Action works)
- Keys can now be configured per game.  Config stored in human readable text file in //SDcard/Beebdroid/config/beebdroidgamecontrols.txt
- Small change to the game loop timing which improves performance
- Changes to how the keyboard (or controls) are rendered which improves performance

Troubleshooting
===============

After deploy from Eclipse if it crashes with this error...

	10-24 09:36:12.003: E/AndroidRuntime(22162): java.lang.RuntimeException: Unable to instantiate activity 
		ComponentInfo{com.littlefluffytoys.beebdroid/com.littlefluffytoys.beebdroid.Beebdroid}: 
		java.lang.ClassNotFoundException: com.littlefluffytoys.beebdroid.Beebdroid

...check these libraries are ticked in "Order And Export" according to this page:

http://stackoverflow.com/questions/6253173/testing-android-project-with-jar-dependecies

