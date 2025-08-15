<a id="readme-top"></a>

<div align="center">
  <a href="https://github.com/your-username/cinematch">
    <img src="images/logo.png" alt="Cinematch Logo" width="80" height="80">
  </a>
  <h3 align="center">Cinematch</h3>
  <p align="center">
    Tear open the silver screen and step inside your favorite movie posters! Cinematch extracts color palettes from posters and recommends Dulux paints inspired by them, perfect for turning your space into a cinematic experience.  
    <br />
    <a href="https://github.com/your-username/cinematch"><strong>Explore the docs »</strong></a>
    <br /><br />
    <a href="https://github.com/your-username/cinematch">View Demo</a>
    &middot;
    <a href="https://github.com/your-username/cinematch/issues/new?labels=bug">Report Bug</a>
    &middot;
    <a href="https://github.com/your-username/cinematch/issues/new?labels=enhancement">Request Feature</a>
  </p>
</div>

---

## Table of Contents
<details>
  <summary>Click to expand</summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#built-with">Built With</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
  </ol>
</details>

---

## About The Project

[![Cinematch Screenshot][product-screenshot]](https://example.com)

Cinematch is a Spring Boot application that scrapes Redbubble for movie poster items, extracts the dominant colors from those posters, and recommends paint colors inspired by them. Perfect for movie fans who want their room to match their favorite films.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

---

### Built With

* Java
* Spring Boot
* JSoup
* Gradle

<p align="right">(<a href="#readme-top">back to top</a>)</p>

---

## Getting Started

Follow these steps to run Cinematch locally.

### Prerequisites

* Java 21
* JSoup
* * Internet connection for API requests

[//]: # (```bash)

[//]: # (# install Maven globally)

[//]: # (brew install maven  # macOS)

[//]: # (sudo apt install maven  # Linux)

### Installation

1. Clone the repo:
   ```sh
   git clone https://github.com/github_username/cinematch.git
2. Build the project:
    ```sh
    ./gradlew build
3. Run the app:
   ```sh
   ./gradlew bootRun
<p align="right">(<a href="#readme-top">back to top</a>)</p>
Usage:
Upload a movie poster or select one from the database. Cinematch scans its color palette and recommends corresponding Dulux paint shades for your space.
For detailed usage examples, check the [Documentation](https://docs.google.com/document/d/1_LDwxvYwSxnguonMLsoSTxoX99Ivz39-PsO6u093vNk/edit?tab=t.0).


<p align="right">(<a href="#readme-top">back to top</a>)</p>
## Roadmap / Service Summary

1. **MovieService** – Fetches movies from TMDB, extracts dominant colours, and stores them.
2. **ImageService** – Extracts dominant and secondary colours from movie posters.
3. **ColourService** – Generates and stores 12-colour palettes from extracted colours.
4. **PaletteToDuluxService** – Finds closest matching Dulux paints for a given palette.
5. **PaintMatchService** – Orchestrates paint matching and stores results.
6. **MovieRecommendationService** – Recommends movies with similar colour profiles.


See the open issues for full details.
<p align="right">(<a href="#readme-top">back to top</a>)</p> ```
