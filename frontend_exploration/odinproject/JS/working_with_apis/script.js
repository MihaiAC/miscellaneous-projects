class WeatherGetter {
  static #urlPrefix =
    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
  #apiKey;

  constructor(apiKey) {
    this.#apiKey = apiKey;
  }

  buildUrl(location) {
    const today = new Date().toISOString().split("T")[0];
    const url =
      WeatherGetter.#urlPrefix +
      "/" +
      location +
      "/" +
      today.toString() +
      "?key=" +
      this.#apiKey;
    return url;
  }

  async getWeatherData(location) {
    const url = this.buildUrl(location);
    let response = await fetch(url);
    let json = await response.json();
    console.log(json);
  }
}
