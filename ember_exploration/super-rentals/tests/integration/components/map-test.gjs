import { module, test } from 'qunit';
import { setupRenderingTest } from 'super-rentals/tests/helpers';
import { render, find } from '@ember/test-helpers';
import ENV from 'super-rentals/config/environment';
import Map from 'super-rentals/components/map';

module('Integration | Component | map', function (hooks) {
  setupRenderingTest(hooks);

  test('it renders a map image for the specified parameters', async function (assert) {
    await render(
      <template>
        <Map
          @lat="37.7797"
          @lng="-122.4184"
          @zoom="10"
          @width="150"
          @height="120"
        />
      </template>,
    );

    assert
      .dom('.map img')
      .exists()
      .hasAttribute('alt', 'Map image at coordinates 37.7797,-122.4184')
      .hasAttribute('src')
      .hasAttribute('width', '150')
      .hasAttribute('height', '120');

    let { src } = find('.map img');
    let token = encodeURIComponent(ENV.TOMTOM_ACCESS_TOKEN);

    assert.ok(
      src.startsWith('https://api.tomtom.com/'),
      'the src starts with "https://api.tomtom.com/"',
    );

    assert.ok(
      src.includes('zoom=10'),
      'the src should include the zoom parameter',
    );

    assert.ok(
      src.includes('center=-122.4184,37.7797'),
      'the src should include the lng,lat parameter',
    );

    assert.ok(
      src.includes(`key=${token}`),
      'the src should include the escaped access token',
    );
  });

  test('the default alt attribute can be overridden', async function (assert) {
    await render(
      <template>
        <Map
          @lat="37.7797"
          @lng="-122.4184"
          @zoom="10"
          @width="150"
          @height="120"
          alt="A map of San Francisco"
        />
      </template>,
    );

    assert.dom('.map img').hasAttribute('alt', 'A map of San Francisco');
  });

  test('the src, width and height attributes cannot be overridden', async function (assert) {
    await render(
      <template>
        <Map
          @lat="37.7797"
          @lng="-122.4184"
          @zoom="10"
          @width="150"
          @height="120"
          src="/assets/images/teaching-tomster.png"
          width="200"
          height="300"
        />
      </template>,
    );

    assert
      .dom('.map img')
      .hasAttribute('src', /^https:\/\/api\.tomtom\.com\//)
      .hasAttribute('width', '150')
      .hasAttribute('height', '120');
  });
});
