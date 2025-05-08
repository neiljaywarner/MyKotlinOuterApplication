# Flutter Module Implementation Guide

This document provides instructions for implementing the Flutter side of the image sharing
functionality in `my_inner_flutter_module`.

## 1. Setup Method Channel in Flutter

Add a method channel listener to receive image URIs from the Android app in `main.dart`:

```dart
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Module',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const methodChannel = MethodChannel('com.neiljaywarner.mykotlinouterapplication/image_channel');
  String? _imageUri;

  @override
  void initState() {
    super.initState();
    // Set up method channel handler
    methodChannel.setMethodCallHandler(_handleMethodCall);
  }

  // Handle method calls from the Android side
  Future<dynamic> _handleMethodCall(MethodCall call) async {
    switch (call.method) {
      case 'setImageUri':
        final String uri = call.arguments as String;
        setState(() {
          _imageUri = uri;
        });
        return true;
      default:
        throw PlatformException(
          code: 'Unimplemented',
          details: 'Method ${call.method} not implemented',
        );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Flutter Module Demo'),
      ),
      body: Center(
        child: _imageUri == null
            ? const Text(
                'Hi From Inner Flutter Module\n\nWaiting for image from Android...',
                style: TextStyle(fontSize: 18),
                textAlign: TextAlign.center,
              )
            : Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text(
                    'Image received from Android:',
                    style: TextStyle(fontSize: 18),
                  ),
                  const SizedBox(height: 16),
                  // Use NetworkImage if the URI is remote, or FileImage if it's a local file
                  // You might need to handle different URI types differently
                  Image.network(
                    _imageUri!,
                    width: 300,
                    height: 300,
                    fit: BoxFit.contain,
                    errorBuilder: (context, error, stackTrace) {
                      return Column(
                        children: [
                          const Icon(Icons.error, size: 50, color: Colors.red),
                          const SizedBox(height: 8),
                          Text(
                            'Error loading image from URI:\n$_imageUri',
                            style: const TextStyle(color: Colors.red),
                            textAlign: TextAlign.center,
                          ),
                        ],
                      );
                    },
                  ),
                  const SizedBox(height: 16),
                  Text(
                    'URI: $_imageUri',
                    style: const TextStyle(fontSize: 12),
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
      ),
    );
  }
}
```

## 2. Handle Different URI Types

The code above assumes the URI is a network URI. For local file URIs (which is more likely when
sharing from the gallery), you'll need to modify the code:

```dart
// Use different image providers based on URI scheme
Widget _buildImage() {
  if (_imageUri == null) return const SizedBox();
  
  Uri uri = Uri.parse(_imageUri!);
  
  if (uri.scheme == 'file') {
    // For file:// URIs (local files)
    return Image.file(
      File(uri.path),
      width: 300,
      height: 300,
      fit: BoxFit.contain,
      errorBuilder: (context, error, stackTrace) => _buildErrorWidget(),
    );
  } else if (uri.scheme == 'content') {
    // For content:// URIs (Android content provider)
    // This requires additional plugins like path_provider and permission_handler
    return Text('Content URI: $_imageUri\n(Requires additional setup)');
  } else {
    // For http/https URIs
    return Image.network(
      _imageUri!,
      width: 300,
      height: 300,
      fit: BoxFit.contain,
      errorBuilder: (context, error, stackTrace) => _buildErrorWidget(),
    );
  }
}

Widget _buildErrorWidget() {
  return Column(
    children: [
      const Icon(Icons.error, size: 50, color: Colors.red),
      const SizedBox(height: 8),
      Text(
        'Error loading image from URI:\n$_imageUri',
        style: const TextStyle(color: Colors.red),
        textAlign: TextAlign.center,
      ),
    ],
  );
}
```

## 3. Add Necessary Dependencies

For handling file URIs and content URIs properly, you'll need to add these dependencies to your
`pubspec.yaml`:

```yaml
dependencies:
  flutter:
    sdk: flutter
  path_provider: ^2.1.1
  permission_handler: ^10.4.5
```

## 4. Build and Test

After making these changes to your Flutter module:

1. Build the AAR file:

```bash
cd ~/AndroidStudioProjects/my_inner_flutter_module
flutter build aar
```

2. Test with the Android app:

```bash
cd ~/AndroidStudioProjects/MyKotlinOuterApplication
./gradlew clean build
```

3. Run the app and test the image sharing functionality.

## 5. Troubleshooting

If you encounter issues:

1. **Image Not Displaying**:
    - Check the URI format received from Android
    - Add debug logs to print the URI in the Flutter console
    - Make sure you have the proper permissions for accessing the image file

2. **Method Channel Communication Issues**:
    - Verify that the method channel name matches on both Android and Flutter sides
    - Check if the method channel is set up correctly in `initState()`
    - Add debug prints to log when methods are called

3. **Build Issues**:
    - Make sure you're using compatible versions of Flutter and Dart
    - Check for any dependency conflicts in the pubspec.yaml