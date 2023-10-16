# Minesweeper Game README

Welcome to the Minesweeper Game developed with IntelliJ IDEA 2023.2.1. This README provides instructions on how to run the application and offers an overview of its features and functionalities.

## Table of Contents
1. [Installation and Setup](#installation-and-setup)
2. [Running the Application](#running-the-application)
3. [Application Overview](#application-overview)
4. [Functionality Highlights](#functionality-highlights)
5. [Additional Information](#additional-information)
6. [UI Design and Color Palette](#ui-design-and-color-palette)

## Installation and Setup
Before running the Minesweeper Game, please follow these initial setup steps:

**1. Install Required Fonts:**
   The application uses the "Gil Sans Ultra Bold" font (*GILSANUB.TTF*) and the "Tempus Sans ITC” font (*TEMPSITC.TTF*) to correctly display the title text. Make sure you have these fonts installed in your system's font directory, typically located at `C:\WINDOWS\FONTS\`.

**2. Open the Project in IntelliJ IDEA:**
   - Launch IntelliJ IDEA on your computer.
   - Go to *File > Open* and navigate to the directory where you've placed the Minesweeper project.
   - Select the project folder and click *OK* to open it in IntelliJ IDEA.

## Running the Application
To run the Minesweeper application, follow these steps:

**3. Compile and Run:**
   - In IntelliJ IDEA, locate and right-click on the `welcomeScreen` file.
   - Select "Run" to build and execute the Minesweeper app.
   - *Note*: The application leverages voice recognition libraries from Sphinx. To ensure proper functionality, you need to add the `sphinx4-data-1.0.0` and `sphinx4-core-1.0.0` JAR files, which can be found in the `lib` folder of the project.
   
## Application Overview
The Minesweeper Game provides an engaging and interactive gaming experience. It consists of various screens, including the welcome screen, help screen, difficulty selection, Minesweeper game, game over, and winning screens. Users are guided through these screens via a fade-out transition.

## Functionality Highlights
The Minesweeper Game boasts a range of functionalities to enhance the gaming experience, including:

- **Difficulty Selection Screen:** Users can choose their preferred game difficulty level (Easy, Medium, or Hard) and receive information about grid size, mine count, and time limit.
- **Minesweeper Gameplay:** Clicking on cells reveals the number of adjacent mines or a bomb if the cell contains one. Right-clicking allows users to flag cells suspected of containing mines.
- **Voice Input:** Users can activate voice recognition by saying "Show," revealing the bombs in the game for 5 seconds.
- **Drawing Challenge:** A drawing mini-game is available, where users must connect three points to form a "circular" shape within 5 seconds to gain 30 seconds of gameplay.
- **Trivia Challenge:** After 10 seconds of gameplay, a trivia challenge is presented to test the user's knowledge about the grid's cell count. Correct answers reveal the bomb locations for 5 seconds.

## Additional Information
- The application leverages a custom CSS file (*mycss.css*) for styling.
- External resources, including Stack Overflow and ChatGPT, were consulted for guidance in implementing certain features.

## UI Design and Color Palette
The Minesweeper Game follows UI design guidelines and features a carefully chosen color palette for a visually appealing and harmonious user experience.

For any questions or support related to the Minesweeper Game, please feel free to contact me.

Enjoy playing Minesweeper!
