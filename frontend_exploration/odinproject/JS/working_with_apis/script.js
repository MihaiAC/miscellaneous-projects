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
    let jsonResponse = await response.json();
    return jsonResponse;
  }
}

class Weather {
  static #address = document.getElementById("address");
  static #conditions = document.getElementById("conditions");
  static #cloudcover = document.getElementById("cloudcover");
  static #temp = document.getElementById("tempC");

  constructor(resolvedAddress, conditions, cloudcover, temp) {
    this.address = resolvedAddress;
    this.conditions = conditions;
    this.cloudcover = cloudcover;
    this.tempC = (((temp - 32) * 5) / 9).toFixed(2);
  }

  displayWeather() {
    Weather.#address.textContent = this.address;
    Weather.#conditions.textContent = this.conditions;
    Weather.#cloudcover.textContent = this.cloudcover + "%";
    Weather.#temp.textContent = this.tempC + "°C";
  }
}
