# Web Browser Simulator

An academic project for a data structures course. It implements a browser
simulator with user profiles, browsing history and navigation, bookmarks,
configuration options, and extensions.

## Features

- Create and update user profiles.
- Save bookmarks and configuration options for each profile.
- Register extensions, associate them with profiles, and add comments.
- Visit pages with a date and time, maintain browsing history, and retrieve the
  most visited pages.
- Navigate backward and forward through each profile's browsing history.
- Resolve domain names using a configurable local DNS table.

## Project structure

- `src/main/java/uoc/ds/pr/pr2`: the `Browser` and `BrowserHelper` interfaces,
  their implementation, and navigation management.
- `src/main/java/uoc/ds/pr/model`: models for profiles, pages, history,
  bookmarks, extensions, and other entities.
- `src/main/java/uoc/ds/pr/util`: custom data structures and DNS resolution.
- `src/main/java/uoc/ds/pr/exceptions`: domain-specific exceptions.
- `src/test/java` and `src/test/resources`: automated tests and test CSV data.

> **Important:** Modifying the tests provided with this project is strictly
> prohibited.

The main interface is `uoc.ds.pr.pr2.Browser`, implemented by
`BrowserPR2Impl`. The `getHelper()` method provides helper operations for
querying data and adding DNS entries. A page's domain must be registered in the
DNS table before the page can be visited.

## Requirements

- JDK 24 or later.
- Apache Maven.

The project uses the `DSLib` library included in `lib/DSLib-2.1-8.jar` and declares its
testing and CSV dependencies in `pom.xml`.
