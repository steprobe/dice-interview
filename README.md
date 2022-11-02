# Dice Interview Application

Android app that allows to search for Artists via the MusicBrainz API and then navigate to another screen with Artist details and a list of albums

## Technical Details

- Standard MVVM using jetpack components
- One Activity, 2 Fragments - Navigation by jetpack Navigation
- Hilt used for Dependency Injection
- Glide used for loading images
- Retrofit for HTTP requests
- JVM Unit tests for View Models and some of the data mappers
- For the details screen, I used another API to get the Album art (coverartarchive). First I load and render the details, and as the art urls get loaded update the list so there is as little user interruption as possible. There are a lot of networks requests to coverartarchive for artists with large back catalogues, so I didn't want that to block the user.

## To improve

With more time the following could be done:

- The UI is obviously very basic, I am not a designer so just used straightforward material3 components and styling.
- Pagination. Currently the search loads the first 100 results.
- E2E UI Testing
- Offline support perhaps makes sense for the artist details page. That could be done with a Room DB.
- Support for loading state, some sort of progress bar or spinner.
