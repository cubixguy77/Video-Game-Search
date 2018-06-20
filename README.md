# Video Game Search
Video Game Search is a native Android app that allows the user to search an extensive database of video games.  Users can filter the results by game name by typing in a search query.

## Technical Details
At a high level, the app consists of a single activity and a single fragments. I chose not to use an activity on its own as future enhancements of the app could involve implementing adding a detail screen, possibly displaying both at the same time.  When the app loads, the fragment gets an instance of the viewmodel (the factory either generates a new instance or returns an existing one if, say, the user just rotated their device).  The fragment then begins observing the list of games, as well as for network errors.  If the user just rotated their device, any previous query is restored.  Otherwise, a blank search is conducted which will result in fetching the newest games added to the GiantBomb API.

I chose to store the games inside of LiveData objects, housed in a ViewModel.  I've found that they make a great team:
  1) I no longer have to parcel the list into storage on device rotation.  The ViewModel object will survive the rotation and automatically deliver the list to the new Fragment when it subscribes.
  2) I don't have to worry about whether the Fragment is active or not when delivering results to it, the framework performs the checks for me.
  3) My ViewModel no longer needs to even hold a reference to the client View.  Instead, the View observes the data in a reactive fashion, which results in cleaner code and fewer awkward rotation bugs.

## Performance
The app has a number of optimizations in place to boost performance.
  1) Picasso: Picasso is a popular library for loading images in Android.  This library has automatic memory and disk caching and will efficiently fit images fetched from the server into our list.
  2) RecyclerView: Inflating layouts is expensive. Using RecyclerView for the list, we only inflate as many instances of the list_item layout as will fit on screen at any given time.
  3) Rotations/Config Changes: Data is held in a ViewModel which survives rotations, so the data does not need to be parceled into storage on rotation, nor do we need to hit the server again with a duplicate request.
  4) Infinite scroll: rather than fetch the entire list, I've implemented infinite scroll, whereby the data is fetched page by page, which cuts down on bandwidth.


## Libraries
*  **Retrofit**: for network calls
*  **Gson**: for parsing the JSON, used by Retrofit
*  **Butterknife**: for resource binding (ain't nobody got time for findViewById)
*  **Picasso**: for image loading
*  **Architecture Components**: for making life easier
*  Testing: **JUnit** for unit testing, **Espresso** for UI testing
*  Support libraries: Things like appcompat, RecyclerView, CardView
