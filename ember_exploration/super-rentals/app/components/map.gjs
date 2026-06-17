import Component from '@glimmer/component';
import ENV from 'super-rentals/config/environment';

export default class Map extends Component {
  get token() {
    return encodeURIComponent(ENV.TOMTOM_ACCESS_TOKEN);
  }

  <template>
    {{yield}}
    <div class="map">
      <img
        alt="Map image at coordinates {{@lat}},{{@lng}}"
        ...attributes
        src="https://api.tomtom.com/map/1/staticimage?key={{this.token}}&zoom={{@zoom}}&center={{@lng}},{{@lat}}&width={{@width}}&height={{@height}}"
        width={{@width}}
        height={{@height}}
      />
    </div>
  </template>
}
