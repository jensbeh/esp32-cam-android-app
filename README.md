# Android App for ESP32-CAM with motion sensor

This app is for the [ESP32-CAM](https://github.com/jensbeh/esp32-cam) into a security system with motion detection:

The ESP hosts a WebServer that provides the camera stream and a WebSocketServer that is used to communicate commands between the [Android app](https://github.com/jensbeh/esp32-cam-android-app) and the Esp. It also sends a notification to the [Android app](https://github.com/jensbeh/esp32-cam-android-app) with an image when it detects motion.

# Usage
This app is for the ESP32-CAM to display the video stream and to display notifications if the ESP32-CAM detects motion.

The app displays the video streams in WebViews as a WebServerClient. The app also sends and receives via WebSocket connections with the stored ESP cameras.

## Add camera:
In the app, a new camera can be added via the "+". The app scans the network to which the mobile phone is connected to list the available ESP cameras. Then a name for the respective camera is selected and connected. 

## Home page:
The video stream of the camera is displayed on the home page. There is also the possibility to activate the flash from there or to start a video recording of the stream or to take a picture of it.

## Camera settings:
The settings button next to the camera name opens a settings overview for the respective camera, where camera settings such as name, brightness, etc. can be changed. can be set. The settings can also be reset to "default" or the camera can be removed completely. The camera is deleted from the mobile phone (pictures and videos remain). A factory reset for the ESP can also be carried out in the settings. This resets the wifi settings and the camera settings and restarts the ESP.

## Gallery page:
Pictures and videos that have been taken are displayed in the gallery page and can be viewed or deleted there. The recordings are saved in the phone under DCIM/EspCamera.

## Notification page:
However, if the ESP detects movement, a WebSocket message is sent to all clients. This is shown as a notification in Android and displayed in the notification page of the app with an image of the movement. These can also be deleted and the images are stored in Documents/EspCamera.


# Camera Controls
* Framesize (1600 x 1200, 1280 x 1024, 1024 x 768, 800 x 600, 640 x 480, 352 x 288, 320 x 240, 240 x 176, 160 x 120)
* Quality (10 to 63)
* Brightness (-2 to 2)
* Contrast (-2 to 2)
* Saturation (-2 to 2)
* Special Effects (No Effect, Negative, Grayscale, Red Tint, Green Tint, Blue Tint, Sepia)
* Auto White Balance (On/Off)
* Auto White Balance Gain (On/Off)
* White Balance Mode (Auto, Sunny, Cloudy, Office, Home) (only if Auto White Balance Gain is on)
* Exposure Control (On/Off)
* Aec value (0 to 1200) (only if Exposure Control is off)
* Aec2 (On/Off)
* Ae level (-2 to 2)
* Agc Control (On/Off)
* Agc Gain (0 to 30) (only if Agc Control is off)
* Gain Ceiling (0 to 6) (only if Agc Control is on)
* Bpc (On/Off)
* Wpc (On/Off)
* Raw Gma (On/Off)
* Lens Correction (On/Off)
* H-Mirror (On/Off)
* V-Flip (On/Off)
* Colorbar (On/Off)
* Flashlight (On/Off)

# Download and install
Download the latest version of the app [here]() and install it.<br/>
This must first be allowed in the Android settings, as Android by default does not allow apps to be installed that are not from the Play Store.<br/>
To do this, simply go to Settings -> Security -> Activate Install unknown apps.

# Bug report
Found a bug? Please post on [GitHub](https://github.com/jensbeh/esp32-cam-android-app/issues).