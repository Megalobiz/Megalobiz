# Megalobiz
Android Mobile App of Megalobiz Website.

Native android app of the [Megalobiz](https://www.megalobiz.com) website , which is a Music Social Network allowing Users to follow Bands and Musicians and also to see theirs Albums and listen to their Songs.

Here’s a complete list of User Stories on which we will be focused:
## A- Required User Stories

* [x] If not yet registered, user will be proposed to create an account
* [x] User can login and get authenticated

### Any User can:
* [x] See Home page with top 5 of the following: Bands, Musicians, Albums, and Songs
* [x] See a list of Bands
* [x] The full band page is displayed when user clicks on a band
* [x] Band page may have related musicians, albums and songs when possible
* [x] See a list of Musicians
* [x] The full musician page is displayed when user clicks on a musician
* [x] Musician page may have related albums and songs when possible
* [x] See a list of Albums
* [x] The full album page is displayed when user clicks on an album
* [x] Album page must have related songs, which are the tracks
* [x] Songs can be played directly in the list with a music player
* [x] See a list of Songs
* [x] The full song page is displayed when user clicks on a song
* [x] User can play the song in the song page
* [x] The song may have list of other musicians that collaborate as Featuring
* [x] User can search for all 4 entities (band, musician, album, song)

### Only Registered User can:
* [ ] User can comment on albums and on songs
* [x] User can Respect (same as Like) all 4 Entities
* [x] User can Rate Songs
* [ ] User can receive notifications about reply on his comments

## B- Optional User Stories

If we have enough time, we will implement the social aspect, which involves posting, sharing and friendship. So when information is released, we can have control on its propagation, we can also calculate the network expansion graph easily.

### Only for registered Users:
* [ ] User can send, accept and ignore friend request
* [ ] User can create new Post
* [ ] User can Amplify (same as Share) Song and Post
* [ ] User can respect and comment a Post
* [ ] User can receive notifications about comment and respect on his Post


## WireFrame 

Here's the WireFrame of implemented user stories:
Please click to see [WireFrame](http://htmlpreview.github.io/?https://github.com/Megalobiz/Megalobiz/blob/master/WireFrame/index.html) 

WireFrame created with [Pencil](http://pencil.evolus.vn).

## Video previews of the App

### 1- Preview with GUEST _____ 2- Preview with Authenticated User:

<img src="Megalobiz_Guest.gif" title="Video Preview with Guest" alt="Video Preview with Guest">
<img src="Megalobiz_AuthUser.gif" title="Video Preview with Authenticated User" alt="Video Preview with Authenticated User">

Gif Animation created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- CodePath Android-Auth-Handler
- ActiveAndroid SNAPSHOT
- Picasso Transformation
- Android Image GPU Filters
- Nine Old Androids

## License

    Copyright [2016] [Megalobiz]

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
