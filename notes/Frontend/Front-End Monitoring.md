Source: https://www.freecodecamp.org/news/the-front-end-monitoring-handbook/

Data Collection:
- Error Monitoring:
	- Resource loading errors
	- JS errors
	- Promise errors
	- Custom errors
- Performance Monitoring:
	- Resource loading time
	- API Request Time
	- DNS, TCP, First-byte Time
	- FPS Rate (I assume for video content, or background gifs?)
	- Cache Hit Rate
	- First Screen Render Time
	- FP, FCP, LCP, FID, LCS, DOMContentLoaded, onload
- Behavior monitoring
	- UV, PV
	- Page Access Depth
	- Page Stay Duration
	- Custom Event Tracking
	- User Clicks
	- Page Navigation

Data Reporting:
- Methods:
	- xhr = JS API to send HTTP request, can be blocked by adblockers and delay `unload` or `pagehide` events;
	- image = sending data by setting src of a new Image() to a tracking URL (WTF?! - no CORS, no success confirmation, bypasses some CSPs, leaking data in the URL too, starting to doubt this article lol)
	- sendBeacon = browser API designed to send small bits of data async; no blocking, reliable during page unloads - probably the default method;
- Timing:
	- requestIdle callback / setTimeout;
	- upload when cache limit is reached;
	- beforeUnload;

Measures to monitor page performance:
- FP = first paint = time from when the page starts loading until the first pixel is painted on the screen;
- FCP = first contentful paint = time for page load start until any part of page content is rendered;
- LCP = largest contentful paint = time for page load start until the largest text block or image element completes rendering;
- CLS = cumulative layout shift = cumulative score of all unexpected layout shifts occurring between page load start and when the page's lifecycle state becomes hidden;

Actually measuring these things: the Performance API
https://developer.mozilla.org/en-US/docs/Web/API/Performance_API

Example code:
FP = time until something on the page changes
```javascript

const observer = new PerformanceObserver(entryHandler);
observer.observe({ type: 'paint', buffered: true });
```
Where entryHandler is a custom function.
Example entryHandler:
```javascript
const entryHandler = (list) => {        
    for (const entry of list.getEntries()) {
        if (entry.name === 'first-paint') {
            observer.disconnect()
        }
        console.log(entry)
    }
}
```
**FCP** = time until the first element on the page is loaded.
`<1.8s` good
`>3s` bad
Code same as above, but check for `'first contentful-paint'` instead.

**LCP**: `<2.5s` good, `>4s` bad

For Google SEO, LCP is used to gauge site performance, can negatively impact it if the competitors are strong.

Elements that have the highest chance to be the LCP: images, videos, elements with background images loaded through `url()`, 

**CLS** stuff like banners, ads appearing and shifting other elements potentially causing misclicks; 
`layout shift score = impact score * distance score`
`impact score` = how unstable elements affect the visible area between two frames
`distance score` = greatest distance any unstable element has moved and dividing it by the viewport's largest dimension (width or height);
CLS = sum of all layout shift scores

Session window = one or more individual layout shifts occurring in rapid succession, <1s between each shift, maximum window duration of 5s. Window = distance between first shift and last shift.

Calculation methods:
- Cumulative (disadvantages long lived pages)
- Average of all session windows
- Maximum of all session windows


**Detecting when page is fully loaded**
- `DomContentLoaded` = triggered when the HTML is fully loaded and parsed, without waiting for CSS, images, iframes;
- `load` = triggered when the entire page + all dependent resources have finished loading;

**`PerformanceObserver`** = way to monitor resource and navigation events.
https://developer.mozilla.org/en-US/docs/Web/API/PerformanceObserver

**Determining if a resource hit the cache** = `transferSize` field is 0;
If `transferSize != 0` but `encodedBodySize=0` => browser asked the server, server replied with "your cached version is fresh". 

**BFC** = Browser Back/Forward Cache = memory cache that saves the entire page in memory =>  can measure timing metrics on this too (can recognise it with an on 'pageshow' event trigger).

Could also add event listeners to routers and measure how long "navigation" takes in an SPA.

### Error data collection
**Resource loading errors** = when an external resource failes to load (images, stylsheets, scripts, fonts, etc.)

Can add an event listener for errors and just use e.target.src or e.target.href to decide if it's a resource loading thing.

**Javascript errors** = errors that occur during script execution
`window.onerror(msg, url, line, column, error)`

**Promise errors** = when a promise rejection is not handled with try-catch.
`window.addEventListener('unhandledrejection', e) => {...}`
Good for API call failures for instance.

**Sourcemap** - could restore minified error location with the `source-map` package in Node environments.

**Framework-specific** error monitoring.

### Behavior data collection
Number of page views, unique visitors
Page stay duration
Page access depth = how far they scroll?
Monitoring mouse clicks
Page navigation



