# Adding Flutter to an Existing Android App

This guide explains how to integrate an existing Flutter module with your Android app and pass image
URIs between them.

## 1. About the Flutter Module

An existing Flutter module named `my_inner_flutter_module` has already been created in the sibling
directory to your Android app. The module is configured to display images passed from the Android
app.

## 2. Integration Setup in the Android App

### 2.1 Building the Flutter Module

To make the Flutter module available to your Android app, you need to build it as an AAR (Android
Archive):
