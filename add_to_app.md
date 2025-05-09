# Adding Flutter to an Existing Android App

This guide explains how to integrate an existing Flutter module with your Android app and pass image
URIs between them using Pigeon.

## 1. About the Flutter Module

An existing Flutter module named `receive_images_flutter_demo` (located in a sibling directory
`../receive_images_flutter_demo/`) is used. This module is set up with Pigeon for typesafe
communication and is designed to receive an image URI and display the corresponding image.

## 2. Integration Setup in the Android App

### 2.1 Building the Flutter Module AAR

To make the Flutter module and its generated Pigeon code available to your Android app, you first
need to build it as an AAR (Android Archive):
