# NextHire AI — Android Career-App Prototype

An Android project written in Kotlin with Jetpack Compose. Its source tree includes screens for a dashboard, AI mentor, résumé/ATS, coding practice, interview preparation, community, jobs, resources, and an admin CMS. This README describes the checked-in source structure; it does not claim that every screen or service has been fully tested.

## Development setup

Open the repository root in Android Studio and sync the Gradle project with a compatible Android SDK. The app module declares `minSdk = 24` and `targetSdk = 36`. Gradle wrapper scripts are not checked in at the repository root.

The Gemini service reads `GEMINI_API_KEY` from `BuildConfig`. For local development, copy `.env.example` to `.env` and replace the placeholder with a development key. `.env` is ignored by Git. Do not commit API keys; because this Android app reads the key on-device, do not embed a production secret in a distributed APK. A server-side proxy is needed to keep a production key private.
