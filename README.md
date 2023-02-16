
# Android NexmoClient Test App

This is the Android NexmoClient Test App used for testing Compatibility with the new VonageClient SDK.

![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/natonathan/Android-NemxoClient-test-app?style=flat-square)
![NexmoClient SDK](https://img.shields.io/badge/NexmoSDK-v2.8.1-blue)

## Run Locally

Clone the project

```bash
  git clone https://github.com/NatoNathan/Android-NemxoClient-test-app.git
```

Go to the project directory

```bash
  cd Android-NemxoClient-test-app
```

Open in android studio

## Setup

### Prerequisites

- Vonage CLI
- Vonage developer account
  - Voange application
- NCCO server
- Java 1.8
- Android Studio or Intellij
- Android SDK (included with Android Studio, but not with Intellij)
- Firebase Account (optional for push)
  - Firebase application

### App Setup

if you don't need push skip to step x

#### 1. Setup Vonage and Firebase

1. Make a Firebase account [here](https://console.firebase.google.com/) if needed
2. Get Firebase Project Id `Firebase console -> Project settings -> General`
3. Get a Firebase Server Key
    - Legacy
      1. Open  `Firebase console -> Project settings -> Cloud Messaging -> Cloud Messaging API (Legacy) -> Manage API in Google Cloud Console`
        ![click Manage API in Google Cloud Console](assets/firebase_legacy.png)
      2. Click Enable
        ![Enable Legacy api](assets/ManageAPIGoogleCloudConsole.png)
      3. Go back to Firebase `Firebase console -> Project settings -> Cloud Messaging` and copy server key
        ![server key](assets/ServerKey.png)
4. Install `vonage` cli

    `npm install --location=global @Vonage/cli`

5. Make a Vonage APP (if needed)

    ```sh
    vonage apps:create APP_NAME --voice_answer_url=LINK_TO_NCCO --voice_event_url=example.com 
    ```

6. Make A Vonage Admin token (Vonage JWT without sub)

    ```sh
    vonage jwt --key_file=./vonage.private.key --acl='{"paths":{"/*/users/**":{},"/*/conversations/**":{},"/*/sessions/**":{},"/*/devices/**":{},"/*/image/**":{},"/*/media/**":{},"/*/applications/**":{},"/*/push/**":{},"/*/knocking/**":{},"/*/legs/**":{}}}' --app_id=VONAGE_APP_ID
    ```

7. Upload Server Key to Vonage

    ```sh
    VONAGE_APP_ID=
    VONAGE_JWT=
    FIREBASE_PROJECT_ID=
    FIREBASE_SERVER_KEY=
    curl -v -X PUT \
    -H "Authorization: Bearer $VONAGE_JWT" \
    -H "Content-Type: application/json" \
    -d "{\"token\":\"$FIREBASE_SERVER_KEY\", \"projectId\":\"$FIREBASE_PROJECT_ID\"}" \
    https://api.nexmo.com/v1/applications/$VONAGE_APP_ID/push_tokens/android
    ```

8. Make a Android Firebase app
    1. Go to firebase project
    2. Click on the "add Android app" option
    3. Download `google-services.json`

9. Place `google-services.json` in `./app` directory

10. Run localy with an emulator or local device
    Remember to enable host audio on emulator device
