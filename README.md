# YumCycle: Revolutionizing Food Management

## Overview

YumCycle is a mobile application designed to help users efficiently manage their fridge and pantry contents, reduce food waste, and discover new recipes based on available ingredients. The app combines inventory management with recipe discovery, offering barcode scanning, manual entry options, personalized recipe suggestions, and a community-driven recipe-sharing platform.

## Features

- **Barcode Scanning:** Quickly add items to your virtual fridge by scanning product barcodes.
- **Manual Entry:** Manually input product details for items without barcodes.
- **Recipe Suggestions:** Get personalized recipe recommendations based on available ingredients.
- **Inventory Management:** Keep track of fridge and pantry contents.
- **Community Engagement:** Share and discover recipes from other users.

## Technologies Used

- **Backend:** Java, Spring Boot
- **Database:** MySQL
- **API Integrations:**
  - **Barcode API** for product details retrieval
  - **Recipe API** for dynamic recipe suggestions
- **Frontend:** Android (Java/Kotlin)
- **Security:** Spring Security for authentication and data protection

## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/CalinaBorzan/Yum_Cycle.git
   cd Yum_Cycle
   ```
2. **Install Dependencies:**
   ```bash
   mvn install
   ```
3. **Set Up the Database:**
   - Create a MySQL database named `yumcycle`.
   - Update `application.properties` with database credentials.
4. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```
5. **Access the App:**
   - Open [http://localhost:8080]

## System Architecture

### Presentation Layer (Frontend)
- Android mobile application
- Barcode scanning via ZXing/Google ML Kit

### Application Layer (Backend)
- RESTful API built with Spring Boot
- User authentication via Spring Security

### Data Layer (Database)
- MySQL storage for users, inventory, and recipes
- Entity-Relationship Modeling


## Portability

- **Android Compatibility:** Java/Kotlin development
- **Backend Compatibility:** Runs on any Java-supported server
- **Cloud Deployment:** Compatible with AWS, Google Cloud, Azure
- **Future Expansion:** Potential for cross-platform development

## Competing Software

Compared to apps like Kitchen Pal, Swing Fridge, and My Fridge Food Tracker, YumCycle offers:
- **User-Friendly Interface**
- **No Subscription Fees**
- **Community Recipe Sharing**
- **Quick Barcode Scanning**

## Future Developments

- Improved recipe recommendation algorithm
- Mobile notifications for expiring items
- iOS app development
- Social sharing and user engagement features

## Conclusion

YumCycle is a practical, user-friendly application designed to help users manage their food inventory, reduce waste, and discover new recipes effortlessly.


