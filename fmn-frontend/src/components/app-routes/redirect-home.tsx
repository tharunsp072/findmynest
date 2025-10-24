import { Component, h, Prop, State, Event, EventEmitter, Method } from '@stencil/core';

@Component({
  tag: 'redirect-home',
  shadow: true,
})
export class RedirectHome {
  componentWillLoad() {
    window.history.replaceState({}, '', '/login');
    window.location.reload();
  }

  render() {
    return <p>Redirecting...</p>;
  }
}