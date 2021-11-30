# Tower Defense

A Westfield State University themed tower defense game for Android.

## Table of Contents

* [General Information](#general-information)
    * [Built With](#built-with)
* [Screenshots](#screenshots)
* [Demo Video](#demo-video)
* [Features](#features)
* [Setup](#setup)
    * [Prerequisites & Useful Links](#prerequisites--useful-links)
    * [How to Build](#how-to-build)

## General Information

This is a tower defense game developed for Android with a Westfield State University theme. Developed utilizing Scrum methodology and Git source control for team collaboration over the span of a semester.

A user can choose from a variety of maps to play on as well as select between three different levels of difficulty. During the game, the player places towers around the map to defeat waves of spawning enemies. After a game is over, the user can enter a name to display next to their score in the high scores list corresponding to the difficulty they played.

### Built With

* Java - Android Studio
* MySQL
* Gradle

## Screenshots

<table>
    <tr>
        <td><img width="1010" alt="home" src="https://user-images.githubusercontent.com/70703647/143954046-7b1289ee-e651-47ad-883b-8461de9c763f.png"></td>
        <td><img width="1010" alt="settings" src="https://user-images.githubusercontent.com/70703647/143954121-df81eb8e-4cb7-4673-beb0-acf2acac26ee.png"></td>
    </tr>
    <tr>
        <td><img width="1010" alt="game_selection" src="https://user-images.githubusercontent.com/70703647/143954372-5236c279-8772-4d1e-a8bc-a06069f26198.png"></td>
        <td><img width="1010" alt="map_selection" src="https://user-images.githubusercontent.com/70703647/143954385-64b3a0c2-a38f-446d-b392-59173abebe60.png"></td>
    </tr>
    <tr>
        <td><img width="1010" alt="game" src="https://user-images.githubusercontent.com/70703647/143954460-d80bbd34-7f6b-4e15-bce2-1353df0fdfc8.png"></td>
        <td><img width="1010" alt="tower_upgrades" src="https://user-images.githubusercontent.com/70703647/143954448-aaf4726d-e6dd-4b33-bbad-811ebc9c8f63.png"></td>
    </tr>
</table>

## Demo Video
[![Demo Video Link](https://user-images.githubusercontent.com/70703647/143954602-149c360d-d946-4d82-af8c-dd6d43130c06.png)](https://www.youtube.com/watch?v=IHWJn54g9U0&t=2s&ab_channel=Mero3379)

## Features

* Stores and retrieves high scores from a remote AWS database using MySQL.
* Makes use of multi-threading to keep logical processes isolated from the user interface.
* Saves game progress and settings in persistent storage using serialization.

## Setup

The following is the installation process for this project on Android Studio using Gradle.

### Prerequisites & Useful Links

[Android Studio](https://developer.android.com/studio)

### How to Build

1. Follow the download instructions for Android Studio.
2. Clone the project - https://github.com/DavinPro/WSUTowerDefense.git
3. Open the AVD Manager and create a new virtual device: Nexus 6P running Android 8.1 Oreo (API 27)
4. Build the project and run the MainActivity class - ```com/wsu/towerdefense/view/activity/MainActivity.java```
